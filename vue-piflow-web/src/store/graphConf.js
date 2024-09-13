const InitState = () => ({
  chartType: "lineChart", //  lineChart  | barChart | PieChart
  xAxisType: "",
  xSort:'origin',
  float:2,
  xData: [],
  yAxisType: [],
  yData: [],
  baseOptions: {
    color: [
      "#7cb5ec",
      "#90ed7d",
      "#f7a35c",
      "#8085e9",
      "#058DC7",
      "#f15c80",
      "#e4d354",
      "#2b908f",
      "#f45b5b",
      "#91e8e1",
      "#50B432",
    ],
    backgroundColor:'#fff',
    title: {
      show: true,
      left: "center",
      text: "",
      top: "",
      textStyle: {
        color: "#000",
      },
    },
    dataZoom:[
      {
        type: 'inside',
        xAxisIndex: [0],
        disabled:false,
        end :100
      },
      {
        type: 'slider',
        show:false,
        xAxisIndex: [0],
        bottom: 0,
        height: 20,
        end :100
      }
    ],

    brush: {
      xAxisIndex: 'all',
      brushLink: 'all',
      outOfBrush: {
        colorAlpha: 0.1
      }
    },
    legend: {
      show: true,
      orient: "horizontal",
      left: "20",
      top:'25'
    },
    grid: {
      top: '20%',
      bottom: "10%",
      left: "10%",
      right: "10%",
    },
    tooltip:{
      show:true,
      triggerOn:'mousemove'
    },
    textStyle: {
      color: "#000",
      fontStyle: "normal",
      fontWeight: "normal",
      fontSize: 12,
    },
    yAxis: {
      show: true,
      name: "",
      type: "value",
      boundaryGap: false,
      nameLocation: "end",
      axisLine: {
        show: false,
        lineStyle: {
          color: "#999",
        },
      },
      axisTick: {
        show: false,
        lineStyle: {
          color: "#999",
        },
      },
      axisLabel: {
        show: true,
        lineStyle: {
          color: "#999",
        },
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: "dashed",
        },
      },
    },
    xAxis: {
      show: true,
      name: "",
      type: "category",
      boundaryGap: true,
      nameLocation: "end",
      axisLine: {
        show: true,
        lineStyle: {
          color: "#999",
        },
      },
      axisTick: {
        show: true,
        lineStyle: {
          color: "#999",
        },
      },
      axisLabel: {
        show: true,
        lineStyle: {
          color: "#999",
        },
      },
      splitLine: {
        show: false,
        lineStyle: {
          type: "dashed",
        },
      },
    },
  },
  lineChart: {
    type: "line",
    smooth: false,
    label: {
      show: false,
    },
    lineStyle: {
      // color: "#000",
      width: 2,
      type: "solid",
      opacity: 1,
    },

  },
  barChart: {
    type: "bar",
    barMaxWidth: 20,
    label: {
      show: false,
      distance: 10,
      color: "#000",
      position: "top",
    },
  },
  pieChart: {
    type: "pie",
    label: {
      show: true,
    },
    lineStyle: {
      // color: "#000",
      width: 2,
      type: "solid",
      opacity: 1,
    },
  },
  brush:{
    toolbox:['rect','clear']
  },
  extraOptions: {},
});
const state = {
  ...InitState(),
};

const getters = {
  chartType: (state) => state.chartType,
  xAxisType: (state) => state.xAxisType,
  xSort: (state) => state.xSort,
  float: (state) => state.float,
  xData: (state) => state.xData,
  yAxisType: (state) => state.yAxisType,
  yData: (state) => state.yData,
  baseOptions: (state) => state.baseOptions,
  extraOptions: (state) => state.extraOptions,
  lineChart: (state) => state.lineChart,
  barChart: (state) => state.barChart,
  PieChart: (state) => state.PieChart,
};


const mutations = {
  CHANGE_CHARTTYPE: (state, val) => {
    state.chartType = val;
  },
  CHANGE_XAXISTYPE: (state, val) => {
    state.xAxisType = val;
  },
  CHANGE_XSORT: (state, val) => {
    state.xSort = val;
  },
  CHANGE_FLOAT: (state, val) => {
    state.float = val;
  },
  CHANGE_YAXISTYPE: (state, val) => {
    state.yAxisType = val;
  },
  CHANGE_XDATA: (state, val) => {
    state.xData = val;
  },
  CHANGE_YDATA: (state, val) => {
    state.yData = val;
  },
  CHANGE_BASEOPTIONS: (state, val) => {
    state.baseOptions = val;
  },
  CHANGE_LINEOPTIONS: (state, val) => {
    state.lineChart = val;
  },
  CHANGE_PIEOPTIONS: (state, val) => {
    state.pieChart = val;
  },
  CHANGE_BAROPTIONS: (state, val) => {
    state.barChart = val;
  },
  CHANGE_EXTRAOPTIONS: (state, val) => {
    state.extraOptions = val;
  },
  INITSTATE: (state, val) => {
    const data = InitState();
    for (const key in data) {
      state[key] = data[key];
    }
  },
};

const actions = {
  changeChartType({ commit }, val) {
    commit("CHANGE_CHARTTYPE", val);
  },
  changexAxisType({ commit }, val) {
    commit("CHANGE_XAXISTYPE", val);
  },
  changexSort({ commit }, val) {
    commit("CHANGE_XSORT", val);
  },
  changeFloat({ commit }, val) {
    commit("CHANGE_FLOAT", val);
  },
  changeyAxisType({ commit }, val) {
    commit("CHANGE_YAXISTYPE", val);
  },
  changexData({ commit }, val) {
    commit("CHANGE_XDATA", val);
  },
  changeyData({ commit }, val) {
    commit("CHANGE_YDATA", val);
  },
  changeBaseOptions({ commit }, val) {
    commit("CHANGE_BASEOPTIONS", val);
  },
  changeExtraOptions({ commit }, val) {
    commit("CHANGE_EXTRAOPTIONS", val);
  },
  changeLineOptions({ commit }, val) {
    commit("CHANGE_LINEOPTIONS", val);
  },
  changeBarOptions({ commit }, val) {
    commit("CHANGE_BAROPTIONS", val);
  },
  changePieOptions({ commit }, val) {
    commit("CHANGE_PIEOPTIONS", val);
  },
  initState({ commit }, val) {
    commit("INITSTATE");
  },
};

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions,
};
