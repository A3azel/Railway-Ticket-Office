# ---- Этап сборки ----
FROM maven:3.9-eclipse-temurin-18 AS build
WORKDIR /app

# Сначала копируем pom для кэширования зависимостей
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Копируем исходники и собираем jar (тесты пропускаем)
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Этап запуска ----
FROM eclipse-temurin:18-jre
WORKDIR /app

# Копируем собранный jar из этапа сборки
COPY --from=build /app/target/springbootpetproject-0.0.1-SNAPSHOT.jar app.jar

# Шрифт нужен PDFService как файл на ФС (addFont по пути), а не из classpath,
# поэтому кладём его в образ отдельно
COPY --from=build /app/src/main/resources/fonts/DejaVuSans.ttf /app/fonts/DejaVuSans.ttf

# Каталог для генерируемых PDF (в исходном коде это src/main/resources/ticketPDF,
# которого в рантайм-образе нет — создаём и переопределяем пути через ENV ниже)
RUN mkdir -p /app/pdf

# Пути по умолчанию под контейнер (переопределяемы через окружение ECS/compose)
ENV PDF_PATH=/app/pdf/ \
    PDF_PATH_FOR_EMAIL=/app/pdf/ \
    FONTS_PATH=/app/fonts/DejaVuSans.ttf

# Запуск от непривилегированного пользователя
RUN useradd -r -u 1001 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
