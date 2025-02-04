const app = Vue.createApp({
    el: "#app",
    data() {
        return {
            player1: null,
            player2: null,
            default_player: "images/default_player.svg",
        };
    },
    methods: {
        initializeSearch() {
            const self = this;

            function setupSelect(id, playerIndex) {
                $(`#${id}`).select2({
                    placeholder: 'Cerca un giocatore...',
                    minimumInputLength: 3,
                    ajax: {
                        url: contextPath + '/realtimesearch',
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
                                    nationality_name: item.nationality_name,
                                    player_positions: item.player_positions,
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
                    }
                }).on('select2:select', function (e) {
                    const selectedData = e.params.data;
                    if (playerIndex === 1) {
                        self.player1 = selectedData;
                    } else {
                        self.player2 = selectedData;
                    }
                });
            }

            setupSelect('search1', 1);
            setupSelect('search2', 2);
        },
        highlight(value1, value2) {
            if (value1 > value2) return "text-green-500 font-bold";
            if (value1 < value2) return "text-red-500 font-bold";
            return "";
        }
    },
    mounted() {
        this.initializeSearch();
    }
});

app.mount('#app');
