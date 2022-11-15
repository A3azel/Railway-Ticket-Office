package com.example.springbootpetproject.service.anotherServices;

import com.example.springbootpetproject.dto.OrderDTO;
import com.example.springbootpetproject.entity.Order;
import com.example.springbootpetproject.facade.OrderFacade;
import com.example.springbootpetproject.service.serviceImplementation.OrdersServiceI;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PDFService {
    private final MailService mailService;
    private final OrdersServiceI ordersServiceI;
    private final OrderFacade orderFacade;

    @Value("${pdf.path}")
    private String pdfPath;

    public PDFService(MailService mailService, OrdersServiceI ordersServiceI, OrderFacade orderFacade) {
        this.mailService = mailService;
        this.ordersServiceI = ordersServiceI;
        this.orderFacade = orderFacade;
    }

    public void sendTicket(Long id, String username){
        /*String emailTicket = parseThymeleafTemplate(id,username);
        try {
            generatePdfFromHtml(emailTicket,id,username);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }*/
    }

    public void generatePdfFromHtml(String html, Long id, String username) throws IOException, DocumentException {
        // set path
        String outputFolder = pdfPath + username + "_" + id + "_ticket.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
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
        context.setVariable("startCityName", orderDTO.getRoute().getStartCityName());


        return templateEngine.process("ticketPattern", context);
    }

}
