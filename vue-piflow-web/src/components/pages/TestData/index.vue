<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.testData")}}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-add" />
        </span>
      </div>
    </div>
    <!-- search -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"/>
    </div>
    <!-- Table button -->
    <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip v-for="(item, index) in promptContent" :key="index" :content="item.content" placement="top-start">
            <span class="button-warp" @click="handleButtonSelect(row,index+1)">
              <Icon :type="item.icon" />
            </span>
        </Tooltip>
      </template>
    </Table>
    <!-- paging -->
    <div class="page">
      <Page
        :prev-text="$t('page.prev_text')"
        :next-text="$t('page.next_text')"
        show-elevator
        :show-total="true"
        :total="total"
        show-sizer
        @on-change="onPageChange"
        @on-page-size-change="onPageSizeChange"/>
    </div>
    <!-- add / update -->
    <Modal
      v-model="isOpen"
      :mask-closable="false"
      :title="id?$t('testData_columns.update_title'):$t('testData_columns.create_title')"
      :ok-text="$t('modal.ok_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-cancel="handleCancelData">
      <div v-if="whatStage === 'init'" class="modal-warp">
        <Form ref="formCustom" :model="formCustom" :rules="ruleCustom" :label-colon="true" :label-width="80">
          <FormItem :label="$t('testData_columns.testData_name')" prop="name">
            <Input
                v-model="formCustom.name"
                :placeholder="$t('modal.placeholder')"
                style="width: 350px"/>
          </FormItem>
          <FormItem :label="$t('testData_columns.description')">
            <Input
                v-model="formCustom.description"
                type="textarea"
                :rows="4"
                :placeholder="$t('modal.placeholder')"
                style="width: 350px"/>
          </FormItem>
          <FormItem :label="$t('testData_columns.methed')">
            <Select v-model="Methed" style="width:350px">
              <Option value="Manual">Manual</Option>
              <Option value="csvImport">Csv Import</Option>
            </Select>
          </FormItem>
        </Form>
      </div>
      <div v-if="whatStage === 'Schema'">
        <div v-if="id && saveType === 'edit'" class="updataModal-warp">
          <Form ref="formCustom" :model="formCustom" :label-colon="true" :label-width="80">
            <FormItem :label="$t('testData_columns.testData_name')">
              <Input
                  v-model="formCustom.name"
                  :placeholder="$t('modal.placeholder')"
                  style="width: 350px" />
            </FormItem>
            <FormItem :label="$t('testData_columns.description')">
              <Input
                  v-model="formCustom.description"
                  type="textarea"
                  :rows="4"
                  :placeholder="$t('modal.placeholder')"
                  style="width: 350px"/>
            </FormItem>
          </Form>
        </div>
        <div class="item">
          <label class="self" style="margin-top:15px;">Schema：</label>
          <ul class="relationship">
            <li v-for="(item,m) in schemaVoList" :key="'ve'+m">
              <Input
                  v-model="item.fieldName"
                  show-word-limit
                  maxlength="100"
                  :placeholder="$t('modal.placeholder')"
                  style="width: 170px"/>

              <Select class="select_type" v-model="item.fieldType" filterable allow-create @on-create="handleCreate">
                <Option v-for="item in typeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>

              <Icon
                  @click="handleRemove(m,schemaVoList.length===1)"
                  type="ios-remove-circle-outline"/>
              <Icon
                  v-if="m==(schemaVoList.length-1)"
                  @click="handleAdd"
                  type="ios-add-circle-outline"/>
            </li>
          </ul>
        </div>
      </div>
      <div v-if="whatStage === 'SchemaVal'">
        <editableForm ref="editable" :editableData="editableData" :editableDataId="editableDataId" :fatherMethod="getTableData" :tableColumn="tableColumn" :schemaId="schemaId"></editableForm>
      </div>
      <div v-if="whatStage === 'csvImport'">
        <Form ref="ImportData" :model="ImportData" :rules="ruleCustom" :label-colon="true" :label-width="80">
          <FormItem :label="$t('testData_columns.header')">
            <Select v-model="ImportData.header" style="width:350px">
              <Option value="true" key="vm">true</Option>
              <Option value="false" key="vmq">false</Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('testData_columns.delimiter')">
            <Input
                v-model="ImportData.delimiter"
                :placeholder="$t('modal.placeholder')"
                style="width: 350px" />
          </FormItem>
          <FormItem v-show="ImportData.header==='false'" :label="$t('testData_columns.schema')">
            <Input
                v-model="ImportData.schema"
                :placeholder="$t('modal.schema')"
                style="width: 350px" />
          </FormItem>
        </Form>
        <Upload
            :action= "this.$url + '/testData/uploadCsvFile'"
            :data="uploadData"
            :headers="{'Authorization': token}"
            style="width:94%;margin: auto"
            ref="upload"
            :show-upload-list="true"
            :on-success="handleSuccess"
            :on-error="handleError"
            :format="['csv']"
            :before-upload="handleBeforeUpload"
            type="drag">
          <div style="padding: 20px 0; height: 120px">
            <div>
              <Icon type="ios-cloud-upload" size="52" style="color: var(--primary-color)"></Icon>
              <p>{{$t("testData_columns.fileDescription")}}</p>
              <p style="font-size: 12px">{{$t("testData_columns.uploadPrompt")}}</p >
            </div>
          </div>
        </Upload>
        <div v-if="file !== null">
          <Icon :color="JarIsShow === false?'red':''" :type="JarIsShow === false?'md-close-circle':''" />
          Upload file: {{ file.name }}
        </div>

      </div>
      <div slot="footer">
        <div v-if="whatStage === 'init'">
          <Button type="primary" @click="handleSubmit('formCustom')">下一步</Button>
        </div>

        <div v-if="id && whatStage === 'Schema' && saveType === 'edit'">
          <Button type="primary" @click="editSchema">完成</Button>
        </div>
        <div v-else-if="whatStage === 'Schema'">
          <Button type="primary" @click="delInit">上一步</Button>
          <Button type="primary" @click="createSchemaVal">下一步</Button>
        </div>

        <div v-if="id && whatStage === 'SchemaVal' && saveType === 'edit'">
          <Button type="primary" @click="editSchemaVal">完成</Button>
        </div>
        <div v-else-if="whatStage === 'SchemaVal'">
          <Button type="primary" @click="whatStage= 'Schema'">上一步</Button>
          <Button type="primary" @click="saveSchemaValues">完成</Button>
        </div>

        <div v-if="whatStage === 'csvImport'">
          <Button type="primary" @click="delInit">上一步</Button>
          <Button type="primary" @click="uploadSuccess">完成</Button>
        </div>
      </div>
    </Modal>
  </section>
