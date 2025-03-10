package com.xmas.greet.util.estadistics;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;


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
import com.xmas.greet.modelo.MATERIAL;

public class MATERIALExporterPDF {
    private List<MATERIAL> listMATERIAL;

    public MATERIALExporterPDF(List<MATERIAL> listMATERIAL) {
        super();
        this.listMATERIAL = listMATERIAL;
    }

    private void writeHeaderTable(PdfPTable table) {
        PdfPCell celda = new PdfPCell();

        celda.setBackgroundColor(new Color(144, 238, 144));
        celda.setPadding(4);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.BLACK);

        celda.setPhrase(new Phrase("ID", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Nombre", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Descripción", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Metros", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Precio", fuente));
        table.addCell(celda);

        celda.setPhrase(new Phrase("Categoria", fuente));
        table.addCell(celda);
    }

    private void writeDatesTable(PdfPTable table) {
        for (MATERIAL MATERIAL : listMATERIAL) {
            table.addCell(String.valueOf(MATERIAL.getID_materiales()));
            table.addCell(MATERIAL.getNombre_materiales());
            table.addCell(MATERIAL.getDescripcion_materiales());
            table.addCell(String.valueOf(MATERIAL.getCantidad_materiales()));
            table.addCell(String.valueOf(MATERIAL.getPrecio_materiales()));
            table.addCell(MATERIAL.getCategoria_materiales());
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        font.setSize(15);

        Paragraph tittle = new Paragraph("LISTA de MATERIALES", font);
        tittle.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(tittle);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] { 1f, 2.5f, 5f, 1.5f, 2.2f, 3f });

        table.setWidthPercentage(110);

        writeHeaderTable(table);
        writeDatesTable(table);

        document.add(table);
        document.close();

    }
}
