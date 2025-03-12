package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xmas.greet.modelo.MATERIAL;

public class MATERIALExporterExcel {
    private XSSFWorkbook book;
    private XSSFSheet sheet;

    private List<MATERIAL> listMATERIAL;

    public MATERIALExporterExcel(List<MATERIAL> listMATERIAL) {
        this.listMATERIAL = listMATERIAL;
        book = new XSSFWorkbook();
        sheet = book.createSheet("MATERIAL");
    }

    private void writeHeaderTable() {
        Row row = sheet.createRow(0);

        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setBold(true);
        font.setFontHeight(14); // Tamaño de fuente ajustado
        font.setColor(IndexedColors.BLACK.getIndex()); // Letra negra
        style.setFont(font);

        // Verde medio (más fuerte que pastel pero suave)
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        String[] headers = { "ID", "Nombre", "Descripción", "Metros", "Precio", "Categoria" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

        // Ajustar ancho de columnas
        sheet.setColumnWidth(0, 256 * 10); // ID
        sheet.setColumnWidth(1, 256 * 20); // Nombre
        sheet.setColumnWidth(2, 256 * 40); // Descripción
        sheet.setColumnWidth(3, 256 * 12); // Metros
        sheet.setColumnWidth(4, 256 * 15); // Precio
        sheet.setColumnWidth(5, 256 * 20); // Categoría
    }

    private void writetabledata() {
        int numberRows = 1;

        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setFontHeight(12); // Tamaño de letra ajustado
        font.setColor(IndexedColors.BLACK.getIndex()); // Letra negra
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.LEFT);

        for (MATERIAL material : listMATERIAL) {
            Row row = sheet.createRow(numberRows++);

            Cell cell = row.createCell(0);
            cell.setCellValue(material.getID_materiales());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(material.getNombre_materiales());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(material.getDescripcion_materiales());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(material.getCantidad_materiales().doubleValue());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(material.getPrecio_materiales());
            cell.setCellStyle(style);

            cell = row.createCell(5);
            cell.setCellValue(material.getCategoria_materiales());
            cell.setCellStyle(style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderTable();
        writetabledata();

        ServletOutputStream outputStream = response.getOutputStream();
        book.write(outputStream);

        book.close();
        outputStream.close();
    }
}
