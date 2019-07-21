var app = new Vue({
    el: "#app",
    data: {
        totalMoney: 0,//总金额
        totalNum: 0,//总数量
        cartList: [],
        addressList: [],
        address: {},
        order: {paymentType: '1'},//订单对象
        loginName: ''
    },
    methods: {
        submitOrder: function () {
            //设置值
            this.$set(this.order, 'receiverAreaName', this.address.address);
            this.$set(this.order, 'receiverMobile', this.address.mobile);
            this.$set(this.order, 'receiver', this.address.contact);
            axios.post('/order/add.shtml', this.order).then(
                function (response) {
                    if (response.data.success) {
                        //跳转到支付页面
                        window.location.href = "pay.html";
                    } else {
                        alert(response.data.message);
                    }
                }
            )
        },
        selectAddress: function (address) {
            this.address = address;
        },
        isSelectedAddress: function (address) {
            return address === this.address;

        },
        findAddressList: function () {
            axios.post('/address/findAddressListByUserId.shtml').then(response => {
                app.addressList = response.data;
                for (var i = 0; i < app.addressList.length; i++) {
                    if (app.addressList[i].isDefault == '1') {
                        app.address = app.addressList[i];
                        break;
                    }
                }
            })
        },
        getName: function () {
            axios.get('/cart/getName.shtml').then(function (response) {
                app.loginName = response.data;
            }).catch(function (error) {
                console.log("1231312131321");
            });
        },
        /**
         * 查找购物车列表
         */
        findCartList: function () {
            axios.post('/cart/findCartList.shtml').then(response => {
                app.cartList = response.data;
                let cartListAll = response.data;
                app.totalNum = 0;
                app.totalMoney = 0;
                for (let i = 0; i < cartListAll.length; i++) {
                    let cart = cartListAll[i];
                    for (let j = 0; j < cart.orderItemList.length; j++) {
                        app.totalNum += cart.orderItemList[j].num;
                        app.totalMoney += cart.orderItemList[j].totalFee;
                    }
                }
            })
        },
        /**
         * 向已有的购物车添加商品
         * @param itemId
         * @param num
         */
        addGoodsToCartList: function (itemId, num) {
            axios.get('/cart/addGoodsToCartList.shtml', {
                params: {
                    itemId: itemId,
                    num: num
                }
            }).then(function (response) {
                if (response.data.success) {
                    //添加成功
                    app.findCartList();
                } else {
                    //添加失败
                    alert(response.data.message);
                }
            });
        }
    },
    created: function () {
        this.getName();
        if (window.location.href.indexOf("getOrderInfo.html") !== -1) {
            this.findAddressList();
        }
        this.findCartList();
    }
});