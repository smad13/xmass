package com.xmas.greet.controlador;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.DocumentException;
import com.xmas.greet.modelo.DETALLE;
import com.xmas.greet.modelo.MATERIAL;
import com.xmas.greet.modelo.PEDIDO;
import com.xmas.greet.modelo.Usuario;
import com.xmas.greet.modelo.venta.Detail;
import com.xmas.greet.modelo.venta.Product;
import com.xmas.greet.modelo.venta.Sale;
import com.xmas.greet.servicio.DETALLEService;
import com.xmas.greet.servicio.MATERIALService;
import com.xmas.greet.servicio.PEDIDOService;
import com.xmas.greet.servicio.UsuarioServicio;
import com.xmas.greet.servicio.venta.DetailService;
import com.xmas.greet.servicio.venta.ProductService;
import com.xmas.greet.servicio.venta.SaleService;
import com.xmas.greet.util.estadistics.DETALLEExporterExcel;
import com.xmas.greet.util.estadistics.DETALLEExporterPDF;
import com.xmas.greet.util.estadistics.MATERIALExporterExcel;
import com.xmas.greet.util.estadistics.MATERIALExporterPDF;
import com.xmas.greet.util.estadistics.PEDIDOExporterExcel;
import com.xmas.greet.util.estadistics.PEDIDOExporterPDF;
import com.xmas.greet.util.estadistics.ProductExporterExcel;
import com.xmas.greet.util.estadistics.ProductExporterPDF;
import com.xmas.greet.util.estadistics.SaleExporterExcelWithDetail;
import com.xmas.greet.util.estadistics.SaleExporterPDFWithDetail;
import com.xmas.greet.util.estadistics.UsuarioExporterExcel;
import com.xmas.greet.util.estadistics.UsuarioExporterPDF;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/exporter")
public class ExporterController {
    @Autowired
    private PEDIDOService pedidoservice;
    private DETALLEService detalleservice;
    private MATERIALService materialService;
    private SaleService saleService;
    private DetailService detailService;
    private UsuarioServicio usuarioServicio;
    private ProductService productService;

    


    public ExporterController(PEDIDOService pedidoservice, DETALLEService detalleservice,
    MATERIALService materialService, SaleService saleService, DetailService detailService,
    UsuarioServicio usuarioServicio, ProductService productService) {
        this.pedidoservice = pedidoservice;
        this.detalleservice = detalleservice;
        this.materialService = materialService;
        this.saleService = saleService;
        this.detailService = detailService;
        this.usuarioServicio = usuarioServicio;
        this.productService = productService;
    }

    //Pedidos
    @GetMapping("/EXPORTAR-PDF/PEDIDOS")
    public void exportarListaPEDIDOSPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PEDIDOS_" + fechaActual + ".pdf";

        response.setHeader(headerKey, headerValue);

        List<PEDIDO> PEDIDOS = pedidoservice.listarTodas();

        PEDIDOExporterPDF exporterPDF = new PEDIDOExporterPDF(PEDIDOS);
        exporterPDF.export(response);
    }

    @GetMapping("/ExportarExcel/PEDIDOS")
public void exportarListaPEDIDOSExcel(HttpServletResponse response) throws DocumentException, IOException {
    System.out.println("Método exportarListaPEDIDOSExcel llamado");

    response.setContentType("application/octet-stream");

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String actualDate = dateFormatter.format(new Date());

    String header = "Content-Disposition";
    String value = "attachment; filename=PEDIDOS_" + actualDate + ".xlsx";

    response.setHeader(header, value);

    List<PEDIDO> PEDIDOS = pedidoservice.listarTodas();

    PEDIDOExporterExcel exporterExcel = new PEDIDOExporterExcel(PEDIDOS);
    exporterExcel.export(response);
}


                                    //Detalles
