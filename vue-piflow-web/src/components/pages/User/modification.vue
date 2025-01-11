<template>
  <section>
    <div class="navbar">
      <div class="left">
        <span>{{$t("sidebar.modification")}}</span>
      </div>
    </div>
    <div class="contentArea">
      <PasswordForm  mode="modification"/>
    </div>
  </section>
</template>

<script>
import PasswordForm from './PasswordForm.vue'
export default {
  name: "modification",
  components:{PasswordForm},
};
</script>

<style lang="scss" scoped>
@import "./index.scss";
.contentArea{
  background: white;
  padding: 50px 20px 20px 20px;
  border-radius: 20px;
  margin-top: 20px;
  text-align: center;
  form{
    width: 60%;
    margin: 0 auto;
  }
  ::v-deep .ivu-form .ivu-form-item-label{
    line-height: inherit;
    width: 180px!important;
  }
  ::v-deep .ivu-form-item-content{
    margin-left: 180px!important;
  }
}
</style>

