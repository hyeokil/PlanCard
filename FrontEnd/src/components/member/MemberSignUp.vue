<template>
    <div>
        <h1>MemberSignUp.vue</h1>
        <form @submit.prevent="signUp">
            <div class="box">
                <input type="userEmail" id="userEmail" v-model.trim="userEmail" placeholder="이메일">
            </div>

            <div class="box">
                <input type="userName" id="userName" v-model.trim="userName" placeholder="이름">
            </div>

            <div class="box">
                <input type="userNickname" id="userNickname" v-model.trim="userNickname" placeholder="닉네임">
            </div>

            <div class="box">
                <input type="password" id="password1" v-model.trim="password1" placeholder="비밀번호">
            </div>

            <div class="box">
                <input type="password" id="password2" v-model.trim="password2" placeholder="비밀번호 재입력">
            </div>
            <div class="box">
                <input type="userPhoto" id="userPhoto" v-model.trim="userPhoto" placeholder="사진 추가">
            </div>
    
            <div class="box" style=" text-align: center;">
                <input type="submit" style="display: inline-block;" value="가입하기">
            </div>
        </form>
    </div>
</template>



<script setup>
  import { ref } from 'vue';
  import { useAccountsStore } from '@/stores/accountsStore.js';

  const accountsStore = useAccountsStore()

  const userEmail = ref(null);
  const userName = ref(null);
  const userNickname = ref(null);
  const password1 = ref(null);
  const password2 = ref(null);
  const userPhoto = ref(null);

  const signUp = function () {
      const info = {
        userEmail: userEmail.value,
        userName: userName.value,
        userNickname: userNickname.value,
        password1: password1.value,
        password2: password2.value,
        userPhoto: userPhoto.value,
      }
      accountsStore.signUp(info)
  }
</script>



<style scoped>

</style>@/stores/accountStore