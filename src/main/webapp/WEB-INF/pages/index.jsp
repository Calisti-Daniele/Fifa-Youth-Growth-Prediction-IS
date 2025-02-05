<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Fifa Youth Growth Prediction</title>

    <script>
        const contextPath = "${pageContext.request.contextPath}";
    </script>

    <script src="https://cdn.tailwindcss.com"></script>
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

    <div class="flex flex-col items-center bg-lime-900 p-4">
        <h1 class="text-2xl font-bold text-white">Fifa Youth Growth Prediction</h1>
        <select id="search" class="w-1/2 mt-4"></select>
    </div>

    <!-- Filtro delle statistiche -->
    <div v-if="currentPlayer" class="bg-gray-800 p-4 text-center">
        <h2 class="text-lg font-semibold">Seleziona le statistiche da visualizzare</h2>
        <div class="flex justify-center space-x-2 mt-2">
            <label v-for="stat in availableStats" :key="stat.value" class="flex items-center space-x-2">
                <input type="checkbox" v-model="selectedStats" :value="stat.value" class="form-checkbox h-5 w-5 text-green-500">
                <span class="text-white">{{ stat.label }}</span>
            </label>
        </div>
    </div>

    <div v-if="currentPlayer" class="flex-grow flex items-center justify-center">
        <div class="bg-lime-900 text-white p-6 rounded-lg shadow-lg w-3/4">
            <div class="text-center">
                <img :src="currentPlayer.image ? currentPlayer.image : '${pageContext.request.contextPath}/' + default_player" class="w-40 mx-auto">
                <h2 class="text-2xl font-bold mt-2">{{ currentPlayer.name }}</h2>
                <p class="text-gray-300">{{ currentPlayer.nationality_name }}</p>
                <p class="bg-green-700 px-3 py-1 inline-block mt-2 rounded">{{ currentPlayer.positions }}</p>
            </div>

            <div class="grid grid-cols-3 gap-4 mt-4 text-center">
                <div v-for="stat in availableStats" :key="stat.value">
                    <span v-if="selectedStats.includes(stat.value)">
                        <h3 class="text-lg font-semibold">{{ stat.label }}</h3>
                        <p class="bg-green-700 px-3 py-1 rounded">{{ parseFloat(currentPlayer[stat.value]).toFixed(2) }}</p>
                    </span>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/vue.js"></script>
</body>
</html>