</template>

<script>
import editableForm from './modules/EditableForm';
import Cookies from "js-cookie";
export default {
  name: "TestData",
  components: {
    editableForm
  },
  data() {
    const validateName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('TestDataName cannot be empty'));
      }
      this.$axios
          .post("/testData/checkTestDataName", this.$qs.stringify({testDataName:value}))
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              callback();
            } else {
              callback(new Error(data.errorMsg));
            }
          })
          .catch(error => {
            console.log(error);
          });
    }
    return {
      isOpen: false,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],

      param: "",
      templateName: "",

      id: "",
      name: "",
      description: "",

      whatStage: 'init',
      schemaVoList: [
        {
          fieldName: "",
          fieldType: "",
          id: ''
        }
      ],
      editableData: [],
      editableDataId: [],
      schemaId: '',
      tableColumn: [
        { type: 'checkbox', width: 40 },
      ],

      formCustom:{
        name: '',
        description: ''
      },
      Methed: 'Manual',

      ImportData: {
        header: 'true',
        delimiter: '',
        schema: '',
      },
      file: null,
      JarIsShow: null,
      token: '',
      uploadData: {},

      ruleCustom: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ]
      },

      typeList: [
        {
          value: 'String',
          label: 'String'
        },
        {
          value: 'Int',
          label: 'Int'
        },
        {
          value: 'Float',
          label: 'Float'
        },
        {
          value: 'Boolean',
          label: 'Boolean'
        },
        {
          value: 'Date',
          label: 'Date'
        },
        {
          value: 'Char',
          label: 'Char'
        },
        {
          value: 'Double',
          label: 'Double'
        },
        {
          value: 'Byte',
          label: 'Byte'
        },
        {
          value: 'Short',
          label: 'Short'
        },
        {
          value: 'Long',
          label: 'Long'
        }
      ],

      saveType: '',

      promptContent: [
        {
          content: 'Edit Schema',
          icon: 'ios-brush-outline'
        },{
          content: 'Edit SchemaValue',
          icon: 'ios-create-outline'
        },{
          content: 'Delete',
          icon: 'ios-trash'
        }
      ]

    };
  },
  watch: {
    isOpen(state) {
      if (!state) {
        this.handleReset();
      }
    },

    param() {
      this.page = 1;
      this.limit = 10;
      this.getTableData();
    }
  },
  computed: {
    columns() {
      return [
        {
          title: this.$t("testData_columns.name"),
          key: "name",
          sortable: true
        },
        {
          title: this.$t("testData_columns.description"),
          key: "description"
        },
        {
          title: this.$t("testData_columns.CreateTime"),
          key: "crtDttm",
          sortable: true
        },
        {
          title: this.$t("testData_columns.action"),
          slot: "action",
          width: 200,
          align: "center"
        }
      ];
    }
  },
  created() {
    this.getTableData();
  },
  mounted() {
    let token = this.$store.state.variable.token;
    if (!token) {
      token = `${Cookies.get('token')}`
    }
    this.token = `Bearer ${token}`;
  },
  methods: {
    handleReset() {
      this.page = 1;
      this.limit = 10;
      this.id = "";
      this.name = "";
      this.description = "";
      this.whatStage = 'init';
      this.schemaVoList= [
        {
          fieldName: "",
          fieldType: "",
          id: ''
        }
      ];
      this.formCustom = {
        name: "",
        description: ""
      };
      this.ImportData= {
        header: 'true',
        delimiter: '',
        schema: '',
      };
      this.Methed= 'Manual';
      this.$refs.formCustom.resetFields();
    },

    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleEditSchemaPage(row);
          break;
        case 2:
          this.handleEditSchemaValPage(row);
          break;
        case 3:
          this.handleDeleteRow(row);
          break;

        default:
          break;
      }
    },

    handleCancelData(){
      if (this.id && this.saveType === 'edit'){


      }else {
        let data = {
          testDataId: this.id,
        };
        this.$axios
            .post("/testData/delTestData", this.$qs.stringify(data))
            .then(res => {
              if (res.data.code === 200) {
                this.handleReset();
                this.getTableData();
              }
            })
            .catch(error => {
              console.log(error);
            });
      }
    },

    handleDeleteRow(row) {
      this.$Modal.confirm({
        title: this.$t("tip.title"),
        okText: this.$t("modal.confirm"),
        cancelText: this.$t("modal.cancel_text"),
        content: `${this.$t("modal.delete_content")} ${row.name}?`,
        onOk: () => {
          let data = {
            testDataId: row.id
          };
          this.$axios
            .post("/testData/delTestData", this.$qs.stringify(data))
            .then(res => {
              if (res.data.code === 200) {
                this.handleReset();
                this.getTableData();
                this.$Modal.success({
                  title: this.$t("tip.title"),
                  content:
                    `${row.name} ` + this.$t("tip.delete_success_content")
                });
              } else {
                this.$Message.error({
                  content: this.$t("tip.delete_fail_content"),
                  duration: 3
                });
              }
            })
            .catch(error => {
              console.log(error);
              this.$Message.error({
                content: this.$t("tip.fault_content"),
                duration: 3
              });
            });
        }
      })
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .post("/testData/testDataListPage", this.$qs.stringify(data))
        .then(res => {
          if (res.data.code === 200) {
            this.tableData = res.data.data;
            this.total = res.data.count;
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3
            });
          }
        })
        .catch(error => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3
          });
        });
    },

    onPageChange(pageNo) {
      this.page = pageNo;
      this.getTableData();
    },

    onPageSizeChange(pageSize) {
      this.limit = pageSize;
      this.getTableData();
    },

    handleModalSwitch() {
      this.isOpen = !this.isOpen;
    },

    handleAdd() {
      this.schemaVoList.push({
        fieldName: "",
        fieldType: ""
      });
    },

    handleRemove(m, mark) {
      if (mark) {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content: "此项不可删除，请重新操作！"
        });
        return;
      }
      this.schemaVoList.splice(m, 1);
    },

    SaveUpdateData(){
      this.whatStage= 'Schema';
      let data = this.formCustom;
      this.$axios
          .post("/testData/saveOrUpdateTestDataSchema", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              this.id= data.testDataId;
            }
          })
          .catch((error) => {
            console.log(error);
            this.$event.emit("loading", false);
          })
    },

    createSchema(){
      let data = this.formCustom, id='';
      if (this.id){
        data.id = this.id;
        id = this.id;
      }

      if (
          this.schemaVoList[0].fieldName &&
          this.schemaVoList[0].fieldType
      ) {
        this.schemaVoList.forEach((item, i) => {
          data[`schemaVoList[${i}].fieldName`] = item.fieldName;
          data[`schemaVoList[${i}].fieldType`] = item.fieldType;
          data[`schemaVoList[${i}].fieldSoft`] = i+1;
          data[`schemaVoList[${i}].id`] = item.id;
        });
      }

      this.$axios
          .post("/testData/saveOrUpdateTestDataSchema", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){
              if (id && this.saveType === 'edit'){
                this.getTableData();
              }else {
                this.handleEditSchema(data.testDataId);
              }
            }

          })
          .catch((error) => {
            console.log(error);
            this.$event.emit("loading", false);
          })

    },

    createSchemaVal(){
      this.whatStage = 'SchemaVal';
      this.createSchema();

    },

    delInit(){
      this.whatStage= 'init';
      let data = {
        testDataId: this.id,
      };
      this.$axios
          .post("/testData/delTestData", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    handleEditSchema(id){
      let data = { testDataId: id };
      this.schemaId = id;
      this.$axios
          .post("/testData/testDataSchemaValuesList", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              this.editableData = [];
              this.editableDataId = [];
              this.getTitle(res.data.schema, 'create');


            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
          });
    },

    handleEditSchemaPage(row){
      this.isOpen= true;
      this.whatStage= 'Schema';
      let data = { page: this.page, limit: this.limit,testDataId: row.id };
      this.$axios
          .post("/testData/testDataSchemaListPage", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data;
              this.id = data.testData.id;
              this.saveType = 'edit';
              this.formCustom.name = data.testData.name;
              this.formCustom.description = data.testData.description;
              if (data.data.length === 0){
                this.schemaVoList= [
                  {
                    fieldName: "",
                    fieldType: "",
                    id: ''
                  }
                ];
              }else {
                this.schemaVoList = data.data;
              }
            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    },

    handleEditSchemaValPage(row){
      this.isOpen= true;
      this.whatStage = 'SchemaVal'
       let data = { testDataId: row.id };
      this.id = row.id;
      this.saveType = 'edit';
      this.schemaId = this.id;
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .post("/testData/testDataSchemaValuesList", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data, result= {}, resultList= [];
              if (data.schemaValue.length!==0){
                data.schema.forEach((item)=>{
                  for (let key in item){
                    if (key === 'FIELD_NAME'){
                      result[item[key]] = '';
                    }
                  }
                })
                resultList.push(result);
                let schemaValList = Object.keys(data.schemaValue[0]);
                let schema = Object.keys(resultList[0]);
                function exist(num, arr1) {
                  for (var j = 0; j < arr1.length; j++) {
                    if (num === arr1[j]) {
                      return false;
                    }
                  }
                  return true; //如果不能找到相匹配的元素，返回true
                }

                let newArr = [];
                for (var i = 0; i < schema.length; i++) {
                  if (exist(schema[i], schemaValList)) {
                    newArr[newArr.length] = schema[i];
                  }
                }
                data.schemaValue.forEach((item)=>{
                  newArr.forEach((items)=>{
                    item[items]= ''
                  })
                })

                this.editableData = data.schemaValue;
                this.editableDataId = data.schemaValueId;
                this.getTitle(data.schemaValue, 'edit');
              }else {
                this.editableData = [];
                this.editableDataId = [];
                this.getTitle(res.data.schema, 'create');
              }


            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    },

    handleEditSchemaVal(id){
      this.isOpen= true;
      this.whatStage = 'SchemaVal'
      let data = { testDataId: id };
      this.id = id;
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
          .post("/testData/testDataSchemaValuesList", this.$qs.stringify(data))
          .then(res => {
            if (res.data.code === 200) {
              let data = res.data;
              this.editableData = data.schemaValue;
              this.editableDataId = data.schemaValueId;
              this.getTitle(data.schemaValue, 'edit');

            } else {
              this.$Message.error({
                content: this.$t("tip.request_fail_content"),
                duration: 3
              });
            }
          })
          .catch(error => {
            console.log(error);
            this.$Message.error({
              content: this.$t("tip.fault_content"),
              duration: 3
            });
          });
    },

    saveSchemaValues(){
      this.isOpen=false;
      this.$refs.editable.saveSchemaValues();
    },

    getTitle(schemaValuesList, type) {
      let list = [], result = {};
      this.tableColumn= [
        { type: 'checkbox', width: 40 },
      ];
      if (type === 'create'){
        schemaValuesList.forEach((item)=>{
          for (let key in item){
            if (key === 'FIELD_NAME'){
              result[item[key]] = '';
            }
          }
        })
        list.push(result)
      }else {
        list = schemaValuesList;
      }

      let tableTitle = Object.keys(list[0]);
      for (let i=0;i<tableTitle.length;i++){
        if (tableTitle[i]==='dataRow'){
        }else {
          this.tableColumn.push({
            field: tableTitle[i],
            title: tableTitle[i],
            editRender: { name: 'input', defaultValue: '' }
          })
        }
      }
    },

    handleSubmit (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          if (this.Methed === 'Manual'){
            this.SaveUpdateData();
            // this.createSchemaVal();
          }else if (this.Methed === 'csvImport'){
            this.whatStage= 'csvImport';
            let data = this.formCustom;
            this.$axios
                .post("/testData/saveOrUpdateTestDataSchema", this.$qs.stringify(data))
                .then((res) => {
                  let data = res.data;
                  if (data.code === 200){
                    this.id= data.testDataId;
                  }
                })
                .catch((error) => {
                  console.log(error);
                  this.$event.emit("loading", false);
                })
          }
        }
      })
    },

    handleResetss (name) {
      this.$refs.formCustom.resetFields();
    },

    handleCreate (val) {
      this.typeList.push({
        value: val,
        label: val
      });
    },

    editSchema(){
      this.isOpen=false;
      this.createSchema();
    },

    editSchemaVal(){
      this.isOpen=false;
      this.$refs.editable.saveSchemaValues();
    },

    handleSuccess (res, file) {
      this.file = null;
      this.getTableData();
      setTimeout(()=>{
        this.isOpen = false;
      },1000)
    },

    handleError ( error, file) {
      this.JarIsShow = false;
    },

    handleBeforeUpload (file) {
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension =
          testmsg === "csv";
      const isLt500M = file.size / 1024 / 1024 < 500;
      if (!extension) {
        this.$Notice.warning({
          title: 'The file format is incorrect',
          desc: 'File format of ' + file.name + ' is incorrect, please select csv.'
        });
        return false;
      }
      if (!isLt500M) {
        this.$Notice.warning({
          title: 'Exceeding file size limit',
          desc: 'File  ' + file.name + ' is too large, no more than 500M.'
        });
        return false;
      } else
        this.file = file;

      return false;
    },

    uploadSuccess(){
      let data = {
        testDataId: this.id,
        header: this.ImportData.header,
        delimiter: this.ImportData.delimiter,
        schema: this.ImportData.schema,
      }
      this.uploadData = data;
      this.$set(this.$refs.upload.data, 'testDataId', this.id);  //更新参数
      this.$set(this.$refs.upload.data, 'header', this.ImportData.header);  //更新参数
      this.$set(this.$refs.upload.data, 'delimiter', this.ImportData.delimiter);
      this.$set(this.$refs.upload.data, 'schema', this.ImportData.schema);
      this.isOpen = true;

      this.$refs.upload.post(this.file);
    },

    uploadError(){
      this.isOpen = false;
      this.$refs.upload.clearFiles();
    }
  }
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
.relationship {
  li > div {
    margin-right: 10px;
  }
  i {
    margin-right: 10px;
    font-size: 18px;
  }
  li {
    margin: 10px 0;
  }
}
.item {
  display: flex;
  label{
    display: inline-block;
    width: 120px;
    text-align: right;
  }
}
.updataModal-warp{
  .item{
    margin: 12px 0;
  }
}
::v-deep .ivu-form .ivu-form-item-label{
  width: 120px!important;
}
</style>

