package com.Tisj.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//@Service
public class YoutubeService{

//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void enviarCorreo(String to, String subject, String body, boolean isHtml) throws MessagingException {
//        MimeMessage mensaje = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
//
//        helper.setFrom(System.getenv("CORREO"));
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(body, isHtml);
//
//        mailSender.send(mensaje);
//    }
//
//    public void notiCompra(String to) throws IOException, MessagingException {
//        ClassPathResource resource = new ClassPathResource("templates/notificarCompra.html");
//        String htmlContent = Files.readString(Path.of(resource.getURI()));
//
//        enviarCorreo(to, "Nueva Compra", htmlContent, true);
//    }
}