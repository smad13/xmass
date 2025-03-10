package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;
import com.xmas.greet.modelo.venta.Product;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProductExporterExcel {
    private XSSFWorkbook workbook;
    private Sheet sheet;
    private List<Product> listProducts;

    public ProductExporterExcel(List<Product> listProducts) {
        this.listProducts = listProducts;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Products");
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        
        String[] headers = {"ID", "Name", "Description", "Price", "Stock", "Category", "Image"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
    }

    private void writeDataRows() {
        int rowCount = 1;
        // Se crea un único drawing para la hoja
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();

        for (Product product : listProducts) {
            Row row = sheet.createRow(rowCount);
            // Ajustar la altura de la fila para mostrar la imagen
            row.setHeightInPoints(60);

            Cell cell = row.createCell(0);
            cell.setCellValue(product.getId());

            cell = row.createCell(1);
            cell.setCellValue(product.getName());

            cell = row.createCell(2);
            cell.setCellValue(product.getDescription());

            cell = row.createCell(3);
            cell.setCellValue(product.getPrice());

            cell = row.createCell(4);
            cell.setCellValue(product.getStock());

            cell = row.createCell(5);
            cell.setCellValue(product.getCategoria().getNombre());

            // Insertar imagen si está presente
            if (product.getImagen() != null) {
                int pictureIdx = workbook.addPicture(product.getImagen(), Workbook.PICTURE_TYPE_JPEG);
                ClientAnchor anchor = helper.createClientAnchor();
                // Posicionar la imagen en la columna 6 (índice 6)
                anchor.setCol1(6);
                anchor.setRow1(rowCount);
                // Opcional: definir el tamaño de la imagen, por ejemplo, que ocupe una columna completa
                anchor.setCol2(7);
                anchor.setRow2(rowCount + 1);
                drawing.createPicture(anchor, pictureIdx);
            }
            rowCount++;
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRows();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
