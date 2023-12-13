<template>
  <section>
    <div class="NotebookPanel-toolbar">
      <ButtonGroup>
        <Button icon="md-add" @click="handleAdd"></Button>
        <Button icon="md-remove" @click="handleRemove('m',EditorList.length===1)"></Button>
        <Button icon="md-play" @click="runInfo"></Button>
      </ButtonGroup>
    </div>

    <ul class="relationship">
      <li v-for="(item,m) in EditorList" :key="'ve'+m">
        <div :class="highlighted.index === m+1? 'prefix-identification': ''"></div>
        <div style="margin: 10px 0">
          <div class="InputArea-prompt" :style="highlighted.isFocus && highlighted.index === m+1? 'color: var(--primary-color)': ''">
            <span style="letter-spacing: 2px;">
              [{{ item.codeSnippetSort }}]:
            </span>
          </div>
          <code-editor
              ref="_firstRefs"
              v-model="item.codeContent"
              :editorInfo="item"
              :readonly="readonly" theme="clouds"
              :isNew="item.isNew?item.isNew:false"
              @func="changeVal">
          </code-editor>
        </div>
        <pre v-if="!!item.newData && item.newData.output.status==='ok'" v-html="item.newData.output.data['text/plain']"></pre>
        <div class="preError" v-if="!!item.newData && item.newData.output.status==='error'">
          <pre><p>---------------------------------------------------------------------------</p>{{item.newData.output['evalue']}}</pre>
        </div>
      </li>
    </ul>
  </section>
</template>

<script>
import CodeEditor from './CodeFormat'
let runId;
let timer = null;
export default {
  name: "codeBlock",
  components:{CodeEditor},
  data() {
    return {
      EditorList: [],
      readonly: false,
      noteBookId: '',
      codeSnippetList: [],
      blurId: '',

      runResultsList: [],
      sourcePath: this.$route.path,

      highlighted: {
        isFocus: null,
        index: 1
      }
    };
  },
  created() {
    this.noteBookId = this.$route.query.id;
  },
  mounted() {
    this.getCodeSnippetList();
  },
  destroyed() {
    if (this.$route.path !==this.sourcePath)
      this.closeSession();

  },
  watch:{
    'EditorList':{
      handler(newVal, oldVal){

        if (newVal.length < oldVal.length)
          oldVal.forEach((item, index)=>{
            if (!!item.id && item.id=== this.blurId)
              if (index+1 >= newVal.length)
                this.highlighted.index = newVal.length;
              else if (index+1 < newVal.length)
                this.highlighted.index =index+1;
          })
        else if (oldVal.length!==0 && newVal.length >= oldVal.length)
          this.highlighted.index = newVal.length;
      },
      deep: true,
    }
  },
  methods: {
    initEditorList(){
      if (this.codeSnippetList.length === 0){
        this.EditorList.push({
          codeSnippetSort: 1,
          codeContent: '',
          isNew: true,
        });
        this.addEditor();
      }else {

        let codeSnippetList = JSON.parse(JSON.stringify(this.codeSnippetList));
        if (this.runResultsList.length !== 0){
          this.runResultsList.forEach(item=>{
            codeSnippetList.forEach(items=>{
              if (item.id === items.id){
                items.newData = item.newData;
              }
            })
          });
          this.EditorList = codeSnippetList;

        }else
          this.EditorList = codeSnippetList;
      }
    },

    handleAdd() {
      this.EditorList.push({
        codeContent: '',
        isNew: true,
      });
      this.addEditor();
    },

    addEditor(){
      let key = this.codeSnippetList.length-1;
      let data = {
        codeContent: '',
        codeSnippetSort: '',
        noteBookId: this.noteBookId,
      };

      if (this.codeSnippetList.length === 0)
        data.codeSnippetSort = '1';
      else
        data.codeSnippetSort = JSON.stringify(this.codeSnippetList[key].codeSnippetSort+1);

      this.$axios
          .post("/codeSnippet/addCodeSnippet", this.$qs.stringify(data))
          .then((res) => {
            if (res.data.code === 200) {
              runId = res.data.codeSnippetId;
              this.getCodeSnippetList();



            } else {
              this.$Message.error({
                content: res.data.errorMsg,
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    changeVal(val){
      this.highlighted.isFocus = val.isFocus;
      this.highlighted.index = val.data.codeSnippetSort;
      if (!! val.data.state && val.data.state === "change")
        this.updateEditor(val.data);
      else
        this.blurId = val.data.id;
    },

    updateEditor(val){
      let data = {
        id: val.id,
        codeContent: val.codeContent,
        codeSnippetSort: val.codeSnippetSort,
        noteBookId: this.noteBookId,
      };
      this.blurId = val.id;

      this.$axios
          .post("/codeSnippet/updateCodeSnippet", this.$qs.stringify(data))
          .then((res) => {

          })
          .catch((error) => {
            console.log(error);
          });
    },

    runInfo(){
      let data = {
        codeSnippetId: this.blurId
      };
      this.$axios
          .post("codeSnippet/runStatements", this.$qs.stringify(data))
          .then((res) => {
            if (res.data.code === 200) {

              timer = setInterval(()=>{
                this.getRunResults();
              },1000);


            } else {
              this.$Message.error({
                content: res.data.errorMsg,
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    getRunResults(){
      let data = {
        codeSnippetId: this.blurId
      };
      this.$axios
          .post("codeSnippet/getStatementsResult", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200) {
              let newData = JSON.parse(data.data);

              if (!!newData.output && newData.output.status==='ok' || !!newData.output && newData.output.status==='error'){
              //  清除定时器
                clearInterval(timer)
                newData.runID = this.blurId;
                this.EditorList.forEach((item)=>{
                  if (item.id === this.blurId){
                    item.newData= newData;
                  }
                })
                let newVal = JSON.parse(JSON.stringify(this.EditorList));
                this.EditorList = newVal;
                this.runResultsList = this.EditorList;
              }


            } else {
              this.$Message.error({
                content: data.errorMsg,
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
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
      this.deleteCodeSnippet();
    },

    getCodeSnippetList(){
      let data = { noteBookId: this.noteBookId };
      this.$axios
          .post("/codeSnippet/codeSnippetList", this.$qs.stringify(data))
          .then(res => {
            let data = res.data;
            if (data.code === 200) {
              this.codeSnippetList = data.codeSnippetList;
              this.initEditorList();
            }
          })
          .catch(error => {
            console.log(error);
          });

    },

    deleteCodeSnippet(){
      let data = {
        codeSnippetId: this.blurId
      };
      this.$axios
          .post("codeSnippet/deleteCodeSnippet", this.$qs.stringify(data))
          .then((res) => {
            if (res.data.code === 200) {
              this.getCodeSnippetList();
            } else {
              this.$Message.error({
                content: res.data.errorMsg,
                duration: 3
              });
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },

    closeSession(){
      let data = {
        noteBookId: this.noteBookId
      }
      this.$axios
          .post("/noteBoot/deleteNoteBookSession", this.$qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code === 200){

            }
          })
          .catch((error) => {
            console.log(error);
          });
    }
  },
};
</script>
<style lang="scss" scoped>
@import "./index.scss";
pre{
  padding: 0 10px 0 70px;
  margin: 0;
}
.preError{
  background: #fdd;
  padding: 5px 0 0 5px;
  margin: 0 0 0 46px;
  pre{
    padding: 0;
    p{
      color: red;
    }
  }
}
.InputArea-prompt{
  color: #616161;
  float: left;
  width: 50px;
  text-align: right;
  padding: 5px;
  margin-left: 8px;
}
</style>