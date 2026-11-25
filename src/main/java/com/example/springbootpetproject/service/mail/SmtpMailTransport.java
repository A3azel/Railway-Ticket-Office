package com.example.springbootpetproject.service.mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/** Отправка через SMTP-сервер из spring.mail.* (например, Gmail) — для локального запуска. */
@Component
@ConditionalOnProperty(name = "app.mail.transport", havingValue = "smtp", matchIfMissing = true)
public class SmtpMailTransport implements MailTransport {

    private final JavaMailSender javaMailSender;

    public SmtpMailTransport(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void send(MimeMessage message) {
        javaMailSender.send(message);
    }
}
