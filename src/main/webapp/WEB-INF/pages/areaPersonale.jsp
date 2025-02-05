<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="model.bean.Utente" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.ZoneId" %>

<%
    HttpSession sessione = request.getSession();
    Utente utente = (Utente) sessione.getAttribute("utente");

    if (utente == null) {
        request.getRequestDispatcher("index.jsp").forward(request, response);
        return;
    }

    // Formattazione della data di iscrizione
    LocalDate dataIscrizione = utente.getDataIscrizione().atZone(ZoneId.systemDefault()).toLocalDate();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormattata = (dataIscrizione != null) ? dataIscrizione.format(formatter) : "Data non disponibile";
%>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Area Personale - Fifa Youth Growth Prediction</title>

    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>

    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body class="bg-white text-white min-h-screen flex flex-col">

<div id="app" class="flex flex-col flex-grow">
    <jsp:include page="/components/navbar.jsp" />

    <div class="w-full flex justify-center bg-lime-900 p-4">
        <h1 class="text-2xl font-bold">Area Personale</h1>
    </div>

    <div class="flex flex-grow items-center justify-center">
        <div class="bg-lime-800 p-8 rounded-lg shadow-lg w-1/3 text-center">
            <h2 class="text-3xl font-bold mb-6">Benvenuto, <%= utente.getNome() %>!</h2>

            <div class="flex flex-col space-y-4">
                <div class="flex justify-between items-center bg-gray-800 p-4 rounded-lg">
                    <span class="text-gray-400">Nome:</span>
                    <span class="font-semibold"><%= utente.getNome() %></span>
                </div>

                <div class="flex justify-between items-center bg-gray-800 p-4 rounded-lg">
                    <span class="text-gray-400">Cognome:</span>
                    <span class="font-semibold"><%= utente.getCognome() %></span>
                </div>

                <div class="flex justify-between items-center bg-gray-800 p-4 rounded-lg">
                    <span class="text-gray-400">Email:</span>
                    <span class="font-semibold"><%= utente.getEmail() %></span>
                </div>

                <div class="flex justify-between items-center bg-gray-800 p-4 rounded-lg">
                    <span class="text-gray-400">Data di iscrizione:</span>
                    <span class="font-semibold"><%= dataFormattata %></span>
                </div>
            </div>

            <div class="mt-6 flex justify-between">
                <a href="${pageContext.request.contextPath}/logout"
                   class="bg-red-600 hover:bg-red-500 text-white font-bold py-2 px-4 rounded">
                    Logout
                </a>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/vue.js"></script>
</body>
</html>
