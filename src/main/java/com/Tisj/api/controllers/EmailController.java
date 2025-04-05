package com.Tisj.api.controllers;

import com.Tisj.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/enviar")
    public ResponseEntity<String> enviarMail(@RequestParam("destinatario") String destinatario,
                                             @RequestParam("asunto") String asunto) {
        emailService.enviarCorreo(destinatario, asunto, "Probando");
        return ResponseEntity.ok("Correo enviado");
    }
}