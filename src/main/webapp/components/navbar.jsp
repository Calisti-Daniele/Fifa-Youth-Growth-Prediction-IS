<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.bean.Utente" %>
<%
    Utente utente = (Utente) session.getAttribute("utente");
%>

<nav class="bg-lime-950">
    <div class="mx-auto max-w-7xl px-2 sm:px-6 lg:px-8">
        <div class="relative flex h-16 items-center justify-between">
            <div class="flex flex-1 items-center justify-center sm:items-stretch sm:justify-start">
                <div class="flex flex-shrink-0 items-center">
                    <img class="h-8 w-auto" src="${pageContext.request.contextPath}/images/logo.png" alt="Your Company">
                </div>
                <div class="hidden sm:ml-6 sm:block">
                    <div class="flex space-x-4">
                        <a href="homepagecontrol" class="rounded-md bg-lime-700 px-3 py-2 text-sm font-medium text-white">Dashboard</a>

                        <% if (utente != null) { %>
                            <a href="comparecontrol" class="rounded-md bg-lime-700 px-3 py-2 text-sm font-medium text-white">Comparazione giocatori</a>
                        <% } %>

                    </div>
                </div>
            </div>

            <div class="absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0">
                <% if (utente == null) { %>
                    <a href="${pageContext.request.contextPath}/redirectLogin" class="bg-green-700 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">Login</a>
                <% } else { %>
                    <a href="areaPersonale" class="rounded-md bg-lime-700 px-3 py-2 text-sm font-medium text-white">Area Personale</a>
                <% } %>
            </div>
        </div>
    </div>
</nav>
