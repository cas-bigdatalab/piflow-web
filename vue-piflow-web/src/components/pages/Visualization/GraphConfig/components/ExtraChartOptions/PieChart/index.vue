<template>
  <section>
    <div class="formWrap">
      <div>
        <label>颜色：</label>
        <Input v-model="pieChart.lineStyle.color" />
      </div>
      <div>
        <label>线宽：</label>
        <Input v-model="pieChart.lineStyle.width" />
      </div>
      <div>
        <label>对齐方法：</label>
        <Select v-model="pieChart.lineStyle.type">
          <Option :value="'solid'">实线</Option>
          <Option :value="'dashed'">点线</Option>
          <Option :value="'dotted'">虚线</Option>
        </Select>
      </div>
      <div>
        <label>不透明度：</label>
        <Input v-model="pieChart.lineStyle.opacity" />
      </div>
    </div>
  </section>
</template>
  
  <script>
import { mapGetters } from "vuex";
export default {
  name: "PieChart",
  computed: {
    ...mapGetters("graphConf", ["pieChart"]),
  },

};
</script>
<style lang="scss" scoped>
.formWrap {
  > div {
    label {
      min-width: 100px;
      text-align: right;
    }
    display: flex;
    align-items: center;
    margin-bottom: 5px;
  }
}
</style>
  
  