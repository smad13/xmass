package com.xmas.greet.util.estadistics;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
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
import com.xmas.greet.modelo.Usuario;
import jakarta.servlet.http.HttpServletResponse;

public class UsuarioExporterPDF {
    private List<Usuario> listUsuarios;
    private String roleName;
    
    public UsuarioExporterPDF(List<Usuario> listUsuarios, String roleName) {
        this.listUsuarios = listUsuarios;
        this.roleName = roleName;
    }
    
    private void writeHeaderTable(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(144, 238, 144));
        cell.setPadding(4);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Apellido", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Dirección", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Correo", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Tipo Documento", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Número Documento", font));
        table.addCell(cell);
    }
    
    private void writeTableData(PdfPTable table) {
        for (Usuario usuario : listUsuarios) {
            table.addCell(String.valueOf(usuario.getId()));
            table.addCell(usuario.getNombre());
            table.addCell(usuario.getApellido());
            table.addCell(usuario.getDireccion());
            table.addCell(usuario.getCorreo());
            table.addCell(usuario.getTipo_documento());
            table.addCell(String.valueOf(usuario.getNumero_documento()));
        }
    }
    
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Paragraph title = new Paragraph("Listado de Usuarios - " + roleName, fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);
        table.setWidths(new float[] {1f, 2f, 2f, 3f, 3f, 2f, 2f});
        
        writeHeaderTable(table);
        writeTableData(table);
        
        document.add(table);
        document.close();
    }
}