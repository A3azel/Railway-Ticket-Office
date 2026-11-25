package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.service.mail.MailTransport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Собирает письма. Как они уходят наружу (SMTP или Amazon SES) решает {@link MailTransport}
 * по свойству app.mail.transport.
 */
@Service
@Slf4j
public class MailService {
    /** Используется только как фабрика MimeMessage. */
    private final JavaMailSender javaMailSender;
    private final MailTransport mailTransport;

    /**
     * Адрес отправителя. Для Gmail SMTP должен совпадать с SMTP-аккаунтом,
     * для SES — быть подтверждённой identity (адрес или домен).
     */
    private final String mailFrom;

    public MailService(JavaMailSender javaMailSender, MailTransport mailTransport,
                       @Value("${app.mail.from}") String mailFrom) {
        this.javaMailSender = javaMailSender;
        this.mailTransport = mailTransport;
        this.mailFrom = mailFrom;
    }

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        log.debug("In the sendSimpleMessage method");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailTransport.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("cannot build email", e);
        }
        log.info("Message send...");
        log.debug("End of sendSimpleMessage method");
    }

    @Async
    public void sendMessageWithAttachment(String to, String subject, String text, byte[] attachment, String fileName) throws MessagingException {
        log.debug("In the sendMessageWithAttachment method");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(mailFrom);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        // Вложение из памяти: PDF лежит в S3, локального файла больше нет
        helper.addAttachment(fileName, new ByteArrayResource(attachment), "application/pdf");

        mailTransport.send(message);
        log.info("Message send...");
        log.debug("End of sendMessageWithAttachment method");
    }
}
