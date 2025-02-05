package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.bean.DatabaseConnection;
import model.bean.FifaPlayer;
import model.dao.FifaPlayerDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

@WebServlet("/realtimesearch")
public class RealtimeSearchControl extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Imposta il content type della risposta come JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Ottieni il parametro di ricerca dalla richiesta
        String query = req.getParameter("query");

        // Se il parametro è assente o troppo corto, restituisci una lista vuota
        if (query == null || query.trim().length() < 5) {
            try (PrintWriter out = resp.getWriter()) {
                out.write("[]");
            }
            return;
        }

        Connection connection = null;
        PrintWriter out = resp.getWriter();

        try {
            // Recupera la connessione
            connection = DatabaseConnection.getConnection();

            // Istanzia il DAO e cerca i giocatori
            FifaPlayerDAO playerDAO = new FifaPlayerDAO(connection);
            List<FifaPlayer> players = playerDAO.getPlayersByNameLike(query.trim());

            // Log per debugging

            // Creazione della risposta JSON
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();

            for (FifaPlayer player : players) {
                System.out.println(player);
                JsonObject jsonPlayer = new JsonObject();
                jsonPlayer.addProperty("name", player.getNome());
                jsonPlayer.addProperty("nationality_name", player.getNationalityName());
                jsonPlayer.addProperty("image", playerDAO.loadImage(player.getPlayerUrl())); // Metodo per caricare immagine
                jsonPlayer.addProperty("player_positions", player.getPlayerPositions());
                jsonPlayer.addProperty("overall", player.getOverall());
                jsonPlayer.addProperty("defending", player.getDefending());
                jsonPlayer.addProperty("shooting", player.getShooting());
                jsonPlayer.addProperty("passing", player.getPassing());
                jsonPlayer.addProperty("dribbling", player.getDribbling());
                jsonPlayer.addProperty("physic", player.getPhysic());

                jsonArray.add(jsonPlayer);
            }

            // Scrive la risposta JSON
            out.print(gson.toJson(jsonArray));

        } catch (Exception e) {
            // Log dell'errore
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // Risposta di errore in JSON
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("error", "Errore del server: " + e.getMessage());
            out.print(errorJson.toString());

        }
    }
}
