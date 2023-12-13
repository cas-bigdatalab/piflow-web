<template>
  <section>
    <!-- header -->
    <div class="navbar">
      <div class="left">
        <span>{{ $t("sidebar.stopHub") }}</span>
      </div>
      <div class="right">
        <span class="button-warp" @click="handleModalSwitch">
          <Icon type="md-cloud-upload" />
        </span>
      </div>
    </div>
    <!-- search -->
    <div class="input">
      <Input
        suffix="ios-search"
        v-model="param"
        :placeholder="$t('modal.placeholder')"
        style="width: 300px"
      />
    </div>
    <!-- Table button -->
    <!-- <Table border :columns="columns" :data="tableData">
      <template slot-scope="{ row }" slot="action">
        <Tooltip
          content="Mount"
          placement="top-start"
          v-if="row.status.text === 'UNMOUNT'"
        >
          <span class="button-warp" @click="handleButtonSelect(row, 1)">
            <Icon type="ios-aperture" />
          </span>
        </Tooltip>
        <Tooltip v-else content="Unmount" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 1)">
            <Icon type="md-aperture" />
          </span>
        </Tooltip>
        <Tooltip content="Publish" placement="top-start">
          <span
            class="button-warp"
            :class="
              (row.status.text === 'MOUNT') & (row.isPublishing === false)
                ? ''
                : 'gray_btn'
            "
            @click="
              (row.status.text === 'MOUNT') & (row.isPublishing === false)
                ? handleButtonSelect(row, 2)
                : ''
            "
          >
            <Icon type="md-paper-plane" />
          </span>
        </Tooltip>
        <Tooltip content="Delete" placement="top-start">
          <span class="button-warp" @click="handleButtonSelect(row, 3)">
            <Icon type="ios-trash" />
          </span>
        </Tooltip>
      </template>
    </Table> -->
    <vxe-table
      border
      resizable
      :tree-config="{ transform: true }"
      @toggle-row-expand="toggleRow"
      :expand-config="{ accordion: true }"
      :data="tableData"
    >
      <vxe-column
        field="jarName"
        :title="$t('python.packageName')"
        sortable
      ></vxe-column>
      <vxe-column
        field="crtDttm"
        :title="$t('StopHub_columns.time')"
        sortable
      ></vxe-column>
      <vxe-column
        field="version"
        :title="$t('python.version')"
        width="120"
        sortable
        tree-node
      ></vxe-column>
      <vxe-column field="jarUrl" :title="$t('python.FileUrl')"></vxe-column>
      <vxe-column
        field="status"
        :title="$t('python.state')"
        sortable
        width="150"
      >
        <template #default="{ row }">
          <span>{{ row.status.text }}</span>
        </template>
      </vxe-column>
      <vxe-column type="expand" width="60">
        <template #content="{ row, rowIndex }">
          <div
            class="scrollbar"
            style="max-height: 330px; overflow: auto; padding: 20px 10px"
          >
            <Row :gutter="20" v-if="childData.length > 0">
              <Col
                :xs="{ span: 24 }"
                :md="{ span: 24 }"
                :lg="{ span: 6 }"
                :xxl="{ span: 6 }"
                v-for="(item, inx) in childData"
                :key="inx"
              >
                <div class="info-warp">
                  <!-- <p class="personName textEllipsis">{{ item.chineseName }}</p> -->
                  <p class="personName textEllipsis"></p>
                  <div>
                    <label class="textEllipsis" style="display: block">
                      <Icon
                        type="ios-paper-outline"
                        style="color: #f1d09d; margin-right: 6px"
                      />
                      {{ $t("python.componentName") }}：{{
                        item.stopName
                      }}</label
                    >
                  </div>
                  <span class="tagNumber">{{ inx + 1 }}</span>
                  <span class="edit" @click="editEvent(item, row)">
                    {{ $t("python.viewDetail") }}</span
                  >
                </div>
              </Col>
              <Col
                span="24"
                style="text-align: center"
                v-if="childData.length === 0"
                >{{ $t("python.noData") }}</Col
              >
            </Row>
          </div>
        </template>
      </vxe-column>
      <vxe-column
        field="action"
        :title="$t('group_columns.action')"
        width="180"
      >
        <template #default="{ row }">
        
          <Tooltip
            content="Mount"
            placement="top-start"
            :transfer="true"
            v-if="row.status.text === 'UNMOUNT'"
          >
            <span class="button-warp" @click="handleButtonSelect(row, 1)">
              <Icon type="ios-aperture" />
            </span>
          </Tooltip>
          <Tooltip
            v-else
            content="Unmount"
            placement="top-start"
            :transfer="true"
          >
            <span class="button-warp" @click="handleButtonSelect(row, 1)">
              <Icon type="md-aperture" />
            </span>
          </Tooltip>
          <Tooltip content="Publish" placement="top-start" :transfer="true">
            <span
              class="button-warp"
              :class="
                (row.status.text === 'MOUNT') & (row.isPublishing === false)
                  ? ''
                  : 'gray_btn'
              "
              @click="
                (row.status.text === 'MOUNT') & (row.isPublishing === false)
                  ? handleButtonSelect(row, 2)
                  : ''
              "
            >
              <Icon type="md-paper-plane" />
            </span>
          </Tooltip>
          <Tooltip content="Delete" placement="top-start" :transfer="true">
            <span class="button-warp" @click="handleButtonSelect(row, 3)">
              <Icon type="ios-trash" />
            </span>
          </Tooltip>
        </template>
      </vxe-column>
    </vxe-table>
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
        @on-page-size-change="onPageSizeChange"
      />
    </div>
    <!-- Upload -->
    <Modal
      v-model="isOpen"
      :loading="loading"
      :title="$t('StopHub_columns.upload')"
      :ok-text="$t('modal.upload_text')"
      :cancel-text="$t('modal.cancel_text')"
      @on-ok="uploadSuccess"
      @on-cancel="uploadError"
    >
      <div class="modal-warp">
        <div class="flex">
          <div>
            <label>{{ $t("python.language") }}</label>
            <Select style="width: 100px" @on-change="changeLanguage">
              <Option
                v-for="(item, i) in language"
                :key="i"
                :value="item.type"
                >{{ item.type }}</Option
              >
            </Select>
          </div>
          <div>
            <label>{{ $t("python.version_lang") }}</label>
            <Select style="width: 100px" v-model="uploadData.languageVersion">
              <Option v-for="(item, i) in versions" :key="i" :value="item">{{
                item
              }}</Option>
            </Select>
          </div>
        </div>
        <div class="item">
          <Upload
            :action="this.$url + '/stops/uploadStopsHubFile'"
            :headers="{ Authorization: token }"
            style="width: 100%"
            ref="upload"
            :data="uploadData"
            :show-upload-list="true"
            :on-success="handleSuccess"
            :on-error="handleError"
            :format="['zip', 'jar']"
            :before-upload="handleBeforeUpload"
            type="drag"
          >
            <div style="padding: 80px 0; height: 240px">
              <div>
                <Icon
                  type="ios-cloud-upload"
                  size="52"
                  style="color: var(--primary-color)"
                ></Icon>
                <p>{{ $t("StopHub_columns.jarDescription") }}</p>
              </div>
            </div>
          </Upload>
        </div>
        <div v-if="file !== null">
          <Icon
            :color="JarIsShow === false ? 'red' : ''"
            :type="JarIsShow === false ? 'md-close-circle' : ''"
          />
          Upload file: {{ file.name }}
        </div>
      </div>
    </Modal>
    <!-- 编辑&保存 PYTHON类型-->
    <vxe-modal
      v-model="showEdit"
      :title="$t('modal.editSave')"
      width="800"
      min-width="600"
      min-height="300"
      :loading="submitLoading"
      resize
      destroy-on-close
      :position="{ top: 100 }"
    >
      <template #default>
        <div class="form-box">
          <vxe-form
            :data="formData"
            :rules="formRules"
            title-align="right"
            title-width="140"
            title-colon
            custom-layout
            @submit="submitEvent"
          >
            <template v-if="componentType == 'PYTHON'">
              <vxe-form-item
                :title="$t('sidebar.basicInfo')"
                title-align="left"
                :title-width="100"
                :span="24"
                :title-prefix="{ icon: 'fa fa-address-card-o' }"
              >
              </vxe-form-item>

              <vxe-form-item
                field="stopBundle"
                title="bundle"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.stopBundle"
                    :placeholder="$t('modal.placeholder')"
                    disabled
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="isPythonComponent"
                :title="$t('python.pythonComponent')"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-switch
                    v-model="data.isPythonComponent"
                    :open-label="$t('dataSource_columns.yes')"
                    :open-value="true"
                    :close-label="$t('dataSource_columns.no')"
                    :close-value="false"
                  ></vxe-switch>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isPythonComponent"
                field="groups"
                title="groups"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.groups"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isPythonComponent"
                field="bundleDescription"
                title="description"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.bundleDescription"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isPythonComponent"
                field="owner"
                title="owner"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.owner"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>

              <vxe-form-item
                v-if="formData.isPythonComponent"
                title="inports"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.inports"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isPythonComponent"
                title="outports"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.outports"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>

              <vxe-form-item
                v-if="formData.isPythonComponent"
                field="isHaveParams"
                :title="$t('python.parameter')"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-switch
                    v-model="data.isHaveParams"
                    :open-label="$t('dataSource_columns.yes')"
                    :open-value="true"
                    :close-label="$t('dataSource_columns.no')"
                    :close-value="false"
                  ></vxe-switch>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isPythonComponent && formData.isHaveParams"
                :span="24"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <template v-for="(item, i) in data.properties">
                    <Row
                      :gutter="16"
                      justify="center"
                      align="middle"
                      class="code-row-bg"
                      style="margin: 8px 0"
                      :key="i"
                    >
                      <Col offset="2" :span="2">
                        <div style="font-weight: bold">{{ i + 1 }}.</div>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.name"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.example"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.description"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="2">
                        <div>
                          <Icon
                            type="ios-add-circle-outline"
                            style="font-size: 20px; cursor: pointer"
                            @click="handleAddProperties"
                          />
                          <Icon
                            type="ios-remove-circle-outline"
                            style="font-size: 20px; cursor: pointer"
                            @click="removeProperties(i)"
                          />
                        </div>
                      </Col>
                    </Row>
                  </template>
                </template>
              </vxe-form-item>

              <vxe-form-item
                field="teamName"
                :title="$t('python.team')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.teamName"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="chineseName"
                :title="$t('python.chineseName')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.chineseName"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="useInstructions"
                :title="$t('python.Instructions')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.useInstructions"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="description"
                :title="$t('python.description')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.description"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="author"
                :title="$t('python.author')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.author"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="authorEmail"
                :title="$t('python.email')"
                :span="12"
                :item-render="{}"
                v-if="formData.isPythonComponent && formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.authorEmail"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                :title="$t('python.logo')"
                title-align="left"
                :title-width="80"
                :span="24"
                :title-prefix="{ icon: 'fa fa-address-card-o' }"
                v-if="formData.isPythonComponent"
              ></vxe-form-item>
              <vxe-form-item
                field="file"
                :title="$t('python.uploadLogo')"
                :span="24"
                :item-render="{}"
                v-if="formData.isPythonComponent"
              >
                <template #default="{ data }">
                  <div class="img_warp">
                    <div
                      class="demo-upload-list"
                      v-for="(item, i) in uploadList"
                      :key="i"
                    >
                      <template>
                        <img :src="item.url" />
                      </template>
                    </div>
                    <Upload
                      :action="$url + '/stops/updateComponentInfo'"
                      :headers="{ Authorization: token }"
                      ref="uploadImg"
                      :data="formData"
                      :show-upload-list="false"
                      :on-success="handleSuccessImg"
                      :on-error="handleErrorImg"
                      :format="['jpg', 'png']"
                      :before-upload="handleBeforeUploadImg"
                      style="width: 80px; display: inline-block"
                      type="drag"
                    >
                      <div style="width: 80px; height: 80px; line-height: 80px">
                        <Icon type="ios-camera" size="20"></Icon>
                      </div>
                    </Upload>

                    <div v-if="file !== null">
                      <Icon
                        :color="JarIsShow === false ? 'red' : ''"
                        :type="JarIsShow === false ? 'md-close-circle' : ''"
                      />
                      Upload file: {{ file.name }}
                    </div>
                  </div>
                </template>
              </vxe-form-item>
              <vxe-form-item align="center" title-align="left" :span="24">
                <template #default>
                  <vxe-button type="submit">{{
                    $t("basicInfo.save")
                  }}</vxe-button>
                  <vxe-button type="reset" @click="handleCancel">{{
                    $t("modal.cancel_text")
                  }}</vxe-button>
                </template>
              </vxe-form-item>
            </template>
            <template v-if="componentType == 'SCALA'">
              <vxe-form-item
                :title="$t('sidebar.basicInfo')"
                title-align="left"
                :title-width="100"
                :span="24"
                :title-prefix="{ icon: 'fa fa-address-card-o' }"
              >
              </vxe-form-item>

              <vxe-form-item
                field="stopBundle"
                title="bundle"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.stopBundle"
                    :placeholder="$t('modal.placeholder')"
                    disabled
                  ></vxe-input>
                </template>
              </vxe-form-item>

              <vxe-form-item
                field="groups"
                title="groups"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.groups"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="bundleDescription"
                title="description"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.bundleDescription"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="owner"
                title="owner"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.owner"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>

              <vxe-form-item title="inports" :span="12" :item-render="{}">
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.inports"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item title="outports" :span="12" :item-render="{}">
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.outports"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>

              <vxe-form-item
                field="isHaveParams"
                :title="$t('python.parameter')"
                :span="12"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <vxe-switch
                    v-model="data.isHaveParams"
                    :open-label="$t('dataSource_columns.yes')"
                    :open-value="true"
                    :close-label="$t('dataSource_columns.no')"
                    :close-value="false"
                  ></vxe-switch>
                </template>
              </vxe-form-item>
              <vxe-form-item
                v-if="formData.isHaveParams"
                :span="24"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <template v-for="(item, i) in data.properties">
                    <Row
                      :gutter="16"
                      justify="center"
                      align="middle"
                      class="code-row-bg"
                      style="margin: 8px 0"
                      :key="i"
                    >
                      <Col offset="2" :span="2">
                        <div style="font-weight: bold">{{ i + 1 }}.</div>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.name"
                          :disabled="componentType != 'PYTHON'"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.example"
                          :disabled="componentType != 'PYTHON'"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="6">
                        <vxe-input
                          v-model="item.description"
                          :disabled="componentType != 'PYTHON'"
                          :placeholder="$t('modal.placeholder')"
                        ></vxe-input>
                      </Col>
                      <Col :span="2" v-if="componentType == 'PYTHON'">
                        <div>
                          <Icon
                            type="ios-add-circle-outline"
                            style="font-size: 20px; cursor: pointer"
                            @click="handleAddProperties"
                          />
                          <Icon
                            type="ios-remove-circle-outline"
                            style="font-size: 20px; cursor: pointer"
                            @click="removeProperties(i)"
                          />
                        </div>
                      </Col>
                    </Row>
                  </template>
                </template>
              </vxe-form-item>

              <vxe-form-item
                field="teamName"
                :title="$t('python.team')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.teamName"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="chineseName"
                :title="$t('python.chineseName')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.chineseName"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="useInstructions"
                :title="$t('python.Instructions')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.useInstructions"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="description"
                :title="$t('python.description')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.description"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="author"
                :title="$t('python.author')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.author"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                field="authorEmail"
                :title="$t('python.email')"
                :span="12"
                :item-render="{}"
                v-if="formData.isShared"
              >
                <template #default="{ data }">
                  <vxe-input
                    v-model="data.authorEmail"
                    :disabled="componentType != 'PYTHON'"
                    :placeholder="$t('modal.placeholder')"
                  ></vxe-input>
                </template>
              </vxe-form-item>
              <vxe-form-item
                :title="$t('python.logo')"
                title-align="left"
                :title-width="80"
                :span="24"
                :title-prefix="{ icon: 'fa fa-address-card-o' }"
              ></vxe-form-item>
              <vxe-form-item
                field="file"
                :title="$t('python.uploadLogo')"
                :span="24"
                :item-render="{}"
              >
                <template #default="{ data }">
                  <div class="img_warp">
                    <div
                      class="demo-upload-list"
                      v-for="(item, i) in uploadList"
                      :key="i"
                    >
                      <template>
                        <img :src="item.url" />
                      </template>
                    </div>
                    <Upload
                      :action="$url + '/stopsHubInfo/updateStopsHubInfo'"
                      :headers="{ Authorization: token }"
                      ref="uploadImg"
                      :data="formData"
                      :show-upload-list="false"
                      :on-success="handleSuccessImg"
                      :on-error="handleErrorImg"
                      :format="['jpg', 'png']"
                      :disabled="componentType != 'PYTHON'"
                      :before-upload="handleBeforeUploadImg"
                      style="width: 80px; display: inline-block"
                      type="drag"
                    >
                      <div style="width: 80px; height: 80px; line-height: 80px">
                        <Icon type="ios-camera" size="20"></Icon>
                      </div>
                    </Upload>

                    <div v-if="file !== null">
                      <Icon
                        :color="JarIsShow === false ? 'red' : ''"
                        :type="JarIsShow === false ? 'md-close-circle' : ''"
                      />
                      Upload file: {{ file.name }}
                    </div>
                  </div>
                </template>
              </vxe-form-item>
              <vxe-form-item align="center" title-align="left" :span="24">
                <template #default>
                  <vxe-button type="submit">{{
                    $t("basicInfo.save")
                  }}</vxe-button>
                  <vxe-button type="reset" @click="handleCancel">{{
                    $t("modal.cancel_text")
                  }}</vxe-button>
                </template>
              </vxe-form-item>
            </template>
          </vxe-form>
        </div>
      </template>
    </vxe-modal>
  </section>
