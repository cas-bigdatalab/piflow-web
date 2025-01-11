<template>
  <Modal
    v-model="open"
    footer-hide
    :title="$t('user_columns.assign_roles')"
  >
    <Form ref="form" :model="formData" :label-width="80">
      <!-- 账号 -->
      <FormItem :label="$t('user_columns.role')">
        <Select  v-model="formData.role">
            <Option v-for="item in roleList" :key="item.role.text" :value="item.role.text">{{item.role.text}}</Option>
        </Select>
      </FormItem>

      <FormItem>
        <div style="text-align: right">
          <Button @click="open = false">{{ $t("modal.cancel_text") }}</Button>
          <Button
            type="primary"
            @click="handleSubmit"
            style="margin-left: 8px"
            >{{ $t("modal.confirm") }}</Button
          >
        </div>
      </FormItem>
    </Form>
  </Modal>
</template>

<script>
import {
  getAllRole,
  updateRole,
} from "@/apis/sysUser";
export default {
  data() {
    return {
      open: false,
      formData: {
        role: "",
      },
      roleList:[],
    };
  },
  created() {
    this.getAllRole()
  },
  methods: {
   async getAllRole(){
      const res = await getAllRole()
      if(res.data.code === 200){
        this.roleList = res.data.data
      }else{
        this.roleList = []
      }

    },
    handleEdit(role,id) {
      this.id = id
      this.formData = {role};
      this.open = true;
    },
    async handleSubmit() {
      const res = await updateRole({
        id:this.id,
        role:this.roleList.find(v=>v.role.text === this.formData.role)
      });
      if (res.data.code === 200) {
        this.$Message.success({
          content: this.$t("tip.operate_success"),
          duration: 3,
        });
        this.$emit("submit");
        this.open = false;
      } else {
        this.$Message.error({
          content: res.data.data,
          duration: 3,
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped></style>
