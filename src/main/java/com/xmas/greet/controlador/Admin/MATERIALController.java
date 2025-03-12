package com.xmas.greet.controlador.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xmas.greet.modelo.MATERIAL;
import com.xmas.greet.servicio.MATERIALService;


@Controller
@RequestMapping("/MATERIAL")
public class MATERIALController {

    // Agregado
    @GetMapping("/MATERIAL-form")
    public String mostrarFormularioMateriales(Model model) {
        return "dashboard/MATERIAL-form";
    }

    @Autowired
    private MATERIALService MATERIALService;

    @GetMapping
    public String listarMATERIALES(Model model) {
        model.addAttribute("MATERIAL", MATERIALService.listarTodas());
        return "dashboard/MATERIAL-list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoMATERIAL(Model model) {
        model.addAttribute("MATERIAL", new MATERIAL());
        return "dashboard/MATERIAL-form";
    }

    @PostMapping
    public String guardarMATERIAL(MATERIAL MATERIAL) {
        MATERIALService.guardar(MATERIAL);
        return "redirect:/MATERIAL";
    }

    @GetMapping("/editar/{ID_materiales}")
    public String mostrarFormularioEditarMATERIAL(@PathVariable Long ID_materiales, Model model) {
        model.addAttribute("MATERIAL", MATERIALService.obtenerPorId(ID_materiales));
        return "dashboard/MATERIAL-form";
    }

    @GetMapping("/eliminar/{ID_materiales}")
    public String eliminarMATERIAL(@PathVariable Long ID_materiales) {
        MATERIALService.eliminar(ID_materiales);
        return "redirect:/MATERIAL";
    }

    
}
