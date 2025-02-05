package test;

import static org.mockito.Mockito.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import controller.RealtimeSearchControl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.DatabaseConnection;
import model.bean.FifaPlayer;
import model.dao.FifaPlayerDAO;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RealTimeSearchControlTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Connection connection;
    private FifaPlayerDAO playerDAO;
    private RealtimeSearchControl realTimeSearchControl;
    private StringWriter responseWriter;
    private ExtentTest test;
    private static ExtentReports extent;

    @BeforeAll
    static void setupExtent() {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-reports/RealTimeSearchControlTestReport.html");
        spark.config().setDocumentTitle("RealTimeSearchControl Test Report");
        spark.config().setReportName("Test di RealTimeSearchControl");
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
        connection = Mockito.mock(Connection.class);
        playerDAO = Mockito.mock(FifaPlayerDAO.class);
        realTimeSearchControl = new RealtimeSearchControl();

        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);

        reset(request, response, playerDAO);

        test = extent.createTest(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Test Search Success")
    void testSearchSuccess() throws Exception {
        String query = "ronald";

        test.log(Status.INFO, "🟢 Inizio test ricerca giocatori con nome valido")
                .info("Input → Query: " + query);

        when(request.getParameter("query")).thenReturn(query);

        List<FifaPlayer> players = Arrays.asList(
                new FifaPlayer(
                        "Ellery Ronald Balcombe", "61.11995315551758", "47.758209228515625",
                        "51.96918869018555", "58.855220794677734", "31.662494659423828",
                        "59.43425369262695", "Ellery Ronald Balcombe",
                        "https://sofifa.com/player/236663/ellery-balcombe/180002", "E. Balcombe",
                        "GK", "England", "Right"
                ),
                new FifaPlayer(
                        "Fábio Ronaldo Costa Conceição", "60.158042907714844", "69.67139434814453",
                        "46.1747932434082", "61.96835708618164", "27.15114402770996",
                        "56.74763870239258", "Fábio Ronaldo Costa Conceição",
                        "/player/269489/fabio-ronaldo-costa-conceicao/230009", "Fábio Ronaldo",
                        "LW, RW", "Portugal", "Right"
                ),
                new FifaPlayer(
                        "Ronaldo Damus", "59.385719299316406", "56.16881561279297",
                        "40.96377944946289", "57.48835372924805", "23.632997512817383",
                        "59.50724792480469", "Ronaldo Damus",
                        "/player/268282/ronaldo-damus/230009", "R. Damus",
                        "ST", "Haiti", "Right"
                ),
                new FifaPlayer(
                        "Yerson Ronaldo Chacón Ramírez", "64.3592300415039", "64.32061767578125",
                        "58.778594970703125", "66.61314392089844", "28.564334869384766",
                        "37.73439407348633", "Yerson Ronaldo Chacón Ramírez",
                        "https://sofifa.com/player/260050/yerson-chacon/220002", "Y. Chacón",
                        "RM, RW, LM", "Venezuela", "Right"
                )
        );

        try (
                MockedStatic<DatabaseConnection> mockedDB = mockStatic(DatabaseConnection.class);
                MockedConstruction<FifaPlayerDAO> mockedDAO = mockConstruction(FifaPlayerDAO.class,
                        (mock, context) -> {
                            when(mock.getPlayersByNameLike(query)).thenReturn(players);
                            when(mock.loadImage(anyString())).thenReturn("imageData");
                        }
                )
        ) {
            mockedDB.when(DatabaseConnection::getConnection).thenReturn(connection);

            PrintWriter writer = new PrintWriter(responseWriter);
            when(response.getWriter()).thenReturn(writer);

            realTimeSearchControl.doGet(request, response);

            JsonArray jsonResponse = new JsonArray();
            for (FifaPlayer player : players) {
                JsonObject jsonPlayer = new JsonObject();
                jsonPlayer.addProperty("name", player.getNome());
                jsonPlayer.addProperty("nationality_name", player.getNationalityName());
                jsonPlayer.addProperty("image", "imageData");
                jsonPlayer.addProperty("player_positions", player.getPlayerPositions());
                jsonPlayer.addProperty("overall", player.getOverall());
                jsonPlayer.addProperty("defending", player.getDefending());
                jsonPlayer.addProperty("shooting", player.getShooting());
                jsonPlayer.addProperty("passing", player.getPassing());
                jsonPlayer.addProperty("dribbling", player.getDribbling());
                jsonPlayer.addProperty("physic", player.getPhysic());
                jsonResponse.add(jsonPlayer);
            }

            String expectedOutput = jsonResponse.toString();
            String actualOutput = responseWriter.toString().trim();

            Assertions.assertEquals(expectedOutput, actualOutput);

            test.pass("✅ Output atteso: JSON con giocatori trovati")
                    .info("Risultato JSON: " + actualOutput);
        }
    }

    @Test
    @DisplayName("Test No Results Found")
    void testNoResultsFound() throws Exception {
        String query = "XYZPlayer";

        test.log(Status.INFO, "🟠 Inizio test ricerca con giocatore inesistente")
                .info("Input → Query: " + query);

        when(request.getParameter("query")).thenReturn(query);

        try (
                MockedStatic<DatabaseConnection> mockedDB = mockStatic(DatabaseConnection.class);
                MockedConstruction<FifaPlayerDAO> mockedDAO = mockConstruction(FifaPlayerDAO.class,
                        (mock, context) -> {
                            when(mock.getPlayersByNameLike(query)).thenReturn(Collections.emptyList());
                        }
                )
        ) {
            mockedDB.when(DatabaseConnection::getConnection).thenReturn(connection);

            PrintWriter writer = new PrintWriter(responseWriter);
            when(response.getWriter()).thenReturn(writer);

            realTimeSearchControl.doGet(request, response);

            String expectedOutput = "[]";
            String actualOutput = responseWriter.toString().trim();

            Assertions.assertEquals(expectedOutput, actualOutput);

            test.pass("⚠️ Output atteso: Lista vuota '[]' per nessun risultato trovato")
                    .info("Risultato ottenuto: " + actualOutput);
        }
    }

    @AfterAll
    static void tearDown() {
        extent.flush();
    }
}
