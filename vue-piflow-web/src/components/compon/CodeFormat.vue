<template>
  <div class="wrap h-100">
    <div class="w-100 code-editor" :ref="generateId"></div>
  </div>
</template>

<script>
// 引入全局实例
import ace from 'ace-builds'
// 主题风格，引入主题后还需要在 options 中指定主题才会生效
import 'ace-builds/src-min-noconflict/theme-monokai'
import 'ace-builds/src-min-noconflict/theme-dracula'
// 支持代码格式， 需要引入具体的语法高亮库才会有对应的语法高亮效果
import 'ace-builds/src-min-noconflict/mode-javascript'
import 'ace-builds/src-min-noconflict/mode-text'
import 'ace-builds/src-min-noconflict/mode-scala'
import 'ace-builds/src-min-noconflict/mode-sh'
import 'ace-builds/src-min-noconflict/mode-python'
import 'ace-builds/src-min-noconflict/mode-java'
import 'ace-builds/src-min-noconflict/mode-sql'
import 'ace-builds/src-min-noconflict/mode-json'
import 'ace-builds/src-min-noconflict/mode-css'
import 'ace-builds/src-min-noconflict/ext-language_tools'
import 'ace-builds/src-min-noconflict/ext-searchbox'
import jsWorkerUrl from 'file-loader!ace-builds/src-noconflict/worker-javascript'
import textWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-text'
import scalaWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-scala'
import shWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-sh'
import pythonWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-python'
import javaWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-java'
import sqlWorkerUrl from 'file-loader!ace-builds/src-noconflict/mode-sql'

import jsonWorkerUrl from 'file-loader!ace-builds/src-noconflict/worker-json'
import cssWorkerUrl from 'file-loader!ace-builds/src-noconflict/worker-css'
ace.config.setModuleUrl('ace/mode/javascript_worker', jsWorkerUrl)
ace.config.setModuleUrl('ace/mode/text_worker', textWorkerUrl)
ace.config.setModuleUrl('ace/mode/scala_worker', scalaWorkerUrl)
ace.config.setModuleUrl('ace/mode/sh_worker', shWorkerUrl)
ace.config.setModuleUrl('ace/mode/python_worker', pythonWorkerUrl)
ace.config.setModuleUrl('ace/mode/java_worker', javaWorkerUrl)
ace.config.setModuleUrl('ace/mode/sql_worker', sqlWorkerUrl)
ace.config.setModuleUrl('ace/mode/json_worker', jsonWorkerUrl)
ace.config.setModuleUrl('ace/mode/css_worker', cssWorkerUrl)
ace.config.setModuleUrl(
    'ace/snippets/javascript',
    require('file-loader!ace-builds/src-noconflict/snippets/javascript.js')
)
ace.config.setModuleUrl(
    'ace/snippets/text',
    require('file-loader!ace-builds/src-noconflict/snippets/text.js')
)
ace.config.setModuleUrl(
    'ace/snippets/java',
    require('file-loader!ace-builds/src-noconflict/snippets/java.js')
)
ace.config.setModuleUrl(
    'ace/snippets/scala',
    require('file-loader!ace-builds/src-noconflict/snippets/scala.js')
)
ace.config.setModuleUrl(
    'ace/snippets/sql',
    require('file-loader!ace-builds/src-noconflict/snippets/sql.js')
)
ace.config.setModuleUrl(
    'ace/snippets/sh',
    require('file-loader!ace-builds/src-noconflict/snippets/sh.js')
)
ace.config.setModuleUrl(
    'ace/snippets/python',
    require('file-loader!ace-builds/src-noconflict/snippets/python.js')
)
ace.config.setModuleUrl('ace/snippets/css', require('file-loader!ace-builds/src-noconflict/snippets/css.js'))
export default {
  name: "CodeFormat",
  model: {
    event: 'change'
  },
  props: {
    // 编辑器内容
    value: String,
    // 默认语言
    language: {
      type: String,
      default: 'javascript'
    },
    // 主题，对应主题库 JS 需要提前引入
    theme: {
      type: String,
      default: 'monokai'
    },
    // 是否只读
    readonly: {
      type: Boolean,
      default: false
    },
    // 最大行数
    maxLines: Number,
    // 是否显示全屏按钮
    withFullscreenBtn: {
      type: Boolean,
      default: false
    },
    withFooterBtns: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      editor: null,
      generateId:
          'id_' +
          Math.random()
              .toString(36)
              .substr(2, 4),
      isVisible: false,
      dialogValue: ''
    }
  },
  mounted() {
    // 初始化
    this.initEditor()
  },
  watch: {
    value(val) {
      if (this.editor.getValue() !== val) {
        this.editor.setValue(val)
        this.editor.clearSelection()
      }
    },
    language(newVal){
      if (newVal){
        this.$nextTick(()=>{
          this.destroy();
          this.initEditor()
        })
      }
    }
  },
  methods: {
    // 初始化
    initEditor() {
      // 创建实例
      this.editor = ace.edit(this.$refs[this.generateId], {
        mode: `ace/mode/${this.language}`,
        theme: `ace/theme/${this.theme}`,
        fontSize: 14,
        tabSize: 2,
        value: this.value,
        selectionStyle: 'text',
        // maxLines: 30,
        // minLines: 20,
        readOnly: this.readonly,
        resize: true
      })
      // 设置属性等，具体需要可根据官方参数自行设置
      this.editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true,
        wrap: true,
        // setShowPrintMargin: false
      })
      this.editor.find('needle',{
        backwards: false,
        wrap: false,
        caseSensitive: false,
        wholeWord: false,
        regExp: false
      },true);
      this.editor.findNext();
      this.editor.findPrevious();
      this.editor.find('foo');
      this.editor.replace('bar');
      this.editor.replaceAll('bar');
      // 设置值改变监听
      this.editor.on('change', () => {
        this.$emit('change', this.editor.getValue())
      })
    },
    // 实例方法，高亮某一行
    gotoLine(lineNumber) {
      this.editor.gotoLine(lineNumber)
    },
    // // 全屏编辑
    // fullscreen() {
    //   // this.dialogValue = cloneDeep(this.editor.getValue())
    //   this.isVisible = true
    // },
    // closeEditCode() {
    //   this.editor.setValue(this.dialogValue)
    //   this.editor.clearSelection()
    // },
    // resize编辑器
    resize() {
      this.editor.resize(true)
    },
    destroy() {
      if (this.editor) {
        this.editor.destroy()
        this.editor = null
      }
    }
  },
  beforeDestroy() {
    this.destroy()
  }
}
</script>

<style lang="scss" scoped>
.wrap {
  position: relative;
  height: 100%;
  .code-editor {
    min-height: 200px;
    height: 100%;
    border: 1px solid #282f3a;
    background-color: #0e1013;
  }
  .icon-fullscreen {
    position: absolute;
    // color: #fff;
    right: 10px;
    font-size: 16px;
    z-index: 9999;
    cursor: pointer;
  }
}
::v-deep .code-dialog {
  &::before {
    content: '';
    position: absolute;
    display: block;
    top: 0;
    left: 0;
    right: 0;
    width: 100%;
    height: 2px;
    background-image: linear-gradient(270deg, #00deff, #2483ff 74%);
  }
  display: flex;
  flex-direction: column;
  background-color: #303640;
  .el-dialog__header {
    border: none;
    .el-dialog__title {
      color: #ccc;
    }
  }
  .el-dialog__body {
    flex: 1 1 0;
    padding-top: 10px;
  }
}
</style>