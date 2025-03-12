package com.xmas.greet.controlador.Vendedor;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;
import com.xmas.greet.modelo.DETALLE;
import com.xmas.greet.servicio.DETALLEService;
import com.xmas.greet.util.estadistics.DETALLEExporterExcel;
import com.xmas.greet.util.estadistics.DETALLEExporterPDF;

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
@RequestMapping("/vendedor/DETALLE")
public class DETALLEControllerVendedor {

    @Autowired
    private DETALLEService service;

    @GetMapping("/NUEVO")
    public String mostrarFormularioNuevoDETALLE(Model model) {
        model.addAttribute("DETALLE", new DETALLE());
        return "dashboard-vendedor/DETALLE-form";
    }

    @GetMapping("/LISTA")
public String listarDETALLES(Model model) {
    List<DETALLE> detalles = service.listarTodas();
    model.addAttribute("DETALLES", detalles);
    return "dashboard-vendedor/DETALLE-lista";
}



    @PostMapping("/GUARDAR")
    public String guardarDETALLE(@ModelAttribute DETALLE detalle, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "dashboard-vendedor/DETALLE-form";
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
            return "dashboard-vendedor/DETALLE-form";
        }

        return "redirect:/vendedor/DETALLE/LISTA";
    }

    @GetMapping("/EDITAR/{id_detalle}")
public String mostrarFormularioEditarDETALLE(@PathVariable Long id_detalle, Model model) {
    DETALLE detalle = service.obtenerPorId(id_detalle);
    model.addAttribute("DETALLE", detalle);
    return "dashboard-vendedor/DETALLE-form";
}


@GetMapping("/ExportarPDF")
public void exportarListaDETALLESPDF(HttpServletResponse response) throws DocumentException, IOException {
    response.setContentType("application/pdf");

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String fechaActual = dateFormatter.format(new Date());

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=DETALLES_" + fechaActual + ".pdf";
    response.setHeader(headerKey, headerValue);

    List<DETALLE> DETALLES = service.listarTodas();

    DETALLEExporterPDF exporterPDF = new DETALLEExporterPDF(DETALLES);
    exporterPDF.export(response);

    response.getOutputStream().close();
}


    @GetMapping("/ExportarExcel")
    public void exportarListaDETALLESExcel(HttpServletResponse response) throws DocumentException, IOException {
        System.out.println("Método exportarListaDETALLESExcel llamado");

        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String actualDate = dateFormatter.format(new Date());

        String header = "Content-Disposition";
        String value = "attachment; filename=DETALLES_" + actualDate + ".xlsx";

        response.setHeader(header, value);

        List<DETALLE> DETALLES = service.listarTodas();

        DETALLEExporterExcel exporterExcel = new DETALLEExporterExcel(DETALLES);
        exporterExcel.export(response);
    }
}
