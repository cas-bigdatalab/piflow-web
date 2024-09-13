<template>
  <section class="card">
    <h1>维度选择</h1>
    <div class="axisList">
      <div
        v-for="item in optionsList"
        draggable="true"
        @dragstart="handleDragStart"
        class="options"
        :key="item"
      >{{ item }}</div>
    </div>
    <div class="select">
      <div>
        <p>横轴</p>
        <div @dragover.prevent @drop="handleXDrop">
          <p v-if="!xAxisType" class="placehold">请拖入上方字段</p>
          <div v-else class="options">
            {{ xAxisType.label }}
            <span class="close" @click="handleRemoveX">x</span>
          </div>
        </div>
      </div>
      <div>
        <p>纵轴</p>
        <div @dragover.prevent @drop="handleYDrop">
          <p v-if="!yAxisType.length" class="placehold">请拖入上方字段</p>
          <div v-for="(item, index) in yAxisType" :key="index" class="options">
            <ColorPicker v-model="item.color" recommend />
            {{ item.label }}
            <span class="close" @click="handleRemoveY(index)">x</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  name: "AxisSelect",
  props: {
    tableData: Array,
  },
  data() {
    return {
      dragType: "",
      dragAxis: "",
      list:[]
    };
  },
  methods: {
    handleXDrop(e) {
      e.preventDefault();
      const data = {
        label: this.dragType,
      };
      this.$store.dispatch("graphConf/changexAxisType", data);
    },
    handleYDrop(e) {
      e.preventDefault();
      this.yAxisType.push({
        label: this.dragType,
        color: this.baseOptions.color[this.yAxisType.length],
      });
    },
    handleRemoveY(index) {
      this.yAxisType.splice(index, 1);
    },
    handleRemoveX() {
      this.xAxisType = null;
      this.$store.dispatch("graphConf/changexAxisType", null);
    },
    handleDragStart(e) {
      this.dragType = e.target.innerHTML;
    },
    handleRemove(list, target) {
      const index = list.findIndex((v) => v === target);
      if (index) list.splice(index, 1);
      return list;
    },
  },
  computed: {
    ...mapGetters("graphConf", ["xAxisType", "yAxisType", "baseOptions"]),
    optionsList(){
        if (!this.tableData) return [];
        const list = this.tableData.map((v) => v.label);
        if (this.xAxisType) {
          this.handleRemove(list, this.xAxisType.label);
        }
        if (this.yAxisType.length) {
          this.yAxisType.forEach((item) => {
            this.handleRemove(list, item.label);
          });
        }
        return list;
      },
  },

};
</script>

<style lang="scss" scoped>
@import "../../index.scss";
$primary: var(--primary-color);
.options {
  background: #9cbfef;
  padding: 2px 13px;
  margin-right: 5px;
  margin-bottom: 5px;
  border-radius: 10px 0;
}
.select {
  padding: 10px 24px;
  > div {
    display: flex;
    border-radius: 10px;
    margin-bottom: 10px;
    width: 100%;
    height: 40px;
    > p {
      background: #f7f8fa;
      margin-right: 2px;
      line-height: 40px;
      padding: 0 10px;
    }
    > div {
      background: #f7f8fa;
      flex-grow: 1;
      display: flex;
      align-items: center;
      padding-left: 5px;
      .placehold {
        line-height: 40px;
        color: rgba(0, 0, 0, 0.3);
        text-indent: 10px;
      }
      .options {
        margin-bottom: 0;
        height: 27px;
        position: relative;
        &:hover {
          .close {
            display: block;
          }
        }
        .colorLenged {
          display: inline-block;
          width: 16px;
          height: 16px;
          vertical-align: middle;
          cursor: pointer;
        }
        .close {
          display: none;
          position: absolute;
          right: -10px;
          top: -5px;
          content: "x";
          width: 16px;
          height: 16px;
          background: #fff;
          text-align: center;
          line-height: 1;
          border-radius: 50%;
          cursor: pointer;
        }
      }
    }
  }
}

.axisList {
  display: flex;
  padding: 24px 24px 0;
  flex-wrap: wrap;
}
::v-deep .ivu-color-picker-default {
  height: 20px;
  .ivu-icon {
    display: none;
  }
  .ivu-color-picker-input {
    padding: 0;
    background: none;
    border: none;
  }
}
.optionsSpan {
  margin-right: 5px;
  background: #eee;
  padding: 2px;
  cursor: pointer;
  &.active {
    color: #009688;
  }
}
.checkedSpan {
  color: #009688;
  border: 1px solid #009688;
  padding: 0 2px;
  cursor: pointer;
  display: inline-block;
  line-height: 1;
}
</style>