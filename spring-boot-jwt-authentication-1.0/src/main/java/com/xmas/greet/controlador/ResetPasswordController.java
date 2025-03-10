package com.xmas.greet.controlador;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.xmas.greet.servicio.PasswordResetTokenService;
import com.xmas.greet.servicio.UsuarioServicio;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final PasswordResetTokenService tokenService;
    private final UsuarioServicio usuarioService;
    private final PasswordEncoder passwordEncoder; 

    public ResetPasswordController(UsuarioServicio usuarioService, 
                                    PasswordResetTokenService tokenService,
                                    PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder; 
    }

    @GetMapping
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        try {

            tokenService.validateToken(token);
            model.addAttribute("token", token);
            return "reset-password"; 
        } catch (Exception e) {
 
            model.addAttribute("error", "El token es inválido o ha expirado.");
            return "error"; 
        }
    }

   
    
}
