package com.xmas.greet.controlador.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.xmas.greet.controlador.dto.UsuarioRegistroDTO;
import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.servicio.UsuarioServicio;

@Controller
@RequestMapping("/registroadmin")
public class RegistroAdminControlador {

    private final UsuarioServicio usuarioServicio;

    public RegistroAdminControlador(UsuarioServicio usuarioServicio) {
        super();
        this.usuarioServicio = usuarioServicio;
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }

    // Endpoint para mostrar el formulario en modo "nuevo registro"
    @GetMapping
    public String mostrarFormularioRegistro() {
        return "dashboard/Empleados-form";
    }
    
    // Endpoint para mostrar el formulario en modo "editar"
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        Usuario usuario = usuarioServicio.obtenerPorId(id);
        UsuarioRegistroDTO registroDTO = new UsuarioRegistroDTO();
        registroDTO.setId(usuario.getId());
        registroDTO.setNombre(usuario.getNombre());
        registroDTO.setApellido(usuario.getApellido());
        registroDTO.setDireccion(usuario.getDireccion());
        registroDTO.setCorreo(usuario.getCorreo());
        registroDTO.setTipo_documento(usuario.getTipo_documento());
        registroDTO.setNumero_documento(usuario.getNumero_documento());
        // Se asigna el rol actual (se asume que el usuario tiene al menos un rol)
        registroDTO.setRol(usuario.getRoles().iterator().next().getNombre());
        // No se carga la contraseña para edición; se dejará vacía si no se desea actualizar
        model.addAttribute("usuario", registroDTO);
        return "dashboard/Empleados-form";
    }

    // Endpoint que procesa tanto el registro como la edición
    @PostMapping("/usuario")
    public String guardarOEditarUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
        if (registroDTO.getId() == null) {
            // Registro nuevo
            usuarioServicio.guardar(registroDTO);
            return "redirect:/empleados?exito";
        } else {
            // Actualización de usuario existente
            usuarioServicio.editarUsuario(registroDTO);
            return "redirect:/empleados?exito";
        }
    }
}
