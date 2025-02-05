package test;

import static org.mockito.Mockito.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import controller.CrittografiaUtils;
import controller.LoginControl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import model.dao.UtenteDAO;
import model.bean.DatabaseConnection;
import org.junit.jupiter.api.*;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;

class LoginControlTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private static ExtentReports extent;
    private ExtentTest test;

    @BeforeAll
    static void setupExtent() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-reports/LoginControlTestReport.html");
        spark.config().setDocumentTitle("LoginControl Test Report");
        spark.config().setReportName("Test di LoginControl");
        spark.config().setTimelineEnabled(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Daniele Calisti");
        extent.setSystemInfo("Ambiente", "Test");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);

        test = extent.createTest(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test Login Success")
    void testLoginSuccess() throws Exception {
        test.log(Status.INFO, "Inizio test login con credenziali valide");

        try (MockedStatic<DatabaseConnection> mockedDB = mockStatic(DatabaseConnection.class)) {
            mockedDB.when(DatabaseConnection::getConnection).thenReturn(connection);

            String email = "dcalisti03@gmail.com";
            String password = "Daniele_2003!";
            test.log(Status.INFO, "Dati inseriti: Email = " + email + ", Password = " + password);

            when(request.getParameter("email")).thenReturn(email);
            when(request.getParameter("password")).thenReturn(password);
            when(request.getSession()).thenReturn(session);

            // Simuliamo il database
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("nome")).thenReturn("Daniele");
            when(resultSet.getString("cognome")).thenReturn("Calisti");
            when(resultSet.getString("email")).thenReturn(email);
            when(resultSet.getString("password")).thenReturn("hashedPassword");

            // Simuliamo la crittografia
            try (MockedStatic<CrittografiaUtils> mockedCrypto = mockStatic(CrittografiaUtils.class)) {
                mockedCrypto.when(() -> CrittografiaUtils.hashSHA1(password)).thenReturn("hashedPassword");

                when(request.getRequestDispatcher("WEB-INF/pages/index.jsp")).thenReturn(dispatcher);

                LoginControl loginControl = new LoginControl();
                loginControl.doPost(request, response);

                // Verifica sessione
                verify(session).setAttribute(eq("utente"), any(Utente.class));
                test.pass("L'utente è stato salvato correttamente in sessione");

                // Verifica redirect
                verify(dispatcher).forward(request, response);
                test.pass("L'utente è stato reindirizzato alla home");
            }
        }
    }

    @Test
    @DisplayName("Test Login Fail")
    void testLoginFail() throws Exception {
        test.log(Status.INFO, "Inizio test login con credenziali errate");

        try (MockedStatic<DatabaseConnection> mockedDB = mockStatic(DatabaseConnection.class)) {
            mockedDB.when(DatabaseConnection::getConnection).thenReturn(connection);

            String email = "wrong@example.com";
            String password = "wrongpass";
            test.log(Status.INFO, "Dati inseriti: Email = " + email + ", Password = " + password);

            when(request.getParameter("email")).thenReturn(email);
            when(request.getParameter("password")).thenReturn(password);
            when(request.getSession()).thenReturn(session);

            // Simuliamo il database
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false); // Nessun utente trovato

            try (MockedStatic<CrittografiaUtils> mockedCrypto = mockStatic(CrittografiaUtils.class)) {
                mockedCrypto.when(() -> CrittografiaUtils.hashSHA1(password)).thenReturn("wrongHashedPassword");

                when(request.getRequestDispatcher("WEB-INF/pages/login.jsp")).thenReturn(dispatcher);

                LoginControl loginControl = new LoginControl();
                loginControl.doPost(request, response);

                // Verifica errore
                verify(request).setAttribute(eq("errore"), anyString());
                test.pass("Il messaggio di errore è stato impostato correttamente");

                // Verifica redirect
                verify(dispatcher).forward(request, response);
                test.pass("L'utente è stato reindirizzato alla pagina di login");
            }
        }
    }

    @AfterAll
    static void tearDown() {
        extent.flush();
    }
}
