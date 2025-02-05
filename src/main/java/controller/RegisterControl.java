package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpSession;
import model.bean.Utente;
import model.dao.UtenteDAO;
import model.bean.DatabaseConnection;
import controller.CrittografiaUtils;

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        if (nome == null || cognome == null || email == null || password == null || confirmPassword == null ||
                nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            request.setAttribute("errore", "Tutti i campi sono obbligatori!");
            request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("errore", "Formato email non valido!");
            request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
        } else if (!password.equals(confirmPassword)) {
            request.setAttribute("errore", "Le password non coincidono!");
            request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
        } else if (password.length() < 8) {
            request.setAttribute("errore", "La password deve contenere almeno 8 caratteri!");
            request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
        } else {

            try {

                conn = DatabaseConnection.getConnection();
                UtenteDAO utenteDAO = new UtenteDAO(conn);

                if (utenteDAO.getUtenteByEmail(email).isPresent()) {
                    request.setAttribute("errore", "Email già in uso!");
                    request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
                    return;
                }

                String hashedPassword = CrittografiaUtils.hashSHA1(password);
                Utente nuovoUtente = new Utente(1,nome, cognome, email, hashedPassword, Instant.now());

                if(utenteDAO.registerUtente(nuovoUtente)){
                    HttpSession session = request.getSession();
                    session.setAttribute("utente", nuovoUtente);
                    request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
                }else{
                    request.setAttribute("errore", "Errore durante la registrazione");
                    request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errore", "Errore nel server, riprova più tardi.");
                request.getRequestDispatcher("WEB-INF/pages/register.jsp").forward(request, response);
            }

        }

    }
}
