package com.Tisj.api.controllers;

import com.Tisj.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("prueba/string")
    public ResponseEntity<String> enviarMail(@RequestParam("destinatario") String destinatario,
                                             @RequestParam("asunto") String asunto) {
        try {
            emailService.enviarCorreo(destinatario, asunto, "Probando", false);
        }catch (Exception e){
            log.error("Error: ", e);
        }
        return ResponseEntity.ok("Correo enviado");
    }

    @GetMapping("prueba/html")
    public ResponseEntity<String> enviarMail(@RequestParam("destinatario") String destinatario) {
        try {
            emailService.notiCompra(destinatario);
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return ResponseEntity.ok("Correo enviado");
    }
}