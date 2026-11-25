package com.example.springbootpetproject.configurtion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

/** Клиент Amazon SES. Креды — из default credentials chain (в ECS это task role). */
@Configuration
@ConditionalOnProperty(name = "app.mail.transport", havingValue = "ses")
public class SesConfig {

    @Bean(destroyMethod = "close")
    public SesV2Client sesV2Client(@Value("${app.mail.ses.region}") String region) {
        return SesV2Client.builder().region(Region.of(region)).build();
    }
}
