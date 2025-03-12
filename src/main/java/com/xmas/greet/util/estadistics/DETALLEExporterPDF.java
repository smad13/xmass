package com.xmas.greet.util.estadistics;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import com.xmas.greet.modelo.DETALLE;
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

public class DETALLEExporterPDF {
    private List<DETALLE> listDETALLE;

    public DETALLEExporterPDF(List<DETALLE> listDETALLE) {
        super();
        this.listDETALLE = listDETALLE;
    }

    private void writeHeaderTable(PdfPTable table) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(new Color(144, 238, 144));
        celda.setPadding(4);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.BLACK);

        celda.setPhrase(new Phrase("ID", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("ID Pedido", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("ID Producto", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Cantidad", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Precio Unitario", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Subtotal", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Fecha Registro", fuente));
        table.addCell(celda);
    }

    private void writeDatesTable(PdfPTable table) {
        for (DETALLE detalle : listDETALLE) {
            table.addCell(String.valueOf(detalle.getId_detalle())); 
            table.addCell(String.valueOf(detalle.getId_pedido()));
            table.addCell(String.valueOf(detalle.getId_producto()));
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(String.format("%.2f", detalle.getPrecio_unitario())); 
            table.addCell(String.format("%.2f", detalle.getSubtotal())); 
            table.addCell(detalle.getFecha_registro().toString()); 
        }
    }
    

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        font.setSize(15);

        Paragraph tittle = new Paragraph("LISTA de DETALLES", font);
        tittle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(tittle);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] { 1f, 2f, 3f, 2f, 2.5f, 3f, 2f });

        table.setWidthPercentage(110);

        writeHeaderTable(table);
        writeDatesTable(table);

        document.add(table);
        document.close();

    }
}
