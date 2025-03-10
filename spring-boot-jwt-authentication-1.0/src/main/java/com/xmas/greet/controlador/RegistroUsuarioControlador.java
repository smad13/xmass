package com.xmas.greet.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xmas.greet.controlador.dto.UsuarioRegistroDTO;
import com.xmas.greet.servicio.UsuarioServicio;



@Controller
@RequestMapping("/registro")
public class RegistroUsuarioControlador {

    private final UsuarioServicio usuarioServicio;


    public RegistroUsuarioControlador(UsuarioServicio usuarioServicio) {
        super();
        this.usuarioServicio = usuarioServicio;

    }


    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    @GetMapping
    public String mostrarFormularioDeRegistro() {
        return "registro"; 
    }

    @PostMapping("/usuario")
    public String registrarCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO, RedirectAttributes redirectAttributes) {
        usuarioServicio.guardar(registroDTO);
        redirectAttributes.addFlashAttribute("exito", "La cuenta ha sido creada exitosamente");
        return "redirect:/login?exito";
    }


 

}
