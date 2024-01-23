<template>
    <div>
        <h1>MyPasswordRessetting.vue</h1>
        <form @submit.prevent="passwordResetting">
            <div class="box">
                <input type="password" id="currentPassword" v-model.trim="currentPassword" placeholder="기존 비밀번호">
            </div>

            <div class="box">
                <input type="password" id="newPassword1" v-model.trim="newPassword1" placeholder="새로운 비밀번호">
            </div>

            <div class="box">
                <input type="password" id="newPassword2" v-model.trim="newPassword2" placeholder="새로운 비밀번호 재입력">
            </div>
    
            <div class="box" style=" text-align: center;">
                <input type="submit" style="display: inline-block;" value="변경하기">
            </div>
        </form>
    </div>
</template>



<script setup>
  import { ref } from 'vue';
  import { useAccountsStore } from '@/stores/accounts';

  const accountsStore = useAccountsStore()

  const currentPassword = ref(null);
  const newPassword1 = ref(null);
  const newPassword2 = ref(null);

  const passwordResetting = function () {
      const info = {
        currentPassword: currentPassword.value,
        newPassword1: newPassword1.value,
        newPassword2: newPassword2.value,
      }
      accountsStore.passwordResetting(info)
  }
</script>



<style scoped>

</style>