</template>

<script>
import Cookies from "js-cookie";
export default {
  name: "stopHub",
  components: {},
  data() {
    return {
      isOpen: false,
      loading: true,
      page: 1,
      limit: 10,
      total: 0,
      tableData: [],
      param: "",
      file: null,
      JarIsShow: null,
      token: "",
      language: [
        {
          type: "PYTHON",
          value: ["2.7.5", "3.7.0", "3.7.5"],
        },
        {
          type: "SCALA",
          value: ["2.11.8"],
        },
      ],
      uploadData: {
        type: "",
        languageVersion: "",
      },
      versions: [],
      childData: [],
      formData: {
        stopBundle: "",
        stopName: "",
        groups: "",
        bundleDescription: "",
        owner: "",
        inports: "",
        outports: "",
        pyVersion: "",
        isShared: false,
        author: "",
        authorEmail: "",
        isPythonComponent: false,
        id: "",
        isHaveParams: false,
        properties: [
          {
            name: "",
            example: "",
            description: "",
            propertySort: 0,
            fkPythonStopsId: "",
            id: "",
          },
        ],
        teamName: "",
        chineseName: "",
        useInstructions: "",
        description: "",
        author: "",
        authorEmail: "",
      },
      componentType: "", //当前查看组件的类型
      isDisabled: false, //是否禁用
      showEdit: false,
      submitLoading: false,
      formRules: {
        groups: [
          {
            required: true,
            message: "请输入group",
          },
        ],
        description: [
          {
            required: true,
            message: "请输入description",
          },
        ],
        owner: [
          {
            required: true,
            message: "请输入owner",
          },
        ],
        inports: [
          {
            required: true,
            message: "请输入inports",
          },
        ],
        outports: [
          {
            required: true,
            message: "请输入outports",
          },
        ],
        pyVersion: [
          {
            required: true,
            message: "请输入python版本",
          },
        ],
        teamName: [{ required: true, message: "请输入团队" }],
        chineseName: [{ required: true, message: "请输入中文名" }],
        useInstructions: [{ required: true, message: "请输入使用说明" }],
        description: [{ required: true, message: "请输入详细描述信息" }],
        author: [{ required: true, message: "请输入组件作者" }],
        authorEmail: [{ required: true, message: "请输入作者联系邮箱" }],
      },
    };
  },
  watch: {
    param() {
      this.page = 1;
      this.limit = 10;
      this.getTableData();
    },
    "formData.isHaveParams": {
      handler(val, oldVal) {
        if (val) {
          if (!this.formData.hasOwnProperty("properties")) {
            let arr = [
              {
                name: "",
                example: "",
                description: "",
                propertySort: 0,
                fkPythonStopsId: "",
                id: "",
              },
            ];
            this.$set(this.formData, "properties", arr);
          }
        }
      },
      immediate: true,
    },
  },
  // computed: {
  //   columns() {
  //     return [
  //       {
  //         title: this.$t("StopHub_columns.name"),
  //         key: "jarName",
  //         sortable: true,
  //       },
  //       {
  //         title: this.$t("StopHub_columns.time"),
  //         key: "crtDttm",
  //         sortable: true,
  //         width: 180,
  //       },
  //       {
  //         title: this.$t("StopHub_columns.version"),
  //         key: "version",
  //         sortable: true,
  //         width: 120,
  //       },
  //       {
  //         title: this.$t("StopHub_columns.jarUrl"),
  //         key: "jarUrl",
  //       },
  //       {
  //         title: this.$t("StopHub_columns.status"),
  //         key: "status",
  //         sortable: true,
  //         render: (h, params) => {
  //           return h("span", params.row.status.text);
  //         },
  //         width: 150,
  //       },
  //       {
  //         title: this.$t("StopHub_columns.action"),
  //         slot: "action",
  //         width: 180,
  //         align: "center",
  //       },
  //     ];
  //   },
  // },
  created() {
    this.getTableData();
  },
  mounted() {
    let token = this.$store.state.variable.token;
    if (!token) {
      token = `${Cookies.get("token")}`;
    }
    this.token = `Bearer ${token}`;
  },
  methods: {
    handleButtonSelect(row, key) {
      switch (key) {
        case 1:
          this.handleMount(row);
          break;
        case 2:
          this.handlePublish(row);
          break;
        case 3:
          this.handleDeleteRow(row);
          break;
        default:
          break;
      }
    },

    handleSuccess(res, file) {
      this.file = null;
      this.$Message.success({
        content: res.errorMsg,
        duration: 3,
      });
      this.getTableData();
      this.$refs.upload.clearFiles();
      setTimeout(() => {
        this.isOpen = false;
      }, 1000);
    },

    handleError(error, file) {
      // this.JarIsShow = false;
      this.pythonIsShow = false;
      this.file = null;
      this.$Message.error({
        content: error.errorMsg,
        duration: 3,
      });
    },

    handleBeforeUpload(file) {
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "zip" || testmsg === "jar";
      const isLt500M = file.size / 1024 / 1024 < 500;
      if (!extension) {
        this.$Notice.warning({
          title: "The file format is incorrect",
          desc:
            "File format of " + file.name + " is incorrect, please select jar.",
        });
        return false;
      }
      if (!isLt500M) {
        this.$Notice.warning({
          title: "Exceeding file size limit",
          desc: "File  " + file.name + " is too large, no more than 500M.",
        });
        return false;
      } else this.file = file;
      this.file = file;
      return false;
    },

    handleRemove(m, mark) {
      if (mark) {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content: "此项不可删除，请重新操作！",
        });
        return;
      }
    },

    handleMount(row) {
      let data = { id: row.id };
      let url = "/stops/unmountStopsHub";
      let flag = false;
      if (row.status.text === "UNMOUNT") {
        url = "/stops/mountStopsHub";
        flag = true;
      }

      this.$event.emit("loading", true);
      this.$axios
        .post(url, this.$qs.stringify(data))
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: res.data.errorMsg,
            });
            this.getTableData();
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: res.data.errorMsg,
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    handlePublish(row) {
      let data = { id: row.id };
      this.$event.emit("loading", true);
      this.$axios
        .post("/stops/stopsHubPublishing", this.$qs.stringify(data))
        .then((res) => {
          if (res.data.code === 200) {
            this.$event.emit("loading", false);
            this.$Modal.success({
              title: this.$t("tip.title"),
              content: res.data.errorMsg,
            });
            this.getTableData();
          } else {
            this.$event.emit("loading", false);
            this.$Message.error({
              content: res.data.errorMsg,
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$event.emit("loading", false);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },

    handleDeleteRow(row) {
      if (row.status.text === "MOUNT") {
        this.$Modal.warning({
          title: this.$t("tip.title"),
          content:
            `${row.jarName} ` + "In operation, temporarily unable to delete !",
        });
      } else {
        this.$Modal.confirm({
          title: this.$t("tip.title"),
          okText: this.$t("modal.confirm"),
          cancelText: this.$t("modal.cancel_text"),
          content: `${this.$t("modal.delete_content")} ${row.jarName}?`,
          onOk: () => {
            let data = {
              id: row.id,
            };
            this.$axios
              .post("/stops/delStopsHub", this.$qs.stringify(data))
              .then((res) => {
                if (res.data.code === 200) {
                  this.$Message.success({
                    duration: 3,
                    content:
                      `${row.jarName} ` + this.$t("tip.delete_success_content"),
                  });
                  this.getTableData();
                } else {
                  this.$Message.error({
                    content: this.$t("tip.delete_fail_content"),
                    duration: 3,
                  });
                }
              })
              .catch((error) => {
                console.log(error);
                this.$Message.error({
                  content: this.$t("tip.fault_content"),
                  duration: 3,
                });
              });
          },
        });
      }
    },

    uploadSuccess() {
      this.isOpen = true;
      this.$refs.upload.post(this.file);
    },

    uploadError() {
      this.isOpen = false;
      this.$refs.upload.clearFiles();
    },

    getTableData() {
      let data = { page: this.page, limit: this.limit };
      if (this.param) {
        data.param = this.param;
      }
      this.$axios
        .get("/stops/stopsHubListPage", { params: data })
        .then((res) => {
          if (res.data.code === 200) {
            this.tableData = res.data.data;
            this.total = res.data.count;
          } else {
            this.$Message.error({
              content: this.$t("tip.request_fail_content"),
              duration: 3,
            });
          }
        })
        .catch((error) => {
          console.log(error);
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
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
    //选择语言
    changeLanguage(val) {
      this.uploadData.type = val;
      this.language.forEach((item) => {
        if (item.type == val) {
          this.versions = item.value;
        }
      });
    },
    //表格展开
    toggleRow({ expanded, row }) {
      this.pythonId = "";
      this.childData = [];
      if (expanded) this.handleRelevanceRow(row.id);
    },
    //查询子组件
    handleRelevanceRow(id) {
      let parameter = {
        stopsHubId: id,
      };
      this.pythonId = id;
      this.$axios
        .post(
          "/stops/getStopsHubInfoByStopHubId",
          this.$qs.stringify(parameter)
        )
        .then((res) => {
          if (res.data.code === 200) {
            let data = res.data.data;
            if (data != null || data != undefined) {
              this.childData = data;
            }
          }
        })
        .catch((error) => {
          this.$Message.error({
            content: this.$t("tip.fault_content"),
            duration: 3,
          });
        });
    },
    //查看组件详情
    editEvent(row, val) {
      for (let key in row) {
        for (let i in this.formData) {
          if (key === i) this.formData[key] = row[i];
        }
      }
      if (row.imageUrl) this.formData.imageUrl = row.imageUrl;

      this.uploadList = [];
      let url = `/stops/getStopsInfoById`;
      let type = "SCALA";
      if (val.type.text == "PYTHON") {
        type = "PYTHON";
      }
      this.componentType = type;
      this.showEdit = true;
      if (this.componentType == "PYTHON") {
        this.isDisabled = true;
      } else {
        this.isDisabled = false;
      }

      let parameter = {
        id: row.id,
        type,
      };
      this.$axios
        .post(url, this.$qs.stringify(parameter))
        .then((res) => {
          if (res.data.code === 200) {
            // console.log(res);
            //此处代码不可删除,如果formData没有properties这个属性则添加，否则报错阻断弹框更新
            this.formData = res.data.data;
            this.tableLoading = true;
            if (res.data.data.imageUrl)
              this.uploadList.push({
                url: res.data.data.imageUrl,
              });
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },

    submitEvent() {
      this.submitLoading = true;
      this.saveRowEvent();
      setTimeout(() => {
        this.submitLoading = false;
        this.showEdit = false;
      }, 500);
    },
    //保存更新弹框内容
    saveRowEvent() {
      let formData = null;

      formData = this.formData;
      // formData.isPythonComponent = this.componentType == "SCALA" ? false : true;
      if (
        this.formData.isHaveParams &&
        Array.isArray(this.formData.properties)
      ) {
        formData.properties = this.formData.properties.map((item, i) => {
          return {
            ...item,
            propertySort: i,
          };
        });
        formData.properties.forEach((item, i) => {
          formData[`properties[${i}].name`] = item.name;
          formData[`properties[${i}].example`] = item.example;
          formData[`properties[${i}].description`] = item.description;
          formData[`properties[${i}].propertySort`] = item.propertySort;
          formData[`properties[${i}].fkPythonStopsId`] = item.fkPythonStopsId;
          formData[`properties[${i}].id`] = item.id;
        });
        this.$delete(formData, "properties");
      } else {
        this.$delete(formData, "properties");
      }
      if (this.file) {
        this.$refs.uploadImg.post(this.file);
      } else {
        //手动上传
        this.tableLoading = true;
        this.$axios
          .post("/stops/updateComponentInfo", this.$qs.stringify(formData))
          .then((res) => {
            this.tableLoading = false;
            this.file = null;
            // console.log(res)
            if (res.data.code === 200) {
              this.handleRelevanceRow(this.pythonId);
              setTimeout(() => {
                this.$Message.success({
                  content: res.data.errorMsg,
                  duration: 3,
                });
              }, 300);
              this.formData.properties = [
                {
                  name: "",
                  example: "",
                  description: "",
                  propertySort: 0,
                  fkPythonStopsId: "",
                  id: "",
                },
              ];
              // })
            } else {
              this.$Message.success({
                content: res.data.errorMsg,
                duration: 3,
              });
            }
            this.showEdit = false;
          })
          .catch((error) => {
            // console.log(error);
            this.file = null;
            this.$VXETable.modal.message({
              content: error.data.message,
              status: "error",
            });
          });
      }
    },
    handleAddProperties() {
      let obj = {
        name: "",
        example: "",
        description: "",
        fkPythonStopsId: "",
        id: "",
        propertySort: this.formData.properties.length,
      };

      this.formData.properties = [...this.formData.properties, obj];
    },
    removeProperties(i) {
      if (this.formData.properties.length > 1) {
        this.formData.properties.splice(i, 1);
      }
    },
    handleSuccessImg(res, file) {
      // console.log(res);
      this.file = null;
      this.$Message.success(res.errorMsg);
      this.handleRelevanceRow(this.pythonId);
      this.$nextTick(() => {
        this.uploadList = [];
        this.$refs.uploadImg.clearFiles();
      });
    },

    handleErrorImg(error, file) {
      this.JarIsShow = false;
      this.$Message.error(error.errorMsg);
      this.$refs.uploadImg.clearFiles();
    },

    handleBeforeUploadImg(file) {
      let testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      let imgArr = ["jpg", "png"];
      const extension = imgArr.indexOf(testmsg) !== -1 ? true : false;
      const isLt500M = file.size / 1024 / 1024 < 500;
      if (!extension) {
        this.$Notice.warning({
          title: "The file format is incorrect",
          desc:
            "File format of " +
            file.name +
            " is incorrect, please select jpg || png.",
        });
        return false;
      }
      if (!isLt500M) {
        this.$Notice.warning({
          title: "Exceeding file size limit",
          desc: "File  " + file.name + " is too large, no more than 500M.",
        });
        return false;
      } else this.file = file;

      return false;
    },
    //取消更新组件
    handleCancel() {
      this.showEdit = false;
      this.file = null;
    },
  },
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
.item {
  display: flex;
  label {
    margin-top: 5px;
  }
}
::v-deep .ivu-upload-drag:hover {
  border: 1px dashed var(--primary-color);
}
.gray_btn {
  cursor: not-allowed !important;
  filter: grayscale(100%) !important;
}
.flex {
  display: flex;
  > div {
    margin-right: 20px;
    label {
      padding-right: 10px;
    }
  }
}
.info-warp {
  width: 100%;
  box-sizing: border-box;
  padding: 0 20px 20px 50px;
  margin-bottom: 10px;
  background: #fff;
  position: relative;
  border-radius: 18px 18px 18px 18px;
  border: 1px solid rgba(61, 85, 167, 0.4);
  //margin-right: 1%;
  display: inline-block;

  &:hover {
    background: #fffdf9;

    .edit {
      display: block;
    }
  }

  label {
    color: #757575;
  }

  p {
    height: 24px;
    font-size: 16px;
    padding-left: 20px;
  }

  .personName {
    color: #262626;
    font-size: 18px;
    line-height: 30px;
    text-indent: 1rem;
    margin-bottom: 6px !important;
    padding: 0;
  }

  .tagNumber {
    border-radius: 16px 0 16px 0;
    width: 50px;
    height: 30px;
    display: block;
    //background: #243c9c;
    background: linear-gradient(
      135deg,
      var(--sidebar-color) 0%,
      rgba(61, 163, 117, 0.3) 100%
    );
    position: absolute;
    top: 0;
    left: 0;
    color: #fff;
    text-align: center;
    line-height: 30px;
  }

  .edit {
    display: none;
    cursor: pointer;
    color: var(--primary-color);
    padding-left: 20px;
    position: absolute;
    right: 20px;
    bottom: 3px;
  }

  .textEllipsis {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}
.img_warp {
  border: 1px solid var(--primary-color)99;
  padding: 10px;
  border-radius: 18px;
  margin-right: 3%;
}
.demo-upload-list {
  display: inline-block;
  width: 80px;
  height: 80px;
  text-align: center;
  line-height: 60px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  margin-right: 4px;
  border: 1px dashed #dcdee2;
}
.demo-upload-list img {
  width: 100%;
  height: 100%;
}
.demo-upload-list-cover {
  display: none;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}
.demo-upload-list:hover .demo-upload-list-cover {
  display: block;
}
.demo-upload-list-cover i {
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  margin: 0 2px;
}
</style>