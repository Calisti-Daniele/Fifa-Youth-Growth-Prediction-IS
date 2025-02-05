package controller;

import model.bean.Utente;
import model.dao.UtenteDAO;
import model.bean.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/login")
public class LoginControl extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            UtenteDAO utenteDAO = new UtenteDAO(conn);
            Optional<Utente> utente = utenteDAO.getUtenteByEmail(email);

            if (utente.isPresent() && utente.get().getPassword().equals(CrittografiaUtils.hashSHA1(password))) { // Sostituisci con hash
                HttpSession session = request.getSession();
                session.setAttribute("utente", utente.get());
                request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
            } else {
                request.setAttribute("errore", "Credenziali non valide!");
                request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
