<template>
  <Modal v-model="open" title="重置密码" footer-hide  :closable="false" :mask-closable="false">
        
 
  </Modal>
</template>

<script>
import Cookies from "js-cookie";
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

    },  
}
</script>