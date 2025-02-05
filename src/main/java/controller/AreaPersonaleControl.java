package controller;
import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "AreaPersonaleControl", value = "/areaPersonale")
public class AreaPersonaleControl extends HttpServlet {
    public AreaPersonaleControl() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("WEB-INF/pages/areaPersonale.jsp").forward(request, response);

    }

}

