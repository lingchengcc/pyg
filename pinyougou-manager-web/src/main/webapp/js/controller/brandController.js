var app = new Vue({
    el: "#app",
    data: {
        pageNo: 1,
        pages: 15,
        brand: {},
        ids: [],
        brandList: [],
        searchBrand: {}
    },
    methods: {
        findBrandById: function (id) {
            axios.get('/brand/findOne/' + id + '.shtml').then(
                function (response) {
                    app.brand = response.data;
                }
            )
        },
        addBrand: function () {
            axios.post('/brand/add.shtml', this.brand).then(
                function (response) {
                    if (!response.data.success) {
                        alert(response.data.message)
                    }
                    app.searchBrandList(1);
                }
            )
        },
        updateBrand: function () {
            axios.post('/brand/update.shtml', this.brand).then(
                function (response) {
                    if (!response.data.success) {
                        alert(response.data.message)
                    }
                    app.searchBrandList(1);
                }
            )
        },
        saveOrUpdate: function () {
            if (this.brand.id != null) {
                app.updateBrand();
            } else {
                app.addBrand();
            }

        },
        deleteBrand: function () {
            axios.post('/brand/delete.shtml', this.ids).then(
                function (response) {
                    if (!response.data.success) {
                        alert(response.data.message)
                    }
                    app.searchBrandList(1);
                }
            )
        },
        searchBrandList: function (curPage) {
            axios.post('/brand/search.shtml?pageNo=' + curPage, this.searchBrand).then(
                function (response) {
                    app.brandList = response.data.list;
                    app.pages = response.data.pages;
                    app.pageNo = response.data.pageNo
                }
            )
        }

    },
    created: function () {
        this.searchBrandList(1)
    }
});