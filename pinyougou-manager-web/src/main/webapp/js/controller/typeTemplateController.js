var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {brandIds: [], specIds: [], customAttributeItems: [{}]},
        ids: [],
        searchEntity: {},
        brandOptions: [],
        specOptions: [],
    },
    methods: {
        jsonToString: function (list, key) {
            //用于循环遍历  获取对象中的属性的值 拼接字符串,返回
            var listJson = JSON.parse(list);
            var str = "";
            for (var i = 0; i < listJson.length; i++) {
                var obj = listJson[i];
                str += obj[key] + ",";
            }
            if (str.length > 0) {
                str = str.substring(0, str.length - 1);
            }
            return str;
        },
        addRows: function () {
            this.entity.customAttributeItems.push({});
        },
        removeRows: function (index) {
            this.entity.customAttributeItems.splice(index, 1);
        },
        searchList: function (curPage) {
            axios.post('/typeTemplate/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {
                //获取数据
                app.list = response.data.list;

                //当前页
                app.pageNo = curPage;
                //总页数
                app.pages = response.data.pages;
            });
        },
        //查询所有品牌列表
        findAll: function () {
            console.log(app);
            axios.get('/typeTemplate/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        },
        findPage: function () {
            var that = this;
            axios.get('/typeTemplate/findPage.shtml', {
                params: {
                    pageNo: this.pageNo
                }
            }).then(function (response) {
                console.log(app);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data.list;
                app.pageNo = curPage;
                //总页数
                app.pages = response.data.pages;
            }).catch(function (error) {

            })
        },
        //该方法只要不在生命周期的
        add: function () {
            axios.post('/typeTemplate/add.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update: function () {
            axios.post('/typeTemplate/update.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        save: function () {
            if (this.entity.id != null) {
                this.update();
            } else {
                this.add();
            }
        },
        findOne: function (id) {
            axios.get('/typeTemplate/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
                app.entity.brandIds = JSON.parse(app.entity.brandIds);
                app.entity.customAttributeItems = JSON.parse(app.entity.customAttributeItems);
                app.entity.specIds = JSON.parse(app.entity.specIds);
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele: function () {
            axios.post('/typeTemplate/delete.shtml', this.ids).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        findAllBrand: function () {
            axios.post('/brand/findAll.shtml').then(
                function (response) {
                    var brandList = response.data;
                    for (var i = 0; i < brandList.length; i++) {
                        var brand = brandList[i];
                        app.brandOptions.push({"id": brand.id, "text": brand.name})
                    }
                }
            ).catch(function () {
                console.log("findAllBrand失败!")
            })
        },
        findAllSpecification: function () {
            axios.post('/specification/findAll.shtml').then(
                function (response) {
                    var specList = response.data;
                    for (var i = 0; i < specList.length; i++) {
                        var spec = specList[i];
                        app.specOptions.push({"id": spec.id, "text": spec.specName})
                    }
                }
            ).catch(function () {
                console.log("findAllSpecification失败!")
            })
        }
    },
    //钩子函数 初始化了事件和
    created: function () {

        this.searchList(1);
        this.findAllBrand();
        this.findAllSpecification()
    }

})
