package com.xmas.greet.controlador.Admin;

import java.math.BigDecimal;
import java.util.List;
import com.xmas.greet.modelo.DETALLE;
import com.xmas.greet.servicio.DETALLEService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/DETALLE")
public class DETALLEController {

    @Autowired
    private DETALLEService service;

    @GetMapping("/NUEVO")
    public String mostrarFormularioNuevoDETALLE(Model model) {
        model.addAttribute("DETALLE", new DETALLE());
        return "dashboard/DETALLE-form";
    }

    @GetMapping("/LISTA")
public String listarDETALLES(Model model) {
    List<DETALLE> detalles = service.listarTodas();
    model.addAttribute("DETALLES", detalles);
    return "dashboard/DETALLE-lista";
}



    @PostMapping("/GUARDAR")
    public String guardarDETALLE(@ModelAttribute DETALLE detalle, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "DETALLE-form";
        }

        if (detalle.getCantidad() != null && detalle.getCantidad() > 0 &&
                detalle.getPrecio_unitario() != null && detalle.getPrecio_unitario() > 0) {
            // Calcula el subtotal usando BigDecimal
            detalle.setSubtotal(
                BigDecimal.valueOf(detalle.getCantidad())
                          .multiply(BigDecimal.valueOf(detalle.getPrecio_unitario()))
            );
            service.guardar(detalle);
        } else {
            model.addAttribute("error", "La cantidad o el precio unitario no pueden ser cero o nulos.");
            return "DETALLE-form";
        }

        return "redirect:/DETALLE/LISTA";
    }

    @GetMapping("/EDITAR/{id_detalle}")
public String mostrarFormularioEditarDETALLE(@PathVariable Long id_detalle, Model model) {
    DETALLE detalle = service.obtenerPorId(id_detalle);
    model.addAttribute("DETALLE", detalle);
    return "dashboard/DETALLE-form";
}



}
