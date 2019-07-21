var app = new Vue({
    el: "#app",
    data: {
        list: [],
        keywords:''
    },
    methods: {
        doSearch:function () {
            window.location.href="http://localhost:9104/search.html?keywords="+encodeURIComponent(this.keywords);
        },
        findByCategoryId: function (categoryId) {
            axios.post('/content/findByCategoryId/' + categoryId + '.shtml').then(function (response) {
                app.list = response.data;
            }).catch(function (error) {
                alert("获取数据失败!")
            })
        }
    },
    //钩子函数 初始化了事件和
    created: function () {
        this.findByCategoryId(1);
    }
});
