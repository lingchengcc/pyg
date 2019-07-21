var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        entity: {},
        ids: [],
        grade: 1,
        entity_1: {},
        entity_2: {},
        searchEntity: {parentId: "0"},
        typeOptions: []
    },
    methods: {
        cleanEntity: function () {
            this.entity = {};
            if (this.grade === 1) {
                this.entity.parentId = 0;
            }
            if (this.grade === 2) {
                this.entity.parentId = this.entity_1.id;
            }
            if (this.grade === 3) {
                this.entity.parentId = this.entity_2.id;
            }
        },
        findAllType: function () {
            axios.get('/typeTemplate/findAll.shtml').then(function (response) {
                var typeList = response.data;
                for (var i = 0; i < typeList.length; i++) {
                    var type = typeList[i];
                    app.typeOptions.push({"id": type.id, "text": type.name})
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        findByParentId: function (parentId) {
            axios.get('/itemCat/findByParentId/' + parentId + '.shtml').then(function (response) {
                app.list = response.data;

            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        searchList: function (p_entity) {
            if (this.grade === 1) {
                this.entity_1 = {};
                this.entity_2 = {}
            }
            if (this.grade === 2) {
                this.entity_1 = p_entity;
                this.entity_2 = {}
            }
            if (this.grade === 3) {
                this.entity_2 = p_entity;
            }
            this.findByParentId(p_entity.id)
        },
        //查询所有品牌列表
        findAll: function () {
            console.log(app);
            axios.get('/itemCat/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        }
        ,
        findPage: function () {
            var that = this;
            axios.get('/itemCat/findPage.shtml', {
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
        }
        ,
        //该方法只要不在生命周期的
        add: function () {
            axios.post('/itemCat/add.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    if (app.grade === 0) {
                        app.searchList({id: 0});
                    }
                    if (app.grade === 2) {
                        app.searchList(app.entity_1);
                    }
                    if (app.grade === 3) {
                        app.searchList(app.entity_2);
                    }
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }
        ,
        update: function () {
            axios.post('/itemCat/update.shtml', this.entity).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    if (app.grade === 1) {
                        app.searchList({id: 0});
                    }
                    if (app.grade === 2) {
                        app.searchList(app.entity_1);
                    }
                    if (app.grade === 3) {
                        app.searchList(app.entity_2);
                    }
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }
        ,
        save: function () {
            if (this.entity.id != null) {
                this.update();
            } else {
                this.add();
            }
        }
        ,
        findOne: function (id) {
            axios.get('/itemCat/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }
        ,
        dele: function () {
            axios.post('/itemCat/delete.shtml', this.ids).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    if (app.grade === 0) {
                        app.searchList({id: 0});
                    }
                    if (app.grade === 2) {
                        app.searchList(app.entity_1);
                    }
                    if (app.grade === 3) {
                        app.searchList(app.entity_2);
                    }
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        }


    },
    //钩子函数 初始化了事件和
    created: function () {

        this.searchList({id: 0});
        this.findAllType();
    }

});
