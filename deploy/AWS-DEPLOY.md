# Деплой на AWS ECS Fargate + RDS

Схема: образ в **ECR** → задача в **ECS Fargate** за **ALB** → БД в **RDS for MySQL**.
Секреты (пароли БД и почты) — в **Secrets Manager**.

Ниже — команды AWS CLI. Замени плейсхолдеры:
`<REGION>` (напр. `eu-central-1`), `<ACCOUNT_ID>`, `<RDS_ENDPOINT>`, идентификаторы VPC/подсетей/групп безопасности.

---

## 0. Предпосылки
- Установлен и настроен AWS CLI (`aws configure`), Docker.
- Существует VPC с как минимум двумя подсетями в разных AZ (подойдёт default VPC).

```bash
export REGION=eu-central-1
export ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
```

---

## 1. ECR: репозиторий и пуш образа

```bash
# создать репозиторий
aws ecr create-repository --repository-name springbootpetproject --region $REGION

# логин docker в ECR
aws ecr get-login-password --region $REGION \
  | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com

# сборка и пуш (из корня проекта)
docker build -t springbootpetproject .
docker tag springbootpetproject:latest $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/springbootpetproject:latest
docker push $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/springbootpetproject:latest
```

---

## 2. RDS: база MySQL

```bash
aws rds create-db-instance \
  --db-instance-identifier railway-db \
  --engine mysql --engine-version 8.0 \
  --db-instance-class db.t3.micro \
  --allocated-storage 20 \
  --master-username admin \
  --master-user-password '<СИЛЬНЫЙ_ПАРОЛЬ>' \
  --db-name Railway_Ticket_Office \
  --backup-retention-period 7 \
  --no-publicly-accessible \
  --region $REGION
```

Дождись статуса `available` и запомни endpoint:
```bash
aws rds describe-db-instances --db-instance-identifier railway-db \
  --query 'DBInstances[0].Endpoint.Address' --output text
```

> Важно: `spring.jpa.hibernate.ddl-auto=update` создаст таблицы сам при первом старте.
> Security group RDS должна разрешать входящий 3306 от security group ECS-задач.

---

## 3. Secrets Manager: пароли

```bash
aws secretsmanager create-secret --name railway/db-password \
  --secret-string '<СИЛЬНЫЙ_ПАРОЛЬ>' --region $REGION

aws secretsmanager create-secret --name railway/mail-password \
  --secret-string '<GMAIL_APP_PASSWORD>' --region $REGION
```

Роль `ecsTaskExecutionRole` должна иметь политику `AmazonECSTaskExecutionRolePolicy`
и разрешение `secretsmanager:GetSecretValue` на эти два секрета.

---

## 4. ECS: кластер и задача

```bash
# кластер
aws ecs create-cluster --cluster-name railway-cluster --region $REGION

# зарегистрировать задачу (предварительно подставь ARNы/endpoint в deploy/ecs-task-definition.json)
aws ecs register-task-definition \
  --cli-input-json file://deploy/ecs-task-definition.json --region $REGION
```

---

## 5. ALB + сервис

1. Создай **Application Load Balancer** (internet-facing) в двух публичных подсетях.
2. Создай **target group**: тип `ip`, порт `8080`, протокол HTTP.
   Health check: путь **`/actuator/health/readiness`**, коды успеха **200**.
   В группу `readiness` явно добавлена проверка БД, поэтому задача выпадет из
   балансировки при недоступности RDS — в отличие от `/login`, который отвечал бы
   200 и с мёртвой базой.

   > Не используй `/actuator/health` — он агрегирует все индикаторы, и приложение
   > будет считаться нездоровым из-за второстепенных подсистем.
3. Listener ALB на `:80` (и `:443` с ACM-сертификатом для HTTPS) → target group.
4. Создай сервис:

```bash
aws ecs create-service \
  --cluster railway-cluster \
  --service-name railway-service \
  --task-definition springbootpetproject \
  --desired-count 1 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[<SUBNET_1>,<SUBNET_2>],securityGroups=[<ECS_SG>],assignPublicIp=ENABLED}" \
  --load-balancers "targetGroupArn=<TARGET_GROUP_ARN>,containerName=app,containerPort=8080" \
  --region $REGION
```

> **`APP_BASE_URL`** в task definition должен указывать на публичный адрес ALB
> (или домен) — именно он подставляется в ссылку активации аккаунта в письме.
> Пока там заглушка `<ALB_DNS_OR_DOMAIN>`, регистрация будет присылать битые ссылки.
> ALB создаётся после task definition, поэтому порядок такой: создать ALB → подставить
> его DNS в `APP_BASE_URL` → перерегистрировать task definition.

---

## 6. Проверка
- ECS → сервис → задача в статусе `RUNNING`, health check в target group `healthy`.
- Логи: CloudWatch, группа `/ecs/springbootpetproject`.
- Открой приложение по DNS-имени ALB.
- Проверь регистрацию: ссылка в письме должна вести на ALB/домен, а не на `localhost`.

---

## Security groups — минимум
- **ALB SG**: входящий 80/443 из интернета.
- **ECS SG**: входящий 8080 только от ALB SG.
- **RDS SG**: входящий 3306 только от ECS SG.

## Обновление версии
```bash
docker build -t springbootpetproject . && \
docker tag springbootpetproject:latest $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/springbootpetproject:latest && \
docker push $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/springbootpetproject:latest && \
aws ecs update-service --cluster railway-cluster --service railway-service \
  --force-new-deployment --region $REGION
```

## Примечание про PDF
PDF пишутся в `/app/pdf` внутри контейнера (эфемерная ФС Fargate) — файлы исчезают при
перезапуске задачи. Для проекта это ок: они генерируются заново по запросу и отправляются
на почту. Если нужна долговременная хранимость — смонтируй EFS в `/app/pdf` через
`volumes` + `mountPoints` в task definition.
