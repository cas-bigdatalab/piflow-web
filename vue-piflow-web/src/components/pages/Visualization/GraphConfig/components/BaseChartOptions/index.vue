<template>
  <section class="card">
    <div class="card">
      <h1>图表配置</h1>
    </div>
    <div class="formWrap" style="padding: 0 16px;">
      <div>
        <label>图表标题：</label>
        <Input v-model="baseOptions.title.text" />
      </div>
      <div>
        <label>横轴排序：</label>
        <Select v-model="xType"  @on-change="handleXTypeChange">
          <Option value="origin">原始</Option>
          <Option value="value">数值</Option>
          <Option value="category">字符</Option>
        </Select>
      </div>
      <div>
          <label>小数位数：</label>
          <InputNumber  @on-change="handleFloatChange"  v-model="float1" type="number"  :min="-1"  :max="30"/>
       </div>
    </div>
    <ExtraChartOptions />

    <h1>高级配置</h1>
    <Collapse simple v-if="baseOptions">
      <!-- title 配置 -->
      <Panel name="title">
        标题(title)
        <template #content>
          <div class="formWrap">
            <div>
              <label>显示：</label>
              <Checkbox v-model="baseOptions.title.show">是</Checkbox>
            </div>
            <div>
              <label>颜色：</label>
              <ColorPicker
                v-model="baseOptions.title.textStyle.color"
                recommend
              />
            </div>
            <!-- <div>
              <label>文本：</label>
              <Input v-model="baseOptions.title.text" />
            </div> -->
            <div>
              <label>位置：</label>
              <Select v-model="baseOptions.title.left">
                <Option value="left">居左</Option>
                <Option value="right">居右</Option>
                <Option value="center">居中</Option>
              </Select>
            </div>
            <div>
              <label>纵向偏移：</label>
              <Input v-model="baseOptions.title.top" />
            </div>
          </div>
        </template>
      </Panel>
      <!-- legend 配置 -->
      <Panel name="legend">
        图例(legend)
        <template #content>
          <div class="formWrap">
            <div>
              <label>显示：</label>
              <Checkbox v-model="baseOptions.legend.show">是</Checkbox>
            </div>
            <div>
              <label>布局朝向：</label>
              <Select v-model="baseOptions.legend.orient">
                <Option :value="'auto'">自动</Option>
                <Option :value="'horizontal'">水平</Option>
                <Option :value="'vertical'">垂直</Option>
              </Select>
            </div>
            <div>
              <label>位置：</label>
              <Select v-model="baseOptions.legend.left">
                <Option value="20">居左</Option>
                <Option value="right">居右</Option>
                <Option value="center">居中</Option>
              </Select>
            </div>
          </div>
        </template>
      </Panel>
      <!-- textStyle 配置 -->
      <Panel name="textStyle">
        字体样式(textStyle)
        <template #content>
          <div class="formWrap">
            <div>
              <label>颜色：</label>
              <ColorPicker v-model="baseOptions.textStyle.color" recommend />
            </div>
            <div>
              <label>字体风格：</label>
              <Select v-model="baseOptions.textStyle.fontStyle">
                <Option :value="'normal'">正常</Option>
                <Option :value="'italic'">斜体</Option>
              </Select>
            </div>
            <div>
              <label>字体粗细：</label>
              <Select v-model="baseOptions.textStyle.fontWeight">
                <Option :value="'normal'">正常</Option>
                <Option :value="'bold'">加粗</Option>
                <Option :value="'lighter'">细体</Option>
              </Select>
            </div>
          </div>
        </template>
      </Panel>
      <!-- yAxis 配置 -->
      <Panel name="xAxis">
        x 轴(xAxis)
        <template #content>
          <div class="formWrap">
            <div>
              <label>显示：</label>
              <Checkbox v-model="baseOptions.xAxis.show">是</Checkbox>
            </div>
            <div>
              <label>名称：</label>
              <Input v-model="baseOptions.xAxis.name" />
            </div>
            <div>
              <label>位置：</label>
              <Select v-model="baseOptions.xAxis.nameLocation">
                <Option value="start">居左</Option>
                <Option value="end">居右</Option>
                <Option value="center">居中</Option>
              </Select>
            </div>

            <div>
              <label>显示轴线：</label>
              <Checkbox v-model="baseOptions.xAxis.axisLine.show">是</Checkbox>
            </div>
            <div>
              <label>轴线颜色：</label>
              <ColorPicker
                recommend
                v-model="baseOptions.xAxis.axisLine.lineStyle.color"
              />
            </div>
            <div>
              <label>两侧留白：</label>
              <Checkbox v-model="baseOptions.xAxis.boundaryGap">是</Checkbox>
            </div>
            <div>
              <label>显示刻度：</label>
              <Checkbox v-model="baseOptions.xAxis.axisTick.show">是</Checkbox>
            </div>
            <div>
              <label>显示标签：</label>
              <Checkbox v-model="baseOptions.xAxis.axisLabel.show">是</Checkbox>
            </div>
            <div>
              <label>显示分割线：</label>
              <Checkbox v-model="baseOptions.xAxis.splitLine.show">是</Checkbox>
            </div>
            <div>
              <label>分割线样式：</label>
              <Select v-model="baseOptions.xAxis.splitLine.lineStyle.type">
                <Option :value="'solid'">实线</Option>
                <Option :value="'dashed'">点线</Option>
                <Option :value="'dotted'">虚线</Option>
              </Select>
            </div>
          </div>
        </template>
      </Panel>
      <!-- yAxis 配置 -->
      <Panel name="yAxis">
        y 轴(yAxis)
        <template #content>
          <div class="formWrap">
            <div>
              <label>显示：</label>
              <Checkbox v-model="baseOptions.yAxis.show">是</Checkbox>
            </div>
            <div>
              <label>名称：</label>
              <Input v-model="baseOptions.yAxis.name" />
            </div>
            <div>
              <label>位置：</label>
              <Select v-model="baseOptions.yAxis.nameLocation">
                <Option value="start">居左</Option>
                <Option value="end">居右</Option>
                <Option value="center">居中</Option>
              </Select>
            </div>

            <div>
              <label>显示轴线：</label>
              <Checkbox v-model="baseOptions.yAxis.axisLine.show">是</Checkbox>
            </div>
            <div>
              <label>轴线颜色：</label>
              <ColorPicker
                recommend
                v-model="baseOptions.yAxis.axisLine.lineStyle.color"
              />
            </div>
            <div>
              <label>两侧留白：</label>
              <Checkbox v-model="baseOptions.yAxis.boundaryGap">是</Checkbox>
            </div>
            <div>
              <label>显示刻度：</label>
              <Checkbox v-model="baseOptions.yAxis.axisTick.show">是</Checkbox>
            </div>
            <div>
              <label>显示标签：</label>
              <Checkbox v-model="baseOptions.yAxis.axisLabel.show">是</Checkbox>
            </div>
            <div>
              <label>显示分割线：</label>
              <Checkbox v-model="baseOptions.yAxis.splitLine.show">是</Checkbox>
            </div>
            <div>
              <label>分割线样式：</label>
              <Select v-model="baseOptions.yAxis.splitLine.lineStyle.type">
                <Option :value="'solid'">实线</Option>
                <Option :value="'dashed'">点线</Option>
                <Option :value="'dotted'">虚线</Option>
              </Select>
            </div>
          </div>
        </template>
      </Panel>
      <!-- grid 配置 -->
      <Panel name="grid">
        网格(grid)
        <template #content>
          <div class="formWrap">
            <div>
              <label>top：</label>
              <Input v-model="baseOptions.grid.top" />
            </div>
            <div>
              <label>bottom：</label>
              <Input v-model="baseOptions.grid.bottom" />
            </div>
            <div>
              <label>left：</label>
              <Input v-model="baseOptions.grid.left" />
            </div>
            <div>
              <label>right：</label>
              <Input v-model="baseOptions.grid.right" />
            </div>
          </div>
        </template>
      </Panel>
      <!-- tooltip 配置 -->
      <Panel name="tooltip">
        提示框(tooltip)
        <template #content>
          <div class="formWrap">
            <div>
              <label>显示：</label>
              <Checkbox v-model="baseOptions.tooltip.show">是</Checkbox>
            </div>
            <div>
              <label>触发条件：</label>
              <Select v-model="baseOptions.tooltip.triggerOn">
                <Option value="mousemove">鼠标移动</Option>
                <Option value="click">鼠标点击</Option>
                <Option value="none">不触发</Option>
              </Select>
            </div>
          </div>
        </template>
      </Panel>
    </Collapse>
  </section>
</template>
  
  <script>
import { mapGetters,mapActions } from "vuex";
import ExtraChartOptions from "../ExtraChartOptions";
export default {
  name: "BaseChartOptions",
  components: { ExtraChartOptions },
  data() {
    return {
      xType: ''
    }
  },
  watch:{
    xSort:{
      handler(val){
          this.xType = val
      },
      immediate:true
    },
    float:{
      handler(val){
          this.float1 = val
      },
      immediate:true
    },
    
  },
  computed: {
    ...mapGetters("graphConf", ["baseOptions",'xSort','float']),
  },
  methods:{
    ...mapActions("graphConf",['changexSort','changeFloat']),
    handleXTypeChange(val){
      this.changexSort(val)
    },
    handleFloatChange(val){
      this.changeFloat(val)
    },
  }
};
</script>
<style lang="scss" scoped>
@import "../../index.scss";
</style>
  
  