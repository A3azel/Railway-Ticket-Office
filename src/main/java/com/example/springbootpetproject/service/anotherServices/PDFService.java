package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.Order;
import com.example.springbootpetproject.facade.OrderFacade;
import com.example.springbootpetproject.service.serviceImplementation.OrdersServiceI;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.example.springbootpetproject.service.storage.TicketStorage;
import jakarta.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * Генерирует PDF-билет и кладёт его в {@link TicketStorage} (S3 в AWS, папка локально).
 * Ключ включает имя пользователя, а сам PDF создаётся только после проверки, что заказ
 * принадлежит этому пользователю, — чужой билет по ключу не получить.
 */
@Service
public class PDFService {
    private final MailService mailService;
    private final OrdersServiceI ordersServiceI;
    private final OrderFacade orderFacade;
    private final TicketStorage ticketStorage;

    @Value("${fonts.path}")
    private String fontsPath;

    public PDFService(MailService mailService, OrdersServiceI ordersServiceI, OrderFacade orderFacade,
                      TicketStorage ticketStorage) {
        this.mailService = mailService;
        this.ordersServiceI = ordersServiceI;
        this.orderFacade = orderFacade;
        this.ticketStorage = ticketStorage;
    }

    /** Ключ объекта: tickets/<username>/<orderId>.pdf */
    static String ticketKey(Long id, String username) {
        return "tickets/" + username + "/" + id + ".pdf";
    }

    static String fileName(Long id, String username) {
        return username + "_" + id + "_ticket.pdf";
    }

    /** Создаёт билет, если его ещё нет в хранилище, и возвращает ключ. */
    public String ensureTicket(Long id, String username) {
        String key = ticketKey(id, username);
        if (!ticketStorage.exists(key)) {
            String html = parseThymeleafTemplate(id, username);
            ticketStorage.save(key, generatePdfFromHtml(html));
        }
        return key;
    }

    /** Ссылка на скачивание из хранилища (S3 presigned URL) или пусто для локального хранилища. */
    public Optional<URI> downloadUrl(Long id, String username) {
        return ticketStorage.downloadUrl(ensureTicket(id, username), fileName(id, username));
    }

    public byte[] ticketBytes(Long id, String username) {
        return ticketStorage.load(ensureTicket(id, username));
    }

    @PreAuthorize("#username == authentication.principal.username")
    public void sendTicket(Long id, String username, String email){
        byte[] pdf = ticketBytes(id, username);
        try {
            mailService.sendMessageWithAttachment(email, "Railway-Ticket-Office", "Your ticket",
                    pdf, fileName(id, username));
        } catch (MessagingException e) {
            throw new IllegalStateException("cannot send ticket " + id, e);
        }
    }

    byte[] generatePdfFromHtml(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(fontsPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (IOException | DocumentException e) {
            throw new IllegalStateException("cannot render ticket PDF", e);
        }
    }

    private String parseThymeleafTemplate(Long id, String username) {
        if(!ordersServiceI.exitByUserNameAndOrderID(username,id)){
            throw new IllegalArgumentException("Order error");
        }
        Order order = ordersServiceI.getOrderById(id);
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        OrderDTO orderDTO = orderFacade.convertOrdersToOrdersDTO(order);

        Context context = new Context();
        context.setVariable("selectedOrder", orderDTO);

        return templateEngine.process("ticketPattern", context);
    }
}
