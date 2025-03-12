package com.xmas.greet.controlador.Admin;

import com.xmas.greet.modelo.PEDIDO;
import com.xmas.greet.servicio.PEDIDOService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/PEDIDO")
public class PEDIDOController {

    //Agregado
    @GetMapping("/pedido-form")
    public String mostrarFormularioPedido(Model model) {
        return "dashboard/PEDIDO-form";
}
    @Autowired
    private PEDIDOService service;

    @GetMapping("/NUEVO")
    public String mostrarFormularioNuevoPEDIDO(Model model) {
        model.addAttribute("PEDIDO", new PEDIDO());
        model.addAttribute("ESTADOS", new String[] {"PENDIENTE", "ENTREGADO", "CANCELADO"});
        return "dashboard/PEDIDO-form";
    }

    @GetMapping("/LISTA")
    public String listarPEDIDOS(Model model) {
        model.addAttribute("PEDIDOS", service.listarTodas());
        return "dashboard/PEDIDO-list";
    }

    @PostMapping("/GUARDAR")
    public String guardarPEDIDO(@ModelAttribute PEDIDO pedido) {
        System.out.println("Estado recibido: " + pedido.getEstado());

        // Si el estado es nulo, se establece como 'PENDIENTE' por defecto
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("PENDIENTE");
        }

        service.guardar(pedido);
        return "redirect:/PEDIDO/LISTA";  
    }

    @GetMapping("/EDITAR/{id_pedido}")
    public String mostrarFormularioEditarPEDIDO(@PathVariable Long id_pedido, Model model) {
        PEDIDO pedido = service.obtenerPorId(id_pedido);  
        model.addAttribute("PEDIDO", pedido);
        model.addAttribute("ESTADOS", new String[] {"PENDIENTE", "ENTREGADO", "CANCELADO"});
        return "dashboard/PEDIDO-form";  
    }

    

}
