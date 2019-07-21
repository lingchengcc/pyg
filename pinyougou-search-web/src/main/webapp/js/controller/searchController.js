var app = new Vue({
        el: "#app",
        data: {
            //作为条件查询的对象
            searchMap: {
                'keywords': '',
                'category': '',
                'brand': '',
                spec: {},
                'price': '',
                'pageNo': 1,
                'pageSize': 40,
                'sortField': '',
                'sortType': ''
            },
            //返回的结果对象
            resultMap: {},
            pageLabels: [],
            preDott: false,
            nextDott: false

        },
        methods: {
            doSort: function (sortField, sortType) {
                this.searchMap.sortField = sortField;
                this.searchMap.sortType = sortType;
                this.search();
            },
            isKeywordsIsBrand: function () {
                if (this.resultMap.brandList != null && this.resultMap.brandList.length > 0) {
                    for (var i = 0; i < this.resultMap.brandList.length; i++) {
                        if (this.searchMap.keywords.indexOf(this.resultMap.brandList[i].text) != -1) {
                            this.searchMap.brand = this.resultMap.brandList[i].text;
                            return true;
                        }
                    }
                }
                return false;
            },
            clear: function () {
                this.searchMap = {
                    'keywords': this.searchMap.keywords,
                    'category': '',
                    'brand': '',
                    spec: {},
                    'price': '',
                    'pageNo': 1,
                    'pageSize': 40
                };
            },
            queryByPage: function (pageNo) {
                pageNo = parseInt(pageNo);
                this.searchMap.pageNo = pageNo;
                this.search();
            }
            ,
            buildPageLabel: function () {
                this.pageLabels = [];
                let firstPage = 1;
                let lastPage = this.resultMap.totalPages;
                if (this.resultMap.totalPages > 5) {
                    if (this.searchMap.pageNo <= 3) {
                        firstPage = 1;
                        lastPage = 5;
                        this.preDott = false;
                        this.nextDott = true;
                    } else if (this.searchMap.pageNo >= this.resultMap.totalPages - 2) {
                        firstPage = this.resultMap.totalPages - 4;
                        lastPage = this.resultMap.totalPages;
                        this.preDott = true;
                        this.nextDott = false;
                    } else {
                        firstPage = this.searchMap.pageNo - 2;
                        lastPage = this.searchMap.pageNo + 2;
                        this.preDott = true;
                        this.nextDott = true;
                    }
                } else {
                    this.preDott = false;
                    this.nextDott = false;
                }
                for (let i = firstPage; i <= lastPage; i++) {
                    this.pageLabels.push(i);
                }
            }
            ,
            //根据搜索的条件 执行查询 返回结果 resultmap 点击的时候调用
            search: function () {
                axios.post('/itemSearch/search.shtml', this.searchMap).then(
                    function (response) {//response.data=map 会有集合数据
                        app.resultMap = response.data;
                        app.buildPageLabel();
                    }
                )
            }
            ,
            //添加搜索项
            addSearchItem: function (key, value) {
                if (key === 'category' || key === 'brand' || key === 'price') {
                    this.searchMap[key] = value;
                } else {
                    this.searchMap.spec[key] = value;
                }
                //发送请求 执行搜索
                this.search();
            }
            ,
            //移除掉搜索项
            removeSearchItem: function (key) {
                //1.移除变量里面的值
                if (key === 'category' || key === 'brand' || key === 'price') {
                    this.searchMap[key] = '';
                } else {
                    delete this.searchMap.spec[key];
                }
                //2.重新发送请求查询
                this.search();
            }
        }
        ,
        created: function () {
            var urlParamObj = this.getUrlParam();
            if (urlParamObj.keywords !== undefined ) {
                this.searchMap.keywords = decodeURIComponent(urlParamObj.keywords);
                this.search();
            }
        }
    })
;