package com.xmas.greet.controlador.Vendedor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;
import com.xmas.greet.modelo.PEDIDO;
import com.xmas.greet.servicio.PEDIDOService;
import com.xmas.greet.util.estadistics.PEDIDOExporterExcel;
import com.xmas.greet.util.estadistics.PEDIDOExporterPDF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendedor/PEDIDO")
public class PEDIDOControllerVendedor {

    //Agregado
    @GetMapping("/pedido-form")
    public String mostrarFormularioPedido(Model model) {
        return "dashboard-vendedor/PEDIDO-form";
}
    @Autowired
    private PEDIDOService service;

    @GetMapping("/NUEVO")
    public String mostrarFormularioNuevoPEDIDO(Model model) {
        model.addAttribute("PEDIDO", new PEDIDO());
        model.addAttribute("ESTADOS", new String[] {"PENDIENTE", "ENTREGADO", "CANCELADO"});
        return "dashboard-vendedor/PEDIDO-form";
    }

    @GetMapping("/LISTA")
    public String listarPEDIDOS(Model model) {
        model.addAttribute("PEDIDOS", service.listarTodas());
        return "dashboard-vendedor/PEDIDO-list";
    }

    @PostMapping("/GUARDAR")
    public String guardarPEDIDO(@ModelAttribute PEDIDO pedido) {
        System.out.println("Estado recibido: " + pedido.getEstado());

        // Si el estado es nulo, se establece como 'PENDIENTE' por defecto
        if (pedido.getEstado() == null || pedido.getEstado().isEmpty()) {
            pedido.setEstado("PENDIENTE");
        }

        service.guardar(pedido);
        return "redirect:/vendedor/PEDIDO/LISTA";  
    }

    @GetMapping("/EDITAR/{id_pedido}")
    public String mostrarFormularioEditarPEDIDO(@PathVariable Long id_pedido, Model model) {
        PEDIDO pedido = service.obtenerPorId(id_pedido);  
        model.addAttribute("PEDIDO", pedido);
        model.addAttribute("ESTADOS", new String[] {"PENDIENTE", "ENTREGADO", "CANCELADO"});
        return "dashboard-vendedor/PEDIDO-form";  
    }

    @GetMapping("/EXPORTAR-PDF")
    public void exportarListaPEDIDOSPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PEDIDOS_" + fechaActual + ".pdf";

        response.setHeader(headerKey, headerValue);

        List<PEDIDO> PEDIDOS = service.listarTodas();

        PEDIDOExporterPDF exporterPDF = new PEDIDOExporterPDF(PEDIDOS);
        exporterPDF.export(response);
    }

    @GetMapping("/ExportarExcel")
public void exportarListaPEDIDOSExcel(HttpServletResponse response) throws DocumentException, IOException {
    System.out.println("Método exportarListaPEDIDOSExcel llamado");

    response.setContentType("application/octet-stream");

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String actualDate = dateFormatter.format(new Date());

    String header = "Content-Disposition";
    String value = "attachment; filename=PEDIDOS_" + actualDate + ".xlsx";

    response.setHeader(header, value);

    List<PEDIDO> PEDIDOS = service.listarTodas();

    PEDIDOExporterExcel exporterExcel = new PEDIDOExporterExcel(PEDIDOS);
    exporterExcel.export(response);
}

}
