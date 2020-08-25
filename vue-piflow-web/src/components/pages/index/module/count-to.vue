<template>
    <count-to
            ref="example"
            :start-val="_startVal"
            :end-val="EndVal"
            :duration="_duration"
            :separator="_separator"
            class="example"
    />
</template>

<script>
    import countTo from 'vue-count-to'
    export default {
        name: "countToDemo",
        components: {
            countTo,
        },
        props:{
            EndVal: {
                type: Number,
                default: 0
            }
        },
        data:()=>{
            return {
                setStartVal: 0,
                setEndVal: '',
                setDuration: 3000,
                setSeparator: ','
            }
        },
        computed: {
            _startVal() {
                if (this.setStartVal) {
                    return this.setStartVal
                } else {
                    return 0
                }
            },
            _endVal() {
                if (this.setEndVal) {
                    return this.setEndVal
                } else {
                    return 0
                }
            },
            // 持续时间，以毫秒为单位
            _duration() {
                if (this.setDuration) {
                    return this.setDuration
                } else {
                    return 100
                }
            },
            // 分隔符
            _separator() {
                return this.setSeparator
            },
        },
    }
</script>

<style lang="scss" scoped>
    .example{
        /*color: #999;*/
        /*color: #F6416C;*/
        color: #4fb1f8;
        display: inline-block;
        /*margin: 10px 0;*/
        text-align: center;
        /*font-size: 22px;*/
        font-size: 1vw;
        font-weight: 500;
    }
</style>