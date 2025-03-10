package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;

import com.xmas.greet.modelo.DETALLE;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DETALLEExporterExcel {
    private XSSFWorkbook book;
    private XSSFSheet sheet;

    private List<DETALLE> listDETALLE;

    public DETALLEExporterExcel(List<DETALLE> listDETALLE) {
        this.listDETALLE = listDETALLE;
        book = new XSSFWorkbook();
        sheet = book.createSheet("DETALLE");
    }

    private void writeHeaderTable() {
        Row row = sheet.createRow(0);
    
        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    
        String[] headers = {"ID", "ID Pedido", "ID Producto", "Cantidad", "Precio Unitario", "Subtotal", "Fecha Registro"};
    
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    
        // Ajustar el ancho de las columnas automáticamente
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void writetabledata() {
        int numberRows = 1;
    
        // Estilo para las celdas
        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    
        // Estilo para la fecha
        CellStyle dateStyle = book.createCellStyle();
        XSSFFont dateFont = book.createFont();
        dateFont.setFontHeight(12);
        dateStyle.setFont(dateFont);
        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderTop(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);
        dateStyle.setBorderLeft(BorderStyle.THIN);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dateStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));  // Formato de fecha
    
        for (DETALLE detalle : listDETALLE) {
            Row row = sheet.createRow(numberRows++);
    
            // Rellenar las celdas
            Cell cell = row.createCell(0);
            cell.setCellValue(detalle.getId_detalle());
            cell.setCellStyle(style);
    
            cell = row.createCell(1);
            cell.setCellValue(detalle.getId_pedido());
            cell.setCellStyle(style);
    
            cell = row.createCell(2);
            cell.setCellValue(detalle.getId_producto());
            cell.setCellStyle(style);
    
            cell = row.createCell(3);
            cell.setCellValue(detalle.getCantidad());
            cell.setCellStyle(style);
    
            cell = row.createCell(4);
            cell.setCellValue(detalle.getPrecio_unitario().doubleValue());
            cell.setCellStyle(style);
    
            cell = row.createCell(5);
            cell.setCellValue(detalle.getSubtotal().doubleValue());
            cell.setCellStyle(style);
    
            // Fecha con formato
            cell = row.createCell(6);
            cell.setCellValue(detalle.getFecha_registro());
            cell.setCellStyle(dateStyle);
        }
    
        // Ajustar el ancho de la columna de la fecha (columna 6) a 142 píxeles (~5000 unidades)
        sheet.setColumnWidth(6, 6000); // Ajuste realizado aquí
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
