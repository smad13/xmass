package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.xmas.greet.modelo.venta.Sale;
import com.xmas.greet.modelo.venta.Detail;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaleExporterExcelWithDetail {
    private XSSFWorkbook book;
    private XSSFSheet sheet;
    private List<Sale> sales;
    private List<Detail> details;
    private Map<Long, List<Detail>> saleDetailMap;

    public SaleExporterExcelWithDetail(List<Sale> sales, List<Detail> details) {
        this.sales = sales;
        this.details = details;
        book = new XSSFWorkbook();
        sheet = book.createSheet("Sales with Details");
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

    private void writeHeaderTable() {
        Row row = sheet.createRow(0);
    
        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    
        // Se define el encabezado: datos de la venta y de su detalle
        String[] headers = {"Sale ID", "Total", "Fecha", "Usuario", "Detail ID", "Producto", "Cantidad"};
    
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    
        // Ajusta el ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void writeTableData() {
        int rowCount = 1;
    
        // Estilo común para las celdas
        CellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    
        // Estilo para la fecha
        CellStyle dateStyle = book.createCellStyle();
        XSSFFont dateFont = book.createFont();
        dateFont.setFontHeight(12);
        dateStyle.setFont(dateFont);
        dateStyle.setBorderBottom(BorderStyle.THIN);
        dateStyle.setBorderTop(BorderStyle.THIN);
        dateStyle.setBorderLeft(BorderStyle.THIN);
        dateStyle.setBorderRight(BorderStyle.THIN);
        DataFormat dataFormat = book.createDataFormat();
        dateStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));
    
        // Para cada venta se imprimen una o varias filas según sus detalles
        for (Sale sale : sales) {
            List<Detail> saleDetails = saleDetailMap.get(sale.getId());
            if (saleDetails == null || saleDetails.isEmpty()) {
                // Si no tiene detalles, se imprime una fila con celdas vacías para la parte de detalle
                Row row = sheet.createRow(rowCount++);
                Cell cell = row.createCell(0);
                cell.setCellValue(sale.getId());
                cell.setCellStyle(style);
    
                cell = row.createCell(1);
                cell.setCellValue(sale.getTotal());
                cell.setCellStyle(style);
    
                cell = row.createCell(2);
                cell.setCellValue(sale.getDate());
                cell.setCellStyle(dateStyle);
    
                cell = row.createCell(3);
                cell.setCellValue(sale.getUsuario().toString());
                cell.setCellStyle(style);
    
                // Columnas de detalle en blanco
                for (int i = 4; i < 7; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue("");
                    cell.setCellStyle(style);
                }
            } else {
                // Si tiene detalles, para cada uno se imprime una fila con la info de la venta repetida
                for (Detail detail : saleDetails) {
                    Row row = sheet.createRow(rowCount++);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(sale.getId());
                    cell.setCellStyle(style);
    
                    cell = row.createCell(1);
                    cell.setCellValue(sale.getTotal());
                    cell.setCellStyle(style);
    
                    cell = row.createCell(2);
                    cell.setCellValue(sale.getDate());
                    cell.setCellStyle(dateStyle);
    
                    cell = row.createCell(3);
                    cell.setCellValue(sale.getUsuario().toString());
                    cell.setCellStyle(style);
    
                    cell = row.createCell(4);
                    cell.setCellValue(detail.getId());
                    cell.setCellStyle(style);
    
                    cell = row.createCell(5);
                    // Se utiliza toString() en Product; si deseas otro atributo (por ejemplo, el nombre), ajústalo según corresponda.
                    cell.setCellValue(detail.getProduct().toString());
                    cell.setCellStyle(style);
    
                    cell = row.createCell(6);
                    cell.setCellValue(detail.getAmount());
                    cell.setCellStyle(style);
                }
            }
        }
    
        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderTable();
        writeTableData();
    
        ServletOutputStream outputStream = response.getOutputStream();
        book.write(outputStream);
        book.close();
        outputStream.close();
    }
}
