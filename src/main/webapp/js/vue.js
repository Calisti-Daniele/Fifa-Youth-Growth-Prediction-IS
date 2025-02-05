const app = Vue.createApp({
    data() {
        return {
            currentPlayer: null,
            default_player: "images/default_player.svg",
            positions_color: ['blue', 'green', 'cyan'],
            selectedStats: ['overall', 'shooting', 'passing', 'dribbling', 'defending', 'physic'], // Default stats
            availableStats: [
                { label: "Overall", value: "overall" },
                { label: "Shooting", value: "shooting" },
                { label: "Passing", value: "passing" },
                { label: "Dribbling", value: "dribbling" },
                { label: "Defending", value: "defending" },
                { label: "Physic", value: "physic" }
            ]
        };
    },
    methods: {
        initializeSearch() {
            $('#search').select2({
                placeholder: 'Cerca un giocatore...',
                minimumInputLength: 3,
                ajax: {
                    url: 'realtimesearch',
                    type: 'GET',
                    dataType: 'json',
                    delay: 250,
                    data: function (params) {
                        return { query: params.term };
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
                    if (!item.id) return item.text;
                    return $(`
                        <div class='flex items-center space-x-2'>
                            <img src='${item.image}' alt='${item.text}' class='w-6 h-6 rounded-full'>
                            <span>${item.text}</span>
                        </div>
                    `);
                },
                templateSelection: function (item) {
                    return item.text || item.id;
                }
            }).on('select2:select', (e) => {
                const selectedData = e.params.data;
                this.updateCurrentPlayer(selectedData);
            }).on('select2:open', function () {
                setTimeout(() => {
                    $('.select2-search__field').css({
                        'color': 'black',
                        'background-color': 'white',
                        'border': '1px solid #15803d',
                        'padding': '5px'
                    });
                }, 10);
            });

        },
        updateCurrentPlayer(player) {
            this.currentPlayer = {
                id: player.id,
                name: player.text,
                image: player.image || this.default_player,
                nationality_name: player.nationality_name,
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
