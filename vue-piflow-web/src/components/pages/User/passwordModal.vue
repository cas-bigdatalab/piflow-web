<template>
  <Modal v-model="open" title="重置密码" footer-hide  width="600"  :closable="false" :mask-closable="false">
        <PasswordForm  mode="passwordModal"  @submit="handleClose"/>
  </Modal>
</template>

<script>
import PasswordForm from './PasswordForm'
export default {
    components:{
        PasswordForm
    },
    data(){
        return {
            open: false,
        }
    },
    methods: {
        handleOpen(){
            this.open  = true
        },
        handleClose(){
            this.open  = false
        },

    },  
}
</script>