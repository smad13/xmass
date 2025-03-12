package com.xmas.greet.controlador.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.xmas.greet.modelo.ContactRequest;
import com.xmas.greet.servicio.EmailService;

@Controller
public class ContactController {

        
    @Autowired
    private EmailService emailService;

    @PostMapping("/contact/send")
    public String sendContactMessage(@ModelAttribute ContactRequest request, Model model) {

        String recipient = "moraandres1013@gmail.com";

       emailService.sendEmail(recipient, request.getSubject(), request.getContent());

        model.addAttribute("message", "Correo enviado correctamente.");
        return "inicio/index"; // Retornar a la misma vista
    }
}

