var app = new Vue({
    el: "#app",
    data: {
        pages: 15,
        pageNo: 1,
        list: [],
        image_entity: {color: '', url: ''},
        image: {group: "", path: ""},
        deleImage: [],
        entity: {goods: {}, goodsDesc: {itemImages: []}, itemList: []},
        ids: [],
        searchEntity: {}
    },
    methods: {
        removePic: function (ids) {
            if (ids.length >= 1) {
                this.deleImage = [];
                for (var i = 0; i < ids.length; i++) {
                    var url = this.entity.goodsDesc.itemImages[i].url;
                    /* http://192.168.111.135/group1/M00/00/00/wKhvh10ZdSuAQ8WMAD6ECO9UxLg843.jpg*/
                    var group = url.split('/')[3];
                    var path = url.substring(url.lastIndexOf(group) + group.length + 1);
                    this.image.group = group;
                    this.image.path = path;
                    this.deleImage.push(this.image);
                }
                for (var j = 0; j < ids.length; j++) {
                    this.entity.goodsDesc.itemImages.splice(j);
                }
                axios.post('http://localhost:9110/upload/deleteFile.shtml', this.deleImage).then(function (response) {
                    alert(response.data.message)
                })
            }
        },
        searchList: function (curPage) {
            axios.post('/goods/search.shtml?pageNo=' + curPage, this.searchEntity).then(function (response) {
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
            axios.get('/goods/findAll.shtml').then(function (response) {
                console.log(response);
                //注意：this 在axios中就不再是 vue实例了。
                app.list = response.data;

            }).catch(function (error) {

            })
        },
        findPage: function () {
            var that = this;
            axios.get('/goods/findPage.shtml', {
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
            //1.获取富文本编辑器中的html的值 赋值给entity变量中的介绍属性中
            this.entity.goodsDesc.introduction = editor.html();
            axios.post('/goods/add.shtml', this.entity).then(function (response) {
                if (response.data.success) {
                    alert("11111成功");
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        update: function () {
            axios.post('/goods/update.shtml', this.entity).then(function (response) {
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
            axios.get('/goods/findOne/' + id + '.shtml').then(function (response) {
                app.entity = response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        dele: function () {
            axios.post('/goods/delete.shtml', this.ids).then(function (response) {
                console.log(response);
                if (response.data.success) {
                    app.searchList(1);
                }
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        //文件上传
        //1.模拟表单 设置数据
        //2.发送aajx请求 上传图片
        uploadFile: function () {
            //模拟创建一个表单对象
            var formData = new FormData();
            //参数formData.append('file' 中的file 为表单的参数名  必须和 后台的file一致
            //file.files[0]  中的file 指定的时候页面中的input="file"的id的值 files 指定的是选中的图片所在的文件对象数组，这里只有一个就选中[0]
            formData.append('file', file.files[0]);


            axios({
                url: 'http://localhost:9110/upload/uploadFile.shtml',
                //data就是表单数据
                data: formData,
                method: 'post',
                //指定头信息：
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                //开启跨域请求携带相关认证信息
                withCredentials: true
            }).then(function (response) {
                if (response.data.success) {
                    app.image_entity.url = response.data.message;
                } else {
                    alert(response.data.message);
                }
            })

        },
        addImage_Entity: function () {
            //向数组中添加一个图片的对象
            if (this.image_entity.url != null && this.image_entity.url !== "" && this.image_entity.url !== undefined) {
                this.entity.goodsDesc.itemImages.push(this.image_entity);
            } else {
                alert("未上传!")
            }

        }
    },
    /* watch:{

     },*/
    //钩子函数 初始化了事件和
    created: function () {


    }

});
