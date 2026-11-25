package com.example.springbootpetproject.service.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.sesv2.SesV2Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Отправка через Amazon SES API v2. Письмо (с вложениями) сериализуется в raw MIME
 * и уходит одним вызовом SendEmail. Права даёт task role (ses:SendEmail / ses:SendRawEmail)
 * — никаких SMTP-логинов и паролей в конфигурации.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "app.mail.transport", havingValue = "ses")
public class SesMailTransport implements MailTransport {

    private final SesV2Client ses;

    public SesMailTransport(SesV2Client ses) {
        this.ses = ses;
    }

    @Override
    public void send(MimeMessage message) {
        try (ByteArrayOutputStream raw = new ByteArrayOutputStream()) {
            message.writeTo(raw);
            var response = ses.sendEmail(r -> r.content(c -> c.raw(m -> m.data(SdkBytes.fromByteArray(raw.toByteArray())))));
            log.info("email sent via SES, messageId={}", response.messageId());
        } catch (MessagingException | IOException e) {
            throw new IllegalStateException("cannot serialize email", e);
        }
    }
}
