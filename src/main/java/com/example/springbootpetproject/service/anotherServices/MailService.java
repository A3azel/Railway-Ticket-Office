package com.example.springbootpetproject.service.anotherServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;

    /**
     * Отправитель должен совпадать с SMTP-аккаунтом, под которым мы логинимся,
     * иначе Gmail отбивает письмо.
     */
    private final String mailAddress;

    @Autowired
    public MailService(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String mailAddress) {
        this.javaMailSender = javaMailSender;
        this.mailAddress = mailAddress;
    }

    @Async
    public void sendSimpleMessage(String to, String subject, String text) {
        log.debug("In the sendSimpleMessage method");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        log.info("Message send...");
        log.debug("End of sendSimpleMessage method");
    }

    @Async
    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment,String fileName) throws MessagingException {
        log.debug("In the sendMessageWithAttachment method");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(mailAddress);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file
                = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment(fileName, file);

        javaMailSender.send(message);
        log.info("Message send...");
        log.debug("End of sendMessageWithAttachment method");
    }
}
