<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Confronto Giocatori</title>

    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>

    <!-- TailwindCSS -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- jQuery & Select2 -->
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

    <style>
        /* Rende visibile il testo nelle select */
        .select2-container--default .select2-selection--single .select2-selection__rendered {
            color: black !important;
        }
        .select2-container--default .select2-selection--single {
            background-color: white !important;
            border: 2px solid #15803d !important;
        }
        /* Cambia il colore del bordo dei risultati Select2 */
        .select2-dropdown {
            border: 2px solid #15803d !important;
        }
    </style>

</head>
<body class="bg-green-900 text-white">

<div id="app">
    <!-- Selezione giocatori -->
    <div class="flex justify-center space-x-4 my-6">
        <select id="search1" class="select2 w-1/3 text-black"></select>
        <select id="search2" class="select2 w-1/3 text-black"></select>
    </div>

    <!-- Confronto giocatori -->
    <div v-if="player1 || player2" class="w-full bg-green-800 p-6 rounded-lg shadow-lg flex justify-between">
        <!-- Giocatore 1 -->
        <div class="w-1/2 text-center">
            <img :src="player1 ? player1.image : '${pageContext.request.contextPath}/' + default_player" width="287px">
            <h2 class="text-xl font-bold mt-2">{{ player1.name }}</h2>
            <p class="text-gray-300">{{ player1.nationality_name }}</p>
            <p class="bg-green-700 px-3 py-1 inline-block mt-2 rounded">{{ player1.player_positions }}</p>

            <div class="mt-4 text-left space-y-2">
                <p class="font-semibold" :class="highlight(player1.overall, player2.overall)">Overall: {{ parseFloat(player1.overall).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player1.shooting, player2.shooting)">Shooting: {{ parseFloat(player1.shooting).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player1.passing, player2.passing)">Passing: {{ parseFloat(player1.passing).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player1.dribbling, player2.dribbling)">Dribbling: {{ parseFloat(player1.dribbling).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player1.defending, player2.defending)">Defending: {{ parseFloat(player1.defending).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player1.physic, player2.physic)">Physic: {{ parseFloat(player1.physic).toFixed(2) }}</p>
            </div>
        </div>

        <!-- Giocatore 2 -->
        <div class="w-1/2 text-center">
            <img :src="player2 ? player2.image : '${pageContext.request.contextPath}/' + default_player" width="287px">
            <h2 class="text-xl font-bold mt-2">{{ player2.name }}</h2>
            <p class="text-gray-300">{{ player2.nationality_name }}</p>
            <p class="bg-green-700 px-3 py-1 inline-block mt-2 rounded">{{ player2.player_positions }}</p>

            <div class="mt-4 text-left space-y-2">
                <p class="font-semibold" :class="highlight(player2.overall, player1.overall)">Overall: {{ parseFloat(player2.overall).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player2.shooting, player1.shooting)">Shooting: {{ parseFloat(player2.shooting).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player2.passing, player1.passing)">Passing: {{ parseFloat(player2.passing).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player2.dribbling, player1.dribbling)">Dribbling: {{ parseFloat(player2.dribbling).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player2.defending, player1.defending)">Defending: {{ parseFloat(player2.defending).toFixed(2) }}</p>
                <p class="font-semibold" :class="highlight(player2.physic, player1.physic)">Physic: {{ parseFloat(player2.physic).toFixed(2) }}</p>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/compare.js"></script>

</body>
</html>
