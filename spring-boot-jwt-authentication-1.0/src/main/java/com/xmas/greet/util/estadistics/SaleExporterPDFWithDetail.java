package com.xmas.greet.util.estadistics;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.xmas.greet.modelo.venta.Sale;
import com.xmas.greet.modelo.venta.Detail;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

public class SaleExporterPDFWithDetail {
    private List<Sale> sales;
    private List<Detail> details;
    private Map<Long, List<Detail>> saleDetailMap;
    
    public SaleExporterPDFWithDetail(List<Sale> sales, List<Detail> details) {
        this.sales = sales;
        this.details = details;
        groupDetails();
    }
    
    // Agrupa los detalles según el ID de la venta
    private void groupDetails() {
        saleDetailMap = new HashMap<>();
        for (Detail detail : details) {
            Long saleId = detail.getSale().getId();
            saleDetailMap.computeIfAbsent(saleId, k -> new java.util.ArrayList<>()).add(detail);
        }
    }
    
    private void writeHeaderTable(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(144, 238, 144));
        cell.setPadding(4);
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        
        String[] headers = {"Sale ID", "Total", "Fecha", "Usuario", "Detail ID", "Producto", "Cantidad"};
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }
    
    private void writeDataTable(PdfPTable table) {
        for (Sale sale : sales) {
            List<Detail> saleDetails = saleDetailMap.get(sale.getId());
            if (saleDetails == null || saleDetails.isEmpty()) {
                // Si no hay detalles, se imprime la fila con columnas vacías en la parte de detalle
                table.addCell(String.valueOf(sale.getId()));
                table.addCell(String.format("%.2f", sale.getTotal()));
                table.addCell(sale.getDate().toString());
                table.addCell(sale.getUsuario().toString());
                table.addCell("");
                table.addCell("");
                table.addCell("");
            } else {
                for (Detail detail : saleDetails) {
                    table.addCell(String.valueOf(sale.getId()));
                    table.addCell(String.format("%.2f", sale.getTotal()));
                    table.addCell(sale.getDate().toString());
                    table.addCell(sale.getUsuario().toString());
                    table.addCell(String.valueOf(detail.getId()));
                    table.addCell(detail.getProduct().toString());
                    table.addCell(String.valueOf(detail.getAmount()));
                }
            }
        }
    }
    
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(15);
        font.setColor(Color.BLACK);
        
        Paragraph title = new Paragraph("Lista de Ventas con Detalles", font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] {1f, 2f, 3f, 3f, 1f, 3f, 1f});
        
        writeHeaderTable(table);
        writeDataTable(table);
        
        document.add(table);
        document.close();
    }
}
