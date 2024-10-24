<template>
  <div>
    <Collapse simple  :value="['bar','label']" v-if="barChart">
      <!-- title 配置 -->
      <Panel name="bar">
        柱设置(bar)
        <template #content>
          <div class="formWrap">
            <div>
              <label>最大柱宽：</label>
              <InputNumber  v-model="barChart.barMaxWidth" type="number"   :max="30"/>
            </div>
            <div>
                <label>显示标签：</label>
                <Checkbox v-model="barChart.label.show">是</Checkbox>
            </div>
          </div>
        </template>
      </Panel>
     <!-- title 配置 -->
     <Panel name="label">
      标签设置(label)
      <template #content>
        <div class="formWrap">

          <div>
            <label>与柱间隔：</label>
            <InputNumber v-model="barChart.label.distance" type="number"  :max="20"/>
          </div>
          <div>
            <label>颜色：</label>
            <ColorPicker recommend v-model="barChart.label.color" />
          </div>
          <div>
            <label>位置：</label>
            <Select v-model="barChart.label.position">
              <Option value="top">顶部</Option>
              <Option value="inside">内部</Option>
            </Select>
          </div>
          
        </div>
      </template>
    </Panel>
    </Collapse>
  </div>
</template>
  
  <script>
import { mapGetters } from "vuex";
export default {
  name: "BarChart",
  computed: {
    ...mapGetters("graphConf", ["barChart"]),
  }
};
</script>
<style lang="scss" scoped>
@import "../../../index.scss";
</style>
  
  