var app = new Vue({
    el: "#app",
    data: {
        entity: {},
    },
    methods: {
        add: function () {
            axios.post('/seller/register.shtml', this.entity).then(function (response) {
                if (response.data.success) {
                    //跳转到登录页面
                    location.href = '/shoplogin.html';
                } else {
                    alert(response.message);
                }
            }).catch(function (error) {
                console.log("add错误!");
            });
        },


    }
});
