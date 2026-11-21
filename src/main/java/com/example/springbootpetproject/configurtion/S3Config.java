package com.example.springbootpetproject.configurtion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Клиенты S3. Креды не задаются явно: default credentials chain в ECS берёт временные
 * ключи task role из эндпоинта 169.254.170.2 (переменная AWS_CONTAINER_CREDENTIALS_RELATIVE_URI),
 * локально — из ~/.aws или переменных окружения.
 */
@Configuration
@ConditionalOnProperty(name = "app.tickets.storage", havingValue = "s3")
public class S3Config {

    @Bean(destroyMethod = "close")
    public S3Client s3Client(@Value("${app.tickets.s3.region}") String region) {
        return S3Client.builder().region(Region.of(region)).build();
    }

    @Bean(destroyMethod = "close")
    public S3Presigner s3Presigner(@Value("${app.tickets.s3.region}") String region) {
        return S3Presigner.builder().region(Region.of(region)).build();
    }
}
