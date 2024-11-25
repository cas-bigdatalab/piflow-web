<template>
  <Modal v-model="open" title="重置密码" footer-hide  :closable="false" :mask-closable="false">
        <PasswordForm  @submit="handleClose"/>
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