package test;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.*;
import controller.ExportExcelControl;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExportExcelControlTest {

    private static ExtentReports extent;
    private ExtentTest test;

    private ExportExcelControl servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private File tempFile;
    private ByteArrayOutputStream outputStream;

    @BeforeAll
    void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-reports/ExportExcelControlTestReport.html");
        spark.config().setDocumentTitle("ExportExcelControl Test Report");
        spark.config().setReportName("Test di ExportExcelControl");
        spark.config().setTimelineEnabled(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Daniele Calisti");
        extent.setSystemInfo("Ambiente", "Test");
    }

    @AfterAll
    void tearDownReport() {
        extent.flush();
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws IOException {
        servlet = new ExportExcelControl();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        tempFile = File.createTempFile("test_export", ".xlsx");
        outputStream = new ByteArrayOutputStream();

        test = extent.createTest(testInfo.getDisplayName());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    @DisplayName("✅ Test Export Excel Successo")
    void testExportExcelSuccess() throws ServletException, IOException {
        test.log(Status.INFO, "🟢 Inizio test di successo per l'export di Excel.");

        // Mock dei parametri HTTP
        when(request.getParameter("player1_name")).thenReturn("John");
        when(request.getParameter("player2_name")).thenReturn("Doe");
        when(request.getParameterValues("stats[]")).thenReturn(new String[]{"Goals", "Assists"});

        when(request.getParameter("player1_Goals")).thenReturn("10");
        when(request.getParameter("player2_Goals")).thenReturn("8");
        when(request.getParameter("player1_Assists")).thenReturn("5");
        when(request.getParameter("player2_Assists")).thenReturn("7");

        test.info("📥 Input ricevuto: player1_name=John, player2_name=Doe, stats=[Goals, Assists]");

        // Mock OutputStream della response
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        });

        // Esegui la servlet
        servlet.doPost(request, response);
        test.pass("🟢 Servlet eseguita con successo.");

        // Scrivi il contenuto del ByteArrayOutputStream in un file
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            outputStream.writeTo(fos);
        }

        // Leggi e verifica il file Excel generato
        try (InputStream is = new FileInputStream(tempFile);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet, "Il foglio di lavoro non deve essere nullo");
            test.pass("✅ Foglio di lavoro creato correttamente.");

            // Verifica intestazione
            Row headerRow = sheet.getRow(0);
            assertEquals("Statistiche", headerRow.getCell(0).getStringCellValue());
            assertEquals("John", headerRow.getCell(1).getStringCellValue());
            assertEquals("Doe", headerRow.getCell(2).getStringCellValue());

            test.pass("✅ Intestazioni corrette: [Statistiche, John, Doe]");

            // Verifica prima riga di dati (Goals)
            Row row1 = sheet.getRow(1);
            assertEquals("Goals", row1.getCell(0).getStringCellValue());
            assertEquals(10.0, row1.getCell(1).getNumericCellValue());
            assertEquals(8.0, row1.getCell(2).getNumericCellValue());

            test.pass("✅ Riga Goals verificata: [Goals, 10, 8]");

            // Verifica seconda riga di dati (Assists)
            Row row2 = sheet.getRow(2);
            assertEquals("Assists", row2.getCell(0).getStringCellValue());
            assertEquals(5.0, row2.getCell(1).getNumericCellValue());
            assertEquals(7.0, row2.getCell(2).getNumericCellValue());

            test.pass("✅ Riga Assists verificata: [Assists, 5, 7]");

        } catch (Exception e) {
            test.fail("❌ Errore nella verifica dell'Excel: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("❌ Test Export Excel Fallimento - Parametri Mancanti")
    void testExportExcelFailMissingParameters() throws ServletException, IOException {
        test.log(Status.INFO, "🔴 Inizio test di fallimento per parametri mancanti.");

        // Mock richiesta senza parametri essenziali
        when(request.getParameter("player1_name")).thenReturn(null);
        when(request.getParameter("player2_name")).thenReturn("Doe");
        when(request.getParameterValues("stats[]")).thenReturn(new String[]{"Goals"});

        test.info("📥 Input ricevuto: player1_name=null, player2_name=Doe, stats=[Goals]");

        // Mock OutputStream della response
        when(response.getOutputStream()).thenReturn(new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }
        });

        // Esegui la servlet
        servlet.doPost(request, response);
        test.pass("⚠️ Servlet eseguita con parametri mancanti.");

        // Verifica che la servlet restituisca errore HTTP 400
        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Dati insufficienti per generare l'Excel.");
        test.pass("🔴 Errore HTTP 400 restituito correttamente.");
    }
}
