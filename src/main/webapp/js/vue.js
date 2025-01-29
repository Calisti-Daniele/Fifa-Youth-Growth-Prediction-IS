const app = Vue.createApp({
    el: '#app',
    data() {
        return {
            currentPlayer: null,
            default_player: "images/default_player.svg",
            positions_color: ['blue','green','cyan']
        };
    },
    methods: {
        initializeSearch() {
            $('#search').select2({
                placeholder: 'Search for anything...',
                minimumInputLength: 5,
                ajax: {
                    url: 'realtimesearch',
                    type: 'GET',
                    dataType: 'json',
                    delay: 250,
                    data: function (params) {
                        return {
                            query: params.term
                        };
                    },
                    processResults: function (data) {
                        return {
                            results: data.map(item => ({
                                id: item.name,
                                text: item.name,
                                image: item.image,
                                nationality: item.nationality_name,
                                positions: item.player_positions,
                                overall: item.overall,
                                defending: item.defending,
                                passing: item.passing,
                                dribbling: item.dribbling,
                                shooting: item.shooting,
                                physic: item.physic
                            }))
                        };
                    },
                    cache: true
                },
                templateResult: function (item) {
                    if (!item.id) {
                        return item.text;
                    }
                    return $(
                        `<div class='flex items-center space-x-2'>
                    <img src='${item.image}' alt='${item.text}' class='w-6 h-6 rounded-full'>
                    <span>${item.text}</span>
                </div>`
                    );
                },
                templateSelection: function (item) {
                    return item.text || item.id;
                }
            }).on('select2:select', (e) => {
                const selectedData = e.params.data;
                this.updateCurrentPlayer(selectedData);
            });
        },
        updateCurrentPlayer(player) {

            console.log(player);
            // Aggiorna il giocatore corrente con i dati selezionati
            this.currentPlayer = {
                id: player.id,
                name: player.text,
                image: player.image || this.default_player,
                nationality: player.nationality_name,
                positions: player.positions,
                overall: player.overall,
                defending: player.defending,
                passing: player.passing,
                dribbling: player.dribbling,
                shooting: player.shooting,
                physic: player.physic
            };
        }
    },
    mounted() {
        this.initializeSearch();
    }
});

app.mount('#app');
