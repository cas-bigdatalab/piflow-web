<template>
    <Modal v-model="open" title="配置横纵轴" footer-hide>
      <Form
        ref="formCustom"
        :model="formCustom"
        :rules="ruleCustom"
        :label-width="80"
      >
        <FormItem
          label="横轴"
          prop="x"
        >
         <Select v-model="formCustom.x">
            <Option v-for="item in tableData" :value="item.label" :key="item.label">{{ item.label }}</Option>
         </Select>
        </FormItem>
  
        <FormItem
          label="纵轴"
          prop="y"
        >
        <Select
        :transfer="true"
        v-model="formCustom.y">
            <Option v-for="item in tableData" :value="item.label" :key="item.label">{{ item.label }}</Option>
         </Select>
        </FormItem>
  
  
        <FormItem>
          <div style="text-align: right;">
            <Button @click="handleClose()">取消</Button>
            <Button
              type="primary"
              @click="handleSubmit()"
              style="margin-left: 8px"
              >确认</Button
            >
          </div>
        </FormItem>
      </Form>
    </Modal>
  </template>
  
  <script>

  export default {
    data() {
      return {
        open: false,
        tableData:[],
        formCustom: {
          x: "",
          y: "",
        },
        ruleCustom: {
          x: [
            { required: true, message: "请选择横轴维度", trigger: "change" },
          ],
          y: [
            { required: true, message: "请选择纵轴维度", trigger: "change" },
          ],
        },
      };
    },
    methods: {
      handleOpen(tableData){
        this.tableData = tableData
        this.open  = true
        this.formCustom = {
          x:"",
          y:"",
        }
      },
      handleUpdate(){
        this.$emit('submit',{
            xAxisType:{label:this.formCustom.x},
            yAxisType:[{
                label:this.formCustom.y,
                color:'#7cb5ec'
            }],
        })
        this.open  = false
      },
      handleSubmit(name) {
        this.$refs['formCustom'].validate((valid) => {
          if (valid) {
            this.handleUpdate();
          } else {
            this.$Message.error("Fail!");
          }
        });
      },
  
      handleClose() {
          this.open = false
      },
  
    },
  };
  </script>
  
  <style lang="scss" scoped></style>
  