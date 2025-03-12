package com.xmas.greet.util.estadistics;

import java.io.IOException;
import java.util.List;
import com.xmas.greet.modelo.Usuario;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UsuarioExporterExcel {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Usuario> listUsuarios;
    private String roleName;
    
    public UsuarioExporterExcel(List<Usuario> listUsuarios, String roleName) {
        this.listUsuarios = listUsuarios;
        this.roleName = roleName;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Usuarios " + roleName);
    }
    
    private void writeHeaderTable() {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // Encabezados para la exportación
        String[] headers = {"ID", "Nombre", "Apellido", "Dirección", "Correo", "Tipo Documento", "Número Documento"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
        
        // Autoajuste del ancho de las columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    private void writeTableData() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(12);
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        
        for (Usuario usuario : listUsuarios) {
            Row row = sheet.createRow(rowCount++);
            
            Cell cell = row.createCell(0);
            cell.setCellValue(usuario.getId());
            cell.setCellStyle(style);
            
            cell = row.createCell(1);
            cell.setCellValue(usuario.getNombre());
            cell.setCellStyle(style);
            
            cell = row.createCell(2);
            cell.setCellValue(usuario.getApellido());
            cell.setCellStyle(style);
            
            cell = row.createCell(3);
            cell.setCellValue(usuario.getDireccion());
            cell.setCellStyle(style);
            
            cell = row.createCell(4);
            cell.setCellValue(usuario.getCorreo());
            cell.setCellStyle(style);
            
            cell = row.createCell(5);
            cell.setCellValue(usuario.getTipo_documento());
            cell.setCellStyle(style);
            
            cell = row.createCell(6);
            cell.setCellValue(usuario.getNumero_documento());
            cell.setCellStyle(style);
        }
    }
    
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderTable();
        writeTableData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
