package com.example.springbootpetproject.service.mail;

import jakarta.mail.internet.MimeMessage;

/**
 * Как письмо уходит наружу. Письмо собирается одинаково (MimeMessage),
 * а отправляется через SMTP (локально) или через Amazon SES API (в AWS).
 */
public interface MailTransport {

    void send(MimeMessage message);
}
