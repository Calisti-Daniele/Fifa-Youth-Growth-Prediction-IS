const app = Vue.createApp({
    data() {
        return {
            player1: null,
            player2: null,
            default_player: "images/default_player.svg",
            selectedStats: [],
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
    watch: {
        player1(newVal) {
            if (newVal && this.player2) {
                this.selectedStats = this.availableStats.map(stat => stat.value);
            }
        },
        player2(newVal) {
            if (newVal && this.player1) {
                this.selectedStats = this.availableStats.map(stat => stat.value);
            }
        }
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
        },
        exportToExcel() {
            if (!this.player1 || !this.player2 || this.selectedStats.length === 0) {
                alert("Seleziona due giocatori e almeno una statistica.");
                return;
            }

            const form = document.createElement("form");
            form.method = "POST";
            form.action = contextPath + "/exportExcel";

            const addHiddenInput = (name, value) => {
                const input = document.createElement("input");
                input.type = "hidden";
                input.name = name;
                input.value = value;
                form.appendChild(input);
            };

            addHiddenInput("player1_name", this.player1.text);
            addHiddenInput("player2_name", this.player2.text);

            this.selectedStats.forEach(stat => {
                addHiddenInput(`stats[]`, stat);
                addHiddenInput(`player1_${stat}`, this.player1[stat]);
                addHiddenInput(`player2_${stat}`, this.player2[stat]);
            });

            document.body.appendChild(form);
            form.submit();
        }
    },
    mounted() {
        this.initializeSearch();
    }
});

app.mount('#app');
