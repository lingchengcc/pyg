var app = new Vue({
    el: "#app",
    data: {
        payObject: {}
    },
    methods: {
        queryPayStatus: function (out_trade_no) {
            axios.post('/pay/queryPayStatus/' + out_trade_no + '.shtml').then(
                response => {
                    if (response.data) {

                        if (response.data.success) {
                            window.location.href = "paysuccess.html?money="+app.payObject.total_fee;
                        } else {
                            if (response.data.message === '超时') {
                                window.location.href="payfail.html"
                            } else {
                                window.location.href = "payfail.html";
                            }
                        }
                    } else {
                        alert("出错");
                    }
                }
            )
        },
        createNative: function () {
            axios.post('/pay/createNative.shtml').then(
                response => {
                    if (response.data) {
                        app.payObject = response.data;
                        app.payObject.total_fee = app.payObject.total_fee / 100;
                    }
                    var rious = new QRious({
                        element: document.getElementById('qrious'),
                        size: 250,
                        level: 'H',
                        value: app.payObject.code_url
                    });
                    if (rious) {
                        app.queryPayStatus(app.payObject.out_trade_no);
                    }
                }
            )
        }
    },
    created: function () {
        //页面一加载就应当调用
        if(window.location.href.indexOf("pay.html")!==-1){
            this.createNative();
        }else {
            let urlParamObject = this.getUrlParam();
            if(urlParamObject.money)
                this.totalMoney=urlParamObject.money;
        }
    }
});