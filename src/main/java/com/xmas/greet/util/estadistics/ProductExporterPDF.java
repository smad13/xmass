package com.xmas.greet.util.estadistics;

import com.xmas.greet.modelo.venta.Product;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductExporterPDF {
    private List<Product> listProducts;

    public ProductExporterPDF(List<Product> listProducts) {
        this.listProducts = listProducts;
    }

    private void writeTableHeader(PdfPTable table) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(java.awt.Color.BLACK);
        String[] headers = {"ID", "Name", "Description", "Price", "Stock", "Category", "Image"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(new java.awt.Color(144, 238, 144));
            cell.setPadding(4);
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table) throws DocumentException, IOException {
        for (Product product : listProducts) {
            table.addCell(product.getId());
            table.addCell(product.getName());
            table.addCell(product.getDescription());
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(String.valueOf(product.getStock()));
            table.addCell(product.getCategoria().getNombre());
            // Insertar imagen en la celda si existe
            if (product.getImagen() != null) {
                Image img = Image.getInstance(product.getImagen());
                img.scaleToFit(60, 60);
                PdfPCell imageCell = new PdfPCell(img, true);
                table.addCell(imageCell);
            } else {
                table.addCell("");
            }
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(java.awt.Color.BLACK);
        Paragraph title = new Paragraph("Lista de Productos", font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[]{1f, 2f, 4f, 1.5f, 1f, 2f, 2f});
        
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }
}
