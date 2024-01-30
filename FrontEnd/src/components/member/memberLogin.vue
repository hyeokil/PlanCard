<template>
  <div class="card p-fluid" id="box">
    <div class="container; card p-fluid" id="logInBox">
      <form @submit.prevent="signIn">
        <div class="box, card p-fluid" id="idInput">
          <img src="/아이디 아이콘.png" alt="아이디" id="idIcon">
          <label for="username"></label>
          <input type="text" id="username" v-model.trim="username" placeholder="ID">
        </div>
        <div class="box, card p-fluid" id="pwInput">
          <img src="/비밀번호 아이콘.png" alt="비밀번호" id="pwIcon">
          <label for="password"></label>
          <input type="password" id="password" v-model.trim="password" placeholder="PW">
        </div>
        <div class="box, card p-fluid" id="loginSubmit">
          <input type="submit" style="display: inline-block;" value="LogIn">
        </div>
      </form> 
      <div>
          <p>아직 회원이 아니신가요?</p>
          <button @click="goSignUp()">[회원가입]</button>
      </div>
    </div>
  </div>
</template>



<script setup>
  import { ref } from 'vue';
  import { useAccountsStore } from '@/stores/accountsStore.js';
  import { useRouter } from 'vue-router';

  const router = useRouter()

  const accountsStore = useAccountsStore()

  const username = ref(null)
  const password = ref(null)

  const signIn = function () {
      const info = {
          username: username.value,
          password: password.value
      }
      accountsStore.signIn(info)
  }

  const goSignUp = function () {
    router.push({name: 'member-signup'});
  }
</script>



<style scoped>
  #box {
  display: flex;
  justify-content: center;
  }
  #logInBox {
    background-color: #EAF9FF;
    width: 350px;
    height: 50%;
  }
  #idIcon {
    width: 30px;
    height: 30px;
    margin-right: 10px;
    margin-left: -15px;
  }
  #pwIcon {
    width: 30px;
    height: 30px;
    margin-top: -15px;
    margin-right: 10px;
  }
  #username {
    height: 35px;
    width: 195px;
  }
  #password {

  }
  #idInput {
    border: 1px solid rgba(0, 0, 0, 0.1);
    height: 40px;
    align-items: center; /* 하위 요소들을 수직 가운데 정렬. */
    margin-bottom: 10px;
  }
  #pwInput {
    border: 1px solid rgba(0, 0, 0, 0.1);
    height: 40px;
    align-items: center; /* 하위 요소들을 수직 가운데 정렬. */
    margin-bottom: 10px;
  }
  #loginSubmit {
    text-align: center;
    background-color: #3498db;
    color: #FFFFFF;
  }
</style>