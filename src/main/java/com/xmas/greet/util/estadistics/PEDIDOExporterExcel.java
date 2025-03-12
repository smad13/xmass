package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xmas.greet.modelo.PEDIDO;

public class PEDIDOExporterExcel {
    private XSSFWorkbook book;
    private XSSFSheet sheet;

    private List<PEDIDO> listPEDIDO;

    public PEDIDOExporterExcel(List<PEDIDO> listPEDIDO) {
        this.listPEDIDO = listPEDIDO;
        book = new XSSFWorkbook();
        sheet = book.createSheet("PEDIDO");
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
    
        String[] headers = {"ID", "Cantidad", "Dirección", "Total", "Id Cliente", "Estado"};
    
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    
        sheet.setColumnWidth(0, 256 * 10);
        sheet.setColumnWidth(1, 256 * 12);
        sheet.setColumnWidth(2, 256 * 30);
        sheet.setColumnWidth(3, 256 * 15);
        sheet.setColumnWidth(4, 256 * 15);
        sheet.setColumnWidth(5, 256 * 15);
    }
    
    private void writetabledata() {
        int numberRows = 1;
    
        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    
        for (PEDIDO pedido : listPEDIDO) {
            Row row = sheet.createRow(numberRows++);
    
            Cell cell = row.createCell(0);
            cell.setCellValue(pedido.getId_pedido());
            cell.setCellStyle(style);
    
    
            cell = row.createCell(2);
            cell.setCellValue(pedido.getDireccion_envio());
            cell.setCellStyle(style);
    
            cell = row.createCell(3);
            cell.setCellValue(pedido.getValor_total().doubleValue());
            cell.setCellStyle(style);
    
            cell = row.createCell(4);
            cell.setCellValue(pedido.getId_cliente());
            cell.setCellStyle(style);
    
            cell = row.createCell(5);
            cell.setCellValue(pedido.getEstado());
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
