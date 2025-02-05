<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Fifa Youth Growth Prediction</title>

    <script src="https://cdn.tailwindcss.com"></script>

    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
</head>
<body>

<div id="app">
    <jsp:include page="/components/navbar.jsp" />

    <div class="w-full flex justify-between items-center bg-lime-900 p-4">
        <!-- Contenuto a sinistra -->
        <div class="flex-1 text-left">
            <p class="text-gray-700">
                <jsp:include page="/components/search.jsp" />
            </p>
        </div>

        <!-- Contenuto al centro -->
        <div class="flex-1 text-center">
            <p class="text-gray-700"></p>
        </div>


    </div>

    <div class="w-full flex justify-between items-center bg-lime-800 p-4 h-72 ">
        <!-- Contenuto a sinistra -->
        <div class="flex-1 text-left">
            <p class="text-gray-700">
                <img :src="currentPlayer ? currentPlayer.image : '${pageContext.request.contextPath}/' + default_player" width="287px">
            </p>
        </div>

        <div v-if="currentPlayer == null">

        </div>
        <!-- Contenuto al centro -->
        <div class="player-card bg-lime-900 text-white p-4 rounded-lg shadow-lg w-2/3 h-auto mx-auto" v-else>
            <div>
                <h1 class="text-2xl font-bold">{{currentPlayer.name}}</h1>
                <div class="flex items-center space-x-2">
                    <!-- Bandiera -->
                    <img src="https://upload.wikimedia.org/wikipedia/commons/5/5c/Flag_of_Portugal.svg" alt="Portugal Flag" class="w-6 h-6">
                    <!-- Posizioni -->


                    <span v-if="!currentPlayer.positions.includes(',')" class="bg-green-700 text-xs font-semibold px-2 py-1 rounded">{{currentPlayer.positions}}</span>
                    <span v-else v-for=" (position, index) in currentPlayer.positions.split(',')" :key="position.trim()"  :class="'bg-'+positions_color[index]+'-700 text-xs font-semibold px-2 py-1 rounded'">
                                    {{ position.trim() }}
                            </span>

                </div>
            </div>

            <!-- Statistiche giocatore -->
            <div class="flex justify-between mt-4">
                <div class="stat">
                    <div :class="currentPlayer.overall < 60 ? 'text-red-500' : currentPlayer.overall >= 60 && currentPlayer.overall < 70 ? 'text-orange-500' : 'text-red-500'" class="text-lg font-bold">{{Math.round(currentPlayer.overall)}}</div>
                    <div class="text-gray-400 text-xs">Overall rating</div>
                </div>
            </div>
        </div>


    </div>

    <div class="border-t border-gray-300 mx-6"></div>

    <div v-if="currentPlayer == null">

    </div>
    <div v-else>
        <div class="flex player-attributes bg-lime-900 text-white p-6 rounded-lg shadow-lg w-auto mt-0">
            <div class="flex-1 grid grid-cols-5 gap-6">
                <!-- Attacking -->
                <div>
                    <h3 class="text-lg font-bold mb-2">Shooting</h3> <span class="bg-green-700 px-2 py-1 rounded"> {{parseFloat(currentPlayer.shooting).toFixed(2)}} </span>
                </div>

                <!-- Skill -->
                <div>
                    <h3 class="text-lg font-bold mb-2">Passing </h3> <span class="bg-green-700 px-2 py-1 rounded">{{parseFloat(currentPlayer.passing).toFixed(2)}}</span>
                </div>

                <!-- Movement -->
                <div>
                    <h3 class="text-lg font-bold mb-2">Dribbling</h3> <span class="bg-green-700 px-2 py-1 rounded">{{parseFloat(currentPlayer.dribbling).toFixed(2)}}</span>
                </div>

                <!-- Power -->
                <div>
                    <h3 class="text-lg font-bold mb-2">Defending</h3> <span class="bg-green-700 px-2 py-1 rounded">{{parseFloat(currentPlayer.defending).toFixed(2)}}</span>
                </div>

                <!-- Mentality -->
                <div>
                    <h3 class="text-lg font-bold mb-2">Physic</h3> <span class="bg-green-700 px-2 py-1 rounded">{{parseFloat(currentPlayer.physic).toFixed(2)}}</span>
                </div>

            </div>

            <div class="ml-6">
                <img src="${pageContext.request.contextPath}/images/graphic-player.png" alt="Grafico" class="w-64 h-auto rounded-lg shadow-md" />
            </div>
        </div>
    </div>


</div>

<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/vue.js"></script>
<script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
