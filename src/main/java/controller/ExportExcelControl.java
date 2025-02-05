package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/exportExcel")
public class ExportExcelControl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String player1Name = request.getParameter("player1_name");
        String player2Name = request.getParameter("player2_name");
        String[] stats = request.getParameterValues("stats[]");

        if (player1Name == null || player2Name == null || stats == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati insufficienti per generare l'Excel.");
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Confronto Giocatori");

        // Creazione delle intestazioni
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Statistiche");
        headerRow.createCell(1).setCellValue(player1Name);
        headerRow.createCell(2).setCellValue(player2Name);

        // Aggiunta dei dati
        for (int i = 0; i < stats.length; i++) {
            String stat = stats[i];
            String player1Stat = request.getParameter("player1_" + stat);
            String player2Stat = request.getParameter("player2_" + stat);

            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(stat);
            row.createCell(1).setCellValue(Double.parseDouble(player1Stat));
            row.createCell(2).setCellValue(Double.parseDouble(player2Stat));
        }

        // Configura la risposta HTTP per il download
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=confronto_giocatori.xlsx");

        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        }

        workbook.close();
    }
}
