package com.example.springbootpetproject.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;

/**
 * Билеты в приватном бакете S3. Права даёт task role задачи ECS
 * (s3:GetObject/PutObject на объекты, s3:ListBucket на бакет) — ключей доступа в коде нет,
 * SDK сам берёт временные креды роли через default credentials chain.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.tickets.storage", havingValue = "s3")
public class S3TicketStorage implements TicketStorage {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final String bucket;
    private final Duration urlTtl;

    public S3TicketStorage(S3Client s3, S3Presigner presigner,
                           @Value("${app.tickets.s3.bucket}") String bucket,
                           @Value("${app.tickets.s3.download-url-ttl}") Duration urlTtl) {
        if (bucket == null || bucket.isBlank()) {
            throw new IllegalStateException("app.tickets.s3.bucket (TICKETS_BUCKET) is not set");
        }
        this.s3 = s3;
        this.presigner = presigner;
        this.bucket = bucket;
        this.urlTtl = urlTtl;
    }

    @Override
    public boolean exists(String key) {
        try {
            s3.headObject(r -> r.bucket(bucket).key(key));
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            // Без s3:ListBucket S3 отвечает на отсутствующий объект 403, а не 404:
            // так он не раскрывает, существует ли ключ. Поэтому ListBucket в политике нужен.
            if (e.statusCode() == 404) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public void save(String key, byte[] pdf) {
        s3.putObject(r -> r.bucket(bucket).key(key).contentType("application/pdf"),
                RequestBody.fromBytes(pdf));
        log.info("ticket uploaded to s3://{}/{}", bucket, key);
    }

    @Override
    public byte[] load(String key) {
        return s3.getObjectAsBytes(r -> r.bucket(bucket).key(key)).asByteArray();
    }

    @Override
    public Optional<URI> downloadUrl(String key, String fileName) {
        var presigned = presigner.presignGetObject(p -> p
                .signatureDuration(urlTtl)
                .getObjectRequest(r -> r.bucket(bucket).key(key)
                        .responseContentType("application/pdf")
                        .responseContentDisposition("attachment; filename=\"" + fileName + "\"")));
        return Optional.of(URI.create(presigned.url().toString()));
    }
}
