var app = new Vue({
    el: "app",
    data: {
        num: 1,
        specificationItems: {}
    },
    methods: {
        addNum: num => {
            this.num += num;
            if (this.num <= 0) {
                this.num = 1;
            }
        },
        selectSpecification: (name, value) => {
            this.$set(this.specificationItems, name, value);
        },
        isSelected: (name, value) => {
            return this.specificationItems[name] === value;
        }
    },
    created: function () {

    }
});