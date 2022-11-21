package com.example.springbootpetproject.service.anotherServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Slf4j
public class MailService {
    private static final String MAIL_ADDRESS = "testspringwebapp@gmail.com";

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        log.debug("In the sendSimpleMessage method");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_ADDRESS);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
        log.info("Message send...");
        log.debug("End of sendSimpleMessage method");
    }

    public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment,String fileName) throws MessagingException {
        log.debug("In the sendMessageWithAttachment method");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(MAIL_ADDRESS);
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
