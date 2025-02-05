package test;

import static org.mockito.Mockito.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import controller.RegisterControl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import model.dao.UtenteDAO;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterControlTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private UtenteDAO utenteDAO;
    private RegisterControl registerControl;
    private StringWriter responseWriter;
    private ExtentTest test;
    private static ExtentReports extent;

    @BeforeAll
    static void setupExtent() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-reports/RegisterControlTestReport.html");
        spark.config().setDocumentTitle("RegisterControl Test Report");
        spark.config().setReportName("Test di RegisterControl");
        spark.config().setTimelineEnabled(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Daniele Calisti");
        extent.setSystemInfo("Ambiente", "Test");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) throws Exception {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        utenteDAO = Mockito.mock(UtenteDAO.class);
        registerControl = new RegisterControl();

        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);

        reset(request, response, session, dispatcher, utenteDAO);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        test = extent.createTest(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test Register Success")
    void testRegisterSuccess() throws Exception {
        String email = "daniele@gmail.com";
        String password = "SecurePass123";

        test.log(Status.INFO, "🟢 Inizio test registrazione con dati validi")
                .info("Input → Email: " + email + ", Password: " + password);

        when(request.getParameter("nome")).thenReturn("Daniele");
        when(request.getParameter("cognome")).thenReturn("Calisti");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirm_password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);
        when(utenteDAO.getUtenteByEmail(email)).thenReturn(Optional.empty());

        when(request.getRequestDispatcher("WEB-INF/pages/index.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(session).setAttribute(eq("utente"), any(Utente.class));
        verify(dispatcher).forward(request, response);

        test.pass("✅ Output atteso: Registrazione completata con successo")
                .info("L'utente è stato creato e reindirizzato alla home");
    }

    @Test
    @DisplayName("Test Register Email Already Used")
    void testRegisterEmailAlreadyUsed() throws Exception {
        String email = "dcalisti03@gmail.com";
        String password = "SecurePass123";

        test.log(Status.INFO, "🟠 Inizio test registrazione con email già in uso")
                .info("Input → Email: " + email);

        when(request.getParameter("nome")).thenReturn("Daniele");
        when(request.getParameter("cognome")).thenReturn("Calisti");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirm_password")).thenReturn(password);
        when(utenteDAO.getUtenteByEmail(email)).thenReturn(Optional.of(new Utente()));

        when(request.getRequestDispatcher("WEB-INF/pages/register.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(request).setAttribute("errore", "Email già in uso!");
        verify(dispatcher).forward(request, response);

        test.pass("⚠️ Output atteso: Messaggio di errore 'Email già in uso!'")
                .info("L'utente non è stato registrato e la pagina di registrazione è stata ricaricata");
    }

    @Test
    @DisplayName("Test Register Password Mismatch")
    void testRegisterPasswordMismatch() throws Exception {
        String email = "dcalisti031@gmail.com";
        String password = "SecurePass123";
        String confirmPassword = "WrongPass123";

        test.log(Status.INFO, "🟠 Inizio test registrazione con password non coincidenti")
                .info("Input → Email: " + email + ", Password: " + password + ", Confirm Password: " + confirmPassword);

        when(request.getParameter("nome")).thenReturn("Daniele");
        when(request.getParameter("cognome")).thenReturn("Calisti");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirm_password")).thenReturn(confirmPassword);

        when(request.getRequestDispatcher("WEB-INF/pages/register.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(request).setAttribute("errore", "Le password non coincidono!");
        verify(dispatcher).forward(request, response);

        test.pass("⚠️ Output atteso: Messaggio di errore 'Le password non coincidono!'");
    }

    @Test
    @DisplayName("Test Register Invalid Email Format")
    void testRegisterInvalidEmailFormat() throws Exception {
        String email = "invalid-email";
        String password = "SecurePass123";

        test.log(Status.INFO, "🟠 Inizio test registrazione con email non valida")
                .info("Input → Email: " + email);

        when(request.getParameter("nome")).thenReturn("Daniele");
        when(request.getParameter("cognome")).thenReturn("Calisti");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirm_password")).thenReturn(password);

        when(request.getRequestDispatcher("WEB-INF/pages/register.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(request).setAttribute("errore", "Formato email non valido!");
        verify(dispatcher).forward(request, response);

        test.pass("⚠️ Output atteso: Messaggio di errore 'Formato email non valido!'");
    }

    @Test
    @DisplayName("Test Register Password Too Short")
    void testRegisterPasswordTooShort() throws Exception {
        String email = "dcalisti032@gmail.com";
        String password = "short";

        test.log(Status.INFO, "🟠 Inizio test registrazione con password troppo corta")
                .info("Input → Email: " + email + ", Password: " + password);

        when(request.getParameter("nome")).thenReturn("Daniele");
        when(request.getParameter("cognome")).thenReturn("Calisti");
        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirm_password")).thenReturn(password);

        when(request.getRequestDispatcher("WEB-INF/pages/register.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(request).setAttribute("errore", "La password deve contenere almeno 8 caratteri!");
        verify(dispatcher).forward(request, response);

        test.pass("⚠️ Output atteso: Messaggio di errore 'La password deve contenere almeno 8 caratteri!'");
    }

    @Test
    @DisplayName("Test Register Empty Fields")
    void testRegisterEmptyFields() throws Exception {
        test.log(Status.INFO, "🟠 Inizio test registrazione con campi vuoti");

        when(request.getParameter("nome")).thenReturn("");
        when(request.getParameter("cognome")).thenReturn("");
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("confirm_password")).thenReturn("");

        when(request.getRequestDispatcher("WEB-INF/pages/register.jsp")).thenReturn(dispatcher);

        registerControl.doPost(request, response);

        verify(request).setAttribute("errore", "Tutti i campi sono obbligatori!");
        verify(dispatcher).forward(request, response);

        test.pass("⚠️ Output atteso: Messaggio di errore 'Tutti i campi sono obbligatori!'");
    }

    @AfterAll
    static void tearDown() {
        extent.flush();
    }
}
