package controller;

import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import model.dao.UtenteDAO;
import model.bean.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.time.Instant;

@WebServlet("/register")
public class RegisterControl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            UtenteDAO utenteDAO = new UtenteDAO(conn);
            Utente nuovoUtente = new Utente(0, nome, cognome, email, CrittografiaUtils.hashSHA1(password), Instant.now());

            if (utenteDAO.registerUtente(nuovoUtente)) {
                HttpSession session = request.getSession();
                session.setAttribute("utente", nuovoUtente);
                request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
            } else {
                request.setAttribute("errore", "Registrazione fallita!");
                request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
