<template>
  <Modal title="Space List" v-model="open" width="40" :mask-closable="false">
    <div>
      <Tree
        :data="spaceList"
        :load-data="loadDataSpaceData"
        @on-check-change="handleSelect"
        :select-node="true"
        show-checkbox
      ></Tree>
    </div>
    <div slot="footer">
      <Button @click="cancel">{{ $t("modal.cancel_text") }}</Button>
      <Button type="primary" @click="changeFilePath">{{
        $t("modal.confirm")
      }}</Button>
    </div>
  </Modal>
</template>

<script>
export default {
  data() {
    return {
      open: false,
      spaceList: [],
      path: "",
      spaceName: "",
    };
  },
  methods: {
    handleDataSourceProperty(dataSourceId) {
      this.open = true;
        this.$axios
          .post(
            "/datasource/getDataSourceInputData",
            this.$qs.stringify({ dataSourceId })
          )
          .then((res) => {
            var dataMap = res.data;
            if (dataMap.code == 200) {
              let dataSourcePropertyMap = {};
              dataMap.dataSourceVo.dataSourcePropertyVoList.forEach((item) => {
                dataSourcePropertyMap[item.name] = item.value;
              });
              this.params = JSON.parse(JSON.stringify(dataSourcePropertyMap))
              this.handleGetSpaceList();
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },
    handleGetSpaceList() {
        this.$axios
          .post("/datasource/getSpaceList", this.params)
          .then((res) => {
        if (res.data.code === 200) {
      let data = res.data.data;
      this.spaceList = data.map((v) => ({
        title: v.spaceName,
        loading: false,
        level: 1,
        id: v.spaceId,
        children: [],
      }));
        }
      })
      .catch((error) => {
        console.log(error);
      });
    },
    loadDataSpaceData(item, callback) {
      if (item.level === 1) {
        this.spaceName = item.title;
        this.spaceId = item.id
      }
        const data = JSON.parse(JSON.stringify(this.params));
        data.dsSpaceId = this.spaceId
        if (item.hash) data.hash = item.hash;
        this.$axios
          .post("/datasource/fileListByDsSpaceId", data)
          .then((res) => {
            if (res.data.code === 200 && res.data.data && res.data.data.files) {
              let obj = {}
              let nodes = res.data.data.files.filter(v=>v.hash !== '0' && v.hash !== item.hash).map((v) => {
                  obj = {
                  title: v.name,
                  level:2,
                  id: v.path || v.name,
                  hash: v.hash,
                }
                if(v.mime === 'directory'){
                  obj.children = []
                  obj.loading = false
                }
                return obj;
              });
              callback(nodes);
            } else {
              callback([]);
            }
          })
          .catch((error) => {
            console.log(error);
          });
    },
    handleSelect(selectedList, node) {
      selectedList.forEach((item) => {
        if (node.nodeKey !== item.nodeKey) {
          item.checked = false;
        }
      });
      this.path = node.id;
    },
    changeFilePath() {
      const dsSpaceNameInput = document
        .getElementById("bariframe")
        .contentWindow.document.getElementsByName("dsSpaceName")[0];
      dsSpaceNameInput.value = this.spaceName;
      this.updateStopsProperty(dsSpaceNameInput.id, this.spaceName);
      const filePathInput = document
        .getElementById("bariframe")
        .contentWindow.document.getElementsByName("dsFilePath")[0];
      filePathInput.value = this.path;
      this.updateStopsProperty(filePathInput.id, this.path);
      this.cancel()
    },

    updateStopsProperty(stopsId, content) {
      let data = {};
      data.id = stopsId;
      data.content = content;
      this.$axios
        .post("/stops/updateStopsOne", this.$qs.stringify(data))
        .then((res) => {})
        .catch((error) => {
          console.log(error);
        });
    },
    cancel() {
      this.spaceList = []
      this.path = ''
      this.spaceName = ''
      this.open = false;
    },
  },
};
</script>

<style lang="scss" scoped>

::v-deep .ivu-modal {
    max-height: 80vh;
  .ivu-modal-header .ivu-modal-header-inner {
    color: #000;
  }
  .ivu-modal-content {
    width: 100%;
    height: 100%;
    max-height: 80vh;
  }
  .ivu-modal-body {
    max-height: calc(80vh - 100px);
    overflow: auto;
  }
}
</style>