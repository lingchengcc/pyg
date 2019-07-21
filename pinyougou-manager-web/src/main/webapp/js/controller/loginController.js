var app = new Vue({
        el: "#app",
        data: {
            name: ""
        },
        methods: {
            getLoginName: function () {
                axios.post('/login/getName.shtml').then(
                    function (response) {
                        app.name = response.data;
                    }
                )
            }
        },
        created: function () {
            this.getLoginName();
        }
    }
);