@GetMapping("/ExportarPDF/DETALLES")
public void exportarListaDETALLESPDF(HttpServletResponse response) throws DocumentException, IOException {
    response.setContentType("application/pdf");

    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String fechaActual = dateFormatter.format(new Date());

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=DETALLES_" + fechaActual + ".pdf";
    response.setHeader(headerKey, headerValue);

    List<DETALLE> DETALLES = detalleservice.listarTodas();

    DETALLEExporterPDF exporterPDF = new DETALLEExporterPDF(DETALLES);
    exporterPDF.export(response);

    response.getOutputStream().close();
}


    @GetMapping("/ExportarExcel/DETALLES")
    public void exportarListaDETALLESExcel(HttpServletResponse response) throws DocumentException, IOException {
        System.out.println("Método exportarListaDETALLESExcel llamado");

        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String actualDate = dateFormatter.format(new Date());

        String header = "Content-Disposition";
        String value = "attachment; filename=DETALLES_" + actualDate + ".xlsx";

        response.setHeader(header, value);

        List<DETALLE> DETALLES = detalleservice.listarTodas();

        DETALLEExporterExcel exporterExcel = new DETALLEExporterExcel(DETALLES);
        exporterExcel.export(response);
    }

                                //Materiales

    @GetMapping("/EXPORTAR-PDF/MATERIALES")
    public void exportarListaMATERIALESPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=MATERIALES_" + fechaActual + ".pdf";

        response.setHeader(headerKey, headerValue);

        List<MATERIAL> MATERIALES = materialService.listarTodas();

        MATERIALExporterPDF exporterPDF = new MATERIALExporterPDF(MATERIALES);
        exporterPDF.export(response);
    }

    @GetMapping("/ExportarExcel/MATERIALES")
    public void exportarListaMATERIALESExcel(HttpServletResponse response) throws DocumentException, IOException {
        System.out.println("Método exportarListaMATERIALESExcel llamado");

        response.setContentType("application/octet-stream");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String actualDate = dateFormatter.format(new Date());

        String header = "Content-Disposition";
        String value = "attachment; filename=MATERIALES_" + actualDate + ".xlsx";

        response.setHeader(header, value);

        List<MATERIAL> MATERIALES = materialService.listarTodas();

        MATERIALExporterExcel exporterExcel = new MATERIALExporterExcel(MATERIALES);
        exporterExcel.export(response);
    }

                                    //ventas
    @GetMapping("/ExportarExcel/ventas")
        public void exportSalesToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sales.xlsx";
        response.setHeader(headerKey, headerValue);

        List<Sale> sales = saleService.getAllSales();
        List<Detail> details = detailService.findDetailAll();

        SaleExporterExcelWithDetail exporter = new SaleExporterExcelWithDetail(sales, details);
        exporter.export(response);
    }

       @GetMapping("/EXPORTAR-PDF/ventas")
    public void exportSalesToPDF(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=sales.pdf";
        response.setHeader(headerKey, headerValue);

        List<Sale> sales = saleService.getAllSales();
        List<Detail> details = detailService.findDetailAll();

        SaleExporterPDFWithDetail exporter = new SaleExporterPDFWithDetail(sales, details);
        exporter.export(response);
    }
                                //Usuario Por Rol

      @GetMapping("/ExportarExcel/usuario/{role}")
    public void exportExcelByRole(@PathVariable("role") String role, HttpServletResponse response) throws IOException {
        // Configurar el response para indicar la descarga del archivo Excel
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=usuarios_" + role.toLowerCase() + ".xlsx";
        response.setHeader(headerKey, headerValue);

        // Obtener la lista de usuarios filtrada por rol. Se espera que el servicio realice el filtrado.
        List<Usuario> usuarios = usuarioServicio.getUsuariosByRole(role);

        // Invocar la exportación en Excel
        UsuarioExporterExcel exporterExcel = new UsuarioExporterExcel(usuarios, role);
        exporterExcel.export(response);
    }

    @GetMapping("/EXPORTAR-PDF/usuario/{role}")
    public void exportPdfByRole(@PathVariable("role") String role, HttpServletResponse response) throws IOException, DocumentException {
        // Configurar el response para indicar la descarga del archivo PDF
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=usuarios_" + role.toLowerCase() + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Obtener la lista de usuarios filtrada por rol.
        List<Usuario> usuarios = usuarioServicio.getUsuariosByRole(role);

        // Invocar la exportación en PDF
        UsuarioExporterPDF exporterPDF = new UsuarioExporterPDF(usuarios, role);
        exporterPDF.export(response);
    }

                                    //Productos
    @GetMapping("/ExportarExcel/productos")
      public void exportProductsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDate = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PRODUCTOS_" + currentDate + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Product> products = productService.getAllProducts();
        ProductExporterExcel exporterExcel = new ProductExporterExcel(products);
        exporterExcel.export(response);
    }

        @GetMapping("/EXPORTAR-PDF/PRODUCTOS")
    public void exportProductsToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDate = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PRODUCTOS_" + currentDate + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Product> products = productService.getAllProducts();
        ProductExporterPDF exporterPDF = new ProductExporterPDF(products);
        exporterPDF.export(response);
    }

}
