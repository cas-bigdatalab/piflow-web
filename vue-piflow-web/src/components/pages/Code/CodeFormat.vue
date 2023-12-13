<template>
    <div :class="isFocus? 'w-100 code-editor ace_editor ace-clouds Focus': 'w-100 code-editor ace_editor ace-clouds blur'" :ref="generateId"></div>
</template>

<script>
// 引入全局实例
import ace from 'ace-builds'
// 主题风格，引入主题后还需要在 options 中指定主题才会生效
// import 'ace-builds/src-min-noconflict/theme-monokai'
// import 'ace-builds/src-min-noconflict/theme-dracula'
import 'ace-builds/src-min-noconflict/theme-clouds'
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
      default: 'scala'
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

    // 当前信息
    editorInfo: {
      type: Object,
      default: ()=>({})
    },
    isNew: {
      type: Boolean,
      default: false
    },

  },
  data() {
    return {
      editor: null,
      generateId:
          'id_' +
          Math.random()
              .toString(36)
              .substr(2, 4),

      isFocus: false
    }
  },
  mounted() {
    // 初始化
    this.initEditor()
  },
  watch: {
    value(val) {
      if (this.editor.getValue() !== val) {
        this.editor.setValue(val);
        this.editor.clearSelection();
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
        maxLines: 30,
        minLines: 3,
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


      this.editor.setHighlightGutterLine(false);
      this.editor.setHighlightActiveLine(false);
      this.editor.setShowPrintMargin(false);
      this.editor.navigateFileEnd();
      this.editor.renderer.setShowGutter(false)
      this.editor.renderer.scrollToX(-6)
      if (this.isNew){
        this.isFocus = true;
        this.editor.focus();
      }

      // 设置值改变
      this.editor.on('change', () => {
        this.editorInfo.codeContent = this.editor.getValue();
        this.editorInfo.state = 'change';

        let data= {
          data: this.editorInfo,
          isFocus: this.isFocus
        };

        this.$emit('func',data)
      });


      this.editor.on("focus", ()=>{
        this.editorInfo.state = '';

        let data= {
          data: this.editorInfo,
          isFocus: true
        };
        this.$emit('func', data);
        this.isFocus = true;
      })


      this.editor.on("blur", ()=>{
        this.editorInfo.state = '';

        let data= {
          data: this.editorInfo,
          isFocus: false
        };
        this.$emit('func', data)
        this.isFocus = false;
      })


      this.editor.on("changeSelectionStyle", ()=>{
        //  this.editor.setSelectionStyle
      })

    },

    // 实例方法，高亮某一行
    gotoLine(lineNumber) {
      this.editor.gotoLine(lineNumber)
    },

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
    height: 100%;
  }

}
.Focus{
  box-shadow: var(--button-color) 0px 0px 3px 1px;
}
.blur{
  background: #F5F5F5;
  border: 1px solid #E0E0E0;
}
::v-deep .ace_hidden-cursors .ace_cursor{
  opacity: 0;
}
</style>