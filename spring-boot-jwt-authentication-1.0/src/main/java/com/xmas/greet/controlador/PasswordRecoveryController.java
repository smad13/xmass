package com.xmas.greet.controlador;

import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xmas.greet.modelo.PasswordResetToken;
import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.servicio.PasswordResetTokenService;
import com.xmas.greet.servicio.UsuarioServicio;

@RestController
@RequestMapping("/recover-password")
public class PasswordRecoveryController {

    private final UsuarioServicio usuarioService;
    private final PasswordResetTokenService tokenService;
    private final JavaMailSender mailSender;

    public PasswordRecoveryController(UsuarioServicio usuarioService, 
                                      PasswordResetTokenService tokenService, 
                                      JavaMailSender mailSender) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.mailSender = mailSender;
    }

    @PostMapping
    public ResponseEntity<String> recoverPassword(@RequestParam String email) {
        Usuario usuario = usuarioService.findByCorreo(email);
        PasswordResetToken resetToken = tokenService.createToken(usuario);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Recuperación de contraseña");
        mailMessage.setText("Haz clic en el siguiente enlace para restablecer tu contraseña: " +
                "http://localhost:8080/reset-password?token=" + resetToken.getToken());

        mailSender.send(mailMessage);

        return ResponseEntity.ok("Enlace de recuperación enviado a " + email);
    }
}
