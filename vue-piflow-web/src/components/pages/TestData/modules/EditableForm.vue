<template>
<div>
  <vxe-grid
      border
      show-overflow
      resizable
      keep-source
      ref="xTable"
      max-height="400"
      :data="tableData"
      :loading="loading"
      :columns="tableColumn"
      class="mytable-scrollbar"
      :toolbar-config="tableToolbar"
      :edit-config="{trigger: 'click', mode: 'row', icon: 'fa fa-pencil',}">
    <template v-slot:toolbar_buttons>
      <div style="text-align: right;margin-right: 12px">
        <vxe-button icon="fa fa-trash-o" @click="$refs.xTable.removeCheckboxRow()" circle></vxe-button>
        <vxe-button icon="fa fa-plus" @click="insertEvent(-1)" circle></vxe-button>
      </div>

    </template>
  </vxe-grid>
</div>
</template>

<script>
export default {
  name: "EditableForm",
  props: {
    editableData: Array,
    editableDataId: Array,
    schemaId: String,
    tableColumn: Array,
    fatherMethod: {
      type: Function,
      default: null
    }},
  data () {
    return {
      tableToolbar: {
        perfect: true,
        zoom: true,
        custom: true,
        slots: {
          buttons: 'toolbar_buttons'
        }
      },
      tableData: [],
      tableDataId: [],
      tableTitle: [],
      loading: false,
    }
  },
  created() {
    if (this.tableData.length === 0){
      this.loading = true;
    }
  },
  watch:{
    editableData(newVal){
      this.tableData=[];
      this.tableDataId=[];
      this.$nextTick(()=>{
        this.tableData=newVal;
        this.tableDataId= this.editableDataId;
        this.loading = false;
      })
    },
    schemaId(newVal){
      this.schemaId = newVal;
    }
  },
  methods: {
    async insertEvent (row) {
      let { row: newRow } = await this.$refs.xTable.insertAt('', row)
      await this.$refs.xTable.setActiveRow(newRow, '')
    },

    saveSchemaValues(){
      const { insertRecords, removeRecords, updateRecords } = this.$refs.xTable.getRecordset();
      let schemaValuesList=[];
      if (updateRecords.length!==0){

        this.tableDataId.forEach((item)=>{
          updateRecords.forEach((items)=>{
            if (item.dataRow===items.dataRow){
              delete items._XID;
              let DataId =[],updateList= [];
              Object.keys(item).forEach(key=>{
                DataId.push(key)
              })
              Object.keys(items).forEach(key=>{
                updateList.push(key)
              })

              function exist(num, arr1) {
                for (var j = 0; j < arr1.length; j++) {
                  if (num === arr1[j]) {
                    return false;
                  }
                }
                return true; //如果不能找到相匹配的元素，返回true
              }

              let newArr = [];
              for (var i = 0; i < updateList.length; i++) {
                if (exist(updateList[i], DataId)) {
                  newArr[newArr.length] = updateList[i];
                }
              }
              newArr.forEach((items)=>{
                item[items]= ''
              })

              Object.keys(item).forEach(key=>{
                Object.keys(items).forEach(keys=>{
                  if (key === keys && keys!== 'dataRow'){
                    schemaValuesList.push({
                      schemaName: keys,
                      schemaValue: items[keys],
                      schemaValueId: item[key],
                      dataRow: items.dataRow
                    })
                  }

                })
              })
            }
          })
        })
      }
      if (insertRecords.length!==0){
        insertRecords.forEach((item,index)=>{
          delete item._XID;
          Object.keys(item).forEach(key=>{
            schemaValuesList.push({
              schemaName: key,
              schemaValue: item[key],
              schemaValueId: '',
              dataRow: this.tableData.length!==0? this.tableData.length+index+1: index+1
            })
          })

        })

      }

      if (removeRecords.length!==0){
        this.tableDataId.forEach((item)=>{
          removeRecords.forEach((items)=>{
            if (item.dataRow===items.dataRow){
              delete items._XID;
              Object.keys(item).forEach(key=>{
                Object.keys(items).forEach(keys=>{
                  if (key === keys && keys!== 'dataRow'){
                    schemaValuesList.push({
                      schemaName: keys,
                      schemaValue: items[keys],
                      schemaValueId: item[key],
                      dataRow: items.dataRow,
                      delete: true
                    })
                  }
                })
              })
            }
          })
        })
      }


      let data={
        testDataId: this.schemaId,
      }
      schemaValuesList.forEach((item, index)=>{
        for (const key in item) {
          data['schemaValuesList['+index+'].'+key] = item[key];
        }
      });

      this.$axios
          .post("/testData/saveOrUpdateTestDataSchemaValues", this.$qs.stringify(data))
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              this.$Modal.success({
                title: "πFlow system tips",
                content: data.errorMsg,
                onOk: () => {
                  this.fatherMethod();
                },
              });

            } else {
              this.$Message.error({
                content: data.errorMsg,
                duration: 3
              });
              if (this.fatherMethod) {
                this.fatherMethod();
              }

            }
          })
          .catch(error => {
            console.log(error);
          })

    },
  }
}
</script>

<style lang="scss" scoped>
::v-deep .vxe-toolbar{
  position: relative;
  .vxe-buttons--wrapper{
    position: absolute;
    right: 0;
  }
  .vxe-tools--operate{
    position: absolute;
    right: 100px;
  }
}



/*滚动条整体部分*/
.mytable-scrollbar ::-webkit-scrollbar {
  width: 10px;
  height: 10px;
}
/*滚动条的轨道*/
.mytable-scrollbar ::-webkit-scrollbar-track {
  background-color: #FFFFFF;
}
/*滚动条里面的小方块，能向上向下移动*/
.mytable-scrollbar ::-webkit-scrollbar-thumb {
  background-color: #bfbfbf;
  border-radius: 5px;
  border: 1px solid #F1F1F1;
  box-shadow: inset 0 0 6px rgba(0,0,0,.3);
}
.mytable-scrollbar ::-webkit-scrollbar-thumb:hover {
  background-color: #A8A8A8;
}
.mytable-scrollbar ::-webkit-scrollbar-thumb:active {
  background-color: #787878;
}
/*边角，即两个滚动条的交汇处*/
.mytable-scrollbar ::-webkit-scrollbar-corner {
  background-color: #FFFFFF;
}

</style>