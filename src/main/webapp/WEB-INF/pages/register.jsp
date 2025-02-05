<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Registrazione - Fifa Youth Growth Prediction</title>

    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>

    <script src="https://cdn.tailwindcss.com"></script>
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-gray-900 text-white">

<div id="app">
    <jsp:include page="/components/navbar.jsp" />

    <div class="w-full flex justify-center bg-lime-900 p-4">
        <h1 class="text-xl font-bold">Registrazione</h1>
    </div>

    <div class="flex justify-center mt-10">
        <div class="bg-lime-800 p-6 rounded-lg shadow-lg w-1/3">
            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-4">
                    <label class="block text-sm font-medium">Nome</label>
                    <input type="text" name="nome" required class="w-full p-2 rounded bg-gray-800 text-white border border-gray-600 focus:border-lime-500">
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-medium">Cognome</label>
                    <input type="text" name="cognome" required class="w-full p-2 rounded bg-gray-800 text-white border border-gray-600 focus:border-lime-500">
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-medium">Email</label>
                    <input type="email" name="email" required class="w-full p-2 rounded bg-gray-800 text-white border border-gray-600 focus:border-lime-500">
                </div>

                <div class="mb-4">
                    <label class="block text-sm font-medium">Password</label>
                    <input type="password" name="password" required class="w-full p-2 rounded bg-gray-800 text-white border border-gray-600 focus:border-lime-500">
                </div>

                <button type="submit" class="w-full bg-green-700 hover:bg-green-600 text-white font-bold py-2 px-4 rounded">Registrati</button>
            </form>

            <p class="text-sm text-center mt-4">
                Hai già un account? <a href="${pageContext.request.contextPath}/redirectLogin" class="text-green-400 hover:underline">Accedi</a>
            </p>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/vue.js"></script>

<%
    String errore = (String) request.getAttribute("errore");
    if (errore != null) {
%>
<script>
    Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'error',
        title: '<%= errore %>',
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        background: '#f87171',
        color: '#fff'
    });
</script>
<%
    }
%>

</body>
</html>
