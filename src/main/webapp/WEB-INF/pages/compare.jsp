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
        /* Cambia il colore del testo digitato nella barra di ricerca di Select2 */
        .select2-container--default .select2-search--inline .select2-search__field {
            color: black !important;  /* Rende il testo visibile */
        }

        /* Cambia il colore del testo dei risultati */
        .select2-container--default .select2-results__option {
            color: black !important;  /* Rende i risultati visibili */
        }

        /* Cambia il colore dello sfondo e del bordo della select */
        .select2-container--default .select2-selection--single {
            background-color: white !important;
            border: 2px solid #15803d !important;
            color: black !important;
        }

        /* Cambia il colore del placeholder */
        .select2-container--default .select2-selection--single .select2-selection__placeholder {
            color: black !important;
        }


    </style>

</head>
<body class="bg-white text-white h-screen flex flex-col">

<div id="app" class="flex flex-col flex-grow">
    <jsp:include page="/components/navbar.jsp" />
    <!-- Selezione giocatori -->
    <div class="flex justify-center space-x-4 my-6">
        <select id="search1" class="select2 w-1/3 text-black"></select>
        <select id="search2" class="select2 w-1/3 text-black"></select>
    </div>

    <!-- Filtro statistiche -->
    <div v-if="player1 && player2" class="bg-gray-800 p-4 text-center">
        <h2 class="text-lg font-semibold">Seleziona le statistiche da visualizzare</h2>
        <div class="flex justify-center space-x-4 mt-2">
            <label v-for="stat in availableStats" :key="stat.value" class="flex items-center space-x-2">
                <input type="checkbox" v-model="selectedStats" :value="stat.value" class="form-checkbox h-5 w-5 text-green-500">
                <span class="text-white">{{ stat.label }}</span>
            </label>
        </div>
    </div>

    <div v-if="player1 && player2 && selectedStats.length > 0" class="text-center mt-4">
        <button @click="exportToExcel" class="bg-green-600 hover:bg-green-500 text-white font-bold py-2 px-4 rounded">
            Esporta in Excel
        </button>
    </div>

    <!-- Confronto giocatori -->
    <div v-if="player1 && player2" class="flex-grow flex items-center justify-center">
        <div class="w-4/5 bg-green-800 p-6 rounded-lg shadow-lg flex justify-between">
            <!-- Giocatore 1 -->
            <div class="w-1/2 text-center">
                <img :src="player1 ? player1.image : '${pageContext.request.contextPath}/' + default_player" class="w-40 mx-auto">
                <h2 class="text-xl font-bold mt-2">{{ player1.name }}</h2>
                <p class="text-gray-300">{{ player1.nationality_name }}</p>
                <p class="bg-green-700 px-3 py-1 inline-block mt-2 rounded">{{ player1.player_positions }}</p>

                <div class="mt-4 text-left space-y-2">
                    <p v-for="stat in availableStats" :key="stat.value" class="font-semibold" :class="highlight(player1[stat.value], player2[stat.value])">
                        <span v-if="selectedStats.includes(stat.value)">
                            {{ stat.label }}: {{ parseFloat(player1[stat.value]).toFixed(2) }}
                        </span>
                    </p>
                </div>
            </div>

            <!-- Giocatore 2 -->
            <div class="w-1/2 text-center">
                <img :src="player2 ? player2.image : '${pageContext.request.contextPath}/' + default_player" class="w-40 mx-auto">
                <h2 class="text-xl font-bold mt-2">{{ player2.name }}</h2>
                <p class="text-gray-300">{{ player2.nationality_name }}</p>
                <p class="bg-green-700 px-3 py-1 inline-block mt-2 rounded">{{ player2.player_positions }}</p>

                <div class="mt-4 text-left space-y-2">
                    <p v-for="stat in availableStats" :key="stat.value" class="font-semibold" :class="highlight(player2[stat.value], player1[stat.value])">
                        <span v-if="selectedStats.includes(stat.value)">
                            {{ stat.label }}: {{ parseFloat(player2[stat.value]).toFixed(2) }}
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </div>

</div>

<script src="${pageContext.request.contextPath}/js/compare.js"></script>

</body>
</html>
