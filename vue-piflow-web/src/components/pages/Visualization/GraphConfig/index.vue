<template>
  <div class="wrap">
    <div style="overflow: auto">
      <ChartType />
      <div>
        <BaseChartOptions />
        <!-- <ExtraChartOptions /> -->
      </div>
    </div>
    <div class="wrap_r">
      <AxisSelect :tableData="tableData" />
      <Chart
        :options="options"
        @saveOptions="handleUpdate"
        @autoSave="handleAutoUpdate"
        @showTableData="handleShowOriginDataTable"
      />
    </div>
    <OriginDataTable  ref="OriginDataTableRef"  />
    <SelectAxisType  ref="SelectAxisType" @submit="handleSetAxis"/>
  </div>
</template>

<script>
import ChartType from "./components/ChartType";
import BaseChartOptions from "./components/BaseChartOptions";
import ExtraChartOptions from "./components/ExtraChartOptions";
import AxisSelect from "./components/AxisSelect";
import Chart from "./components/Chart";
import OriginDataTable from "./components/OriginDataTable";
import { mapGetters } from "vuex";
import { handleDeepMerge, handleFormata } from "./utils";
import SelectAxisType from './components/SelectAxisType'
export default {
  name: "GraphConfig",
  components: {
    ChartType,
    BaseChartOptions,
    ExtraChartOptions,
    Chart,
    AxisSelect,
    OriginDataTable,
    SelectAxisType
  },
  data() {
    return {
      show: false,
      // baseOptions: {},
      // extraOptions: {},
      // 格式化的数据
      tableData: [],
      // 原始数据
      originalSortTableData: [],
      // xAxisType: "",
      xData: [],
      // yAxisType: [],
      yData: [],
    };
  },

  created() {
    if (this.$route.query.id) {
      this.handleInit();
    } else if (this.$route.query.tableName) {
      this.handleCreatByQuery();
    }
  },
  watch: {
    xAxisType(val) {
      if (this.tableData.length) {
        // 设置是否展示 dataZoom
        this.xData = this.handleGetColums(val);
        if (this.xData.length > 12 ) {
          this.baseOptions.dataZoom[0].disabled == false;
          this.baseOptions.dataZoom[0].end = this.xData.length > 50 ? 30 : 50;
          this.baseOptions.dataZoom[1].show = true;
          this.baseOptions.dataZoom[1].end = this.xData.length > 50 ? 30 : 50;
        }else{
          this.baseOptions.dataZoom[1].show == false
          this.baseOptions.dataZoom[1].show = false;
        }
        this.handleChangeOriginSortData()
      }
    },
    xSort() {
      this.handleChangeOriginSortData()
    },
    yAxisType: {
      handler: function(arr) {
        if (this.originalSortTableData.length) {
          this.yData = arr.map((item) => {
            return {
              itemStyle: {
                color: item.color,
              },
              name: item.label,
              data: this.handleGetColums(item),
            };
          });
        this.handleChangeOriginSortData()

        }
      },
      deep: true,
    },
  },
  computed: {
    ...mapGetters("graphConf", [
      "chartType",
      "xAxisType",
      "xSort",
      "float",
      "yAxisType",
      "baseOptions",
      "extraOptions",
      "lineChart",
      "barChart",
      "pieChart",
    ]),
    options: {
      get: function() {
        if (!this.baseOptions?.xAxis) return {};
        const data = JSON.parse(JSON.stringify(this.baseOptions));
        const obj = {
          ...data,
          series: this.series,
        };
        obj.xAxis.data = this.xData;
        return obj;
      },
      set: function(newVal) {
        return newVal;
      },
    },
    //获取series  由 图类型设置 （this[this.chartType]） 和  ydata 共同生成
    series() {
      const obj = JSON.parse(JSON.stringify(this[this.chartType]));
      const i = this.float
      obj.label.formatter = (param)=>  { 
        if((!i && i!==0 ) ||  i == -1){  
            return param.value
        }else{
          return   Math.floor(param.value *Math.pow(10,i))/Math.pow(10,i)
        }
      } 
      const list = this.yData.map((item) => ({
        ...obj,
        ...item,
      }));
      return list;
    },
  },
  methods: {
    handleChangeOriginSortData(){
      if(!this.originalTableData) return
      const list  = JSON.parse(JSON.stringify(this.originalTableData))
        //x轴类型改变
        if (this.xSort === "value") {
          this.originalSortTableData = list.sort((a, b) => {
            const numA = parseFloat(a[this.xAxisType.label]);
            const numB = parseFloat(b[this.xAxisType.label]);
            if (numA < numB) return -1;
            if (numA > numB) return 1;
            return 0;
          });
        } else if(this.xSort === 'category') {
          this.originalSortTableData = list.sort((a, b) =>
            String(a[this.xAxisType.label]).localeCompare(String(b[this.xAxisType.label]))
          );
        }else if(this.xSort === 'origin'){
          this.originalSortTableData  = list
        }
        this.xData = this.handleGetColums(this.xAxisType);

        // 由于大表的排序改变，所以y轴数据也需要重新生成
        this.yData = this.yAxisType.map((item) => {
          return {
            itemStyle: {
              color: item.color,
            },
            name: item.label,
            data: this.handleGetColums(item),
          };
        });
    },
    handleShowOriginDataTable(){
      this.$refs.OriginDataTableRef.show(this.originalTableData)
    },
    handleCreatByQuery() {
      this.$event.emit("loading", true);
      this.$axios({
        method: "POST",
        url: "/visual/getVisualData",
        data: { tableName: this.$route.query.tableName },
      })
        .then((res) => {
          this.$event.emit("loading", false);
          if (res.data.code === 200) {
            const list = res.data.data;
            this.originalSortTableData = list;
            this.originalTableData = JSON.parse(JSON.stringify(list))
            this.tableData = Object.keys(list[0]).map((item) => {
              const obj = {
                label: item,
                data: [],
              };
              this.originalSortTableData.forEach((v) => {
                obj.data.push(v[item]);
              });
              return obj;
            });

            // 配置项
            this.$store.dispatch(
              "graphConf/changeBaseOptions",
              this.baseOptions
            );

            if (this.$route.query.x) {
              const xAxisType = { label: this.$route.query.x };
              this.$store.dispatch("graphConf/changexAxisType", xAxisType);
            }
            if (this.$route.query.y) {
              let yAxisType = this.$route.query.y.split(",").map((v, i) => ({
                label: v,
                color: this.baseOptions.color[i],
              }));
              this.$store.dispatch("graphConf/changeyAxisType", yAxisType);
            }
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    // 自动保存
    handleAutoUpdate() {
      if (this.autoSaveTimeout) {
        clearTimeout(this.autoSaveTimeout);
      }
      this.autoSaveTimeout = setTimeout(() => {
        this.handleUpdate("update", "auto");
      }, 4000);
    },
    handleInit() {
      if (!this.$route.query.id) return;
      this.$store.dispatch("graphConf/initState", {});
      this.$event.emit("loading", true);
      this.$axios({
        method: "POST",
        url: "/visual/getGraphConfById",
        data: { id: this.$route.query.id },
      })
        .then((res) => {
          if (res.data.code === 200) {
            delete res.data.data.createTime;
            delete res.data.data.updateTime;
            this.graphConf = res.data.data;
            this.handleGetTableData(this.graphConf.graphTemplateId);
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },
    handleGetColums(data) {
      if(data){
        let list = [];
        this.originalSortTableData.forEach((item) => {
          for (const key in item) {
            if (key === data.label) list.push(item[key]);
          }
        });

        return list;
      }else{
        return []
      }

    },
    handleGetTableData(graphTemplateId) {
      this.$axios({
        method: "POST",
        url: "/visual/getTableData",
        data: { graphTemplateId },
      }).then((res) => {
        this.$event.emit("loading", false);

        if (res.data.code === 200 && res.data.data.length) {
          const list = res.data.data;
          this.originalSortTableData = list;
          this.originalTableData = JSON.parse(JSON.stringify(list))
          this.tableData = Object.keys(list[0]).map((item) => {
            const obj = {
              label: item,
              data: [],
            };
            this.originalSortTableData.forEach((v) => {
              obj.data.push(v[item]);
            });
            return obj;
          });
          // 赋值
          if (this.graphConf.configInfo) {
            const {
              baseOptions,
              extraOptions,
              xAxisType,
              yAxisType,
              chartType,
              xSort,
              float
            } = JSON.parse(this.graphConf.configInfo);
            this.$store.dispatch("graphConf/changexAxisType", xAxisType);
            this.$store.dispatch("graphConf/changeyAxisType", yAxisType);
            this.$store.dispatch("graphConf/changeChartType", chartType);
            this.$store.dispatch("graphConf/changexSort", xSort);
            this.$store.dispatch("graphConf/changeFloat", float);
            this.$store.dispatch(
              "graphConf/changeBaseOptions",
              handleDeepMerge(this.baseOptions, baseOptions)
            );
            if (chartType === "lineChart") {
              this.$store.dispatch(
                "graphConf/changeLineOptions",
                handleDeepMerge(this.lineChart, extraOptions)
              );
            } else if (chartType === "barChart") {
              this.$store.dispatch(
                "graphConf/changeBarOptions",
                handleDeepMerge(this.barChart, extraOptions)
              );
            } else if (chartType === "pieChart") {
              this.$store.dispatch(
                "graphConf/changePieOptions",
                handleDeepMerge(this.pieChart, extraOptions)
              );
            }
          } else {
            this.baseOptions.title.text = this.graphConf.name;
            this.$store.dispatch(
              "graphConf/changeBaseOptions",
              this.baseOptions
            );
            this.$refs.SelectAxisType.handleOpen(this.tableData)
            //如果第一次进来没有配置项

          }
        } else {
          this.$Message.error({
            content: this.$t("tip.request_fail_content"),
            duration: 3,
          });
        }
      });
    },
    handleSetAxis({xAxisType,yAxisType}){
      this.$store.dispatch("graphConf/changexAxisType", xAxisType);
      this.$store.dispatch("graphConf/changeyAxisType", yAxisType);
      this.handleAutoUpdate()
    },


    handleUpdate(type, action) {
      if (!this.$route.query.id) return;
      const configInfo = {
        chartType: this.chartType,
        baseOptions: handleFormata(this.baseOptions),
        extraOptions: this[this.chartType],
        xAxisType: this.xAxisType,
        yAxisType: this.yAxisType,
        xSort:this.xSort,
        float:this.float
      };
      if (action !== "auto") {
        this.$event.emit("loading", true);
      }
      this.$axios({
        method: "POST",
        url: "/visual/updateGraphConf",
        data: {
          ...this.graphConf,
          configInfo: JSON.stringify(configInfo),
          addFlag: type,
        },
      }).then((res) => {
        this.$event.emit("loading", false);
        if (res.data.code === 200) {
          if (action !== "auto") {
            this.$Message.success({
              content: this.$t("tip.update_success_content"),
              duration: 3,
            });
          }
        } else {
          this.$Message.error({
            content: res.data.msg,
            duration: 3,
          });
        }
      });
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.wrap {
  height: calc(100% - 89px);
  width: 100%;
  background: rgba(247, 248, 250, 1);
  display: flex;
  > div:first-child {
    width: 400px;
    flex-shrink: 0;
  }
  .wrap_r {
    position: relative;
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    height: 100%;
    > section:last-child {
      flex-grow: 1;
    }
  }
}
</style>
