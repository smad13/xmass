package com.xmas.greet.controlador.Vendedor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.servicio.UsuarioServicio;

@Controller
public class ControladorVendedor {

    @Autowired
    private UsuarioServicio usuarioServicio;

    //PANEL 
    @GetMapping("/vendedor/dashboard")
    public String dashboard_vendedor() {
        return "dashboard-vendedor/index";
    }


    //Clientes
    @GetMapping("/vendedor/clientes")
	public String obtenerClientes(Model model) {
    List<Usuario> usuarios = usuarioServicio.listarUsuarios();
    List<Usuario> clientes = usuarios.stream()
            .filter(usuario -> usuario.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_CLIENTE")))
            .collect(Collectors.toList());
			model.addAttribute("clientes", clientes);
		return "/dashboard-vendedor/clientes";
	}

    //Reportes
    @GetMapping("/vendedor/REPORTES")
    public String mostrarReportes() {
        return "dashboard-vendedor/reportes";
    }


}
