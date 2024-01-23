<template>
  <div>
    <h1>MemberLogin.vue</h1>
    <div class="container">
      <h1>로그인</h1>

      <form @submit.prevent="signIn" >
        <div class="box">
            <label for="username">username &nbsp</label>
            <input type="text" id="username" v-model.trim="username">
        </div>
        <div class="box">
            <label for="password">password &nbsp</label>
            <input type="password" id="password" v-model.trim="password">
        </div>
        <div class="box" style=" text-align: center;">
          <input type="submit" style="display: inline-block;" value="LogIn">
        </div>
      </form> 
    </div>
    <div>
        <p>아직 회원이 아니신가요?</p>
        <button @click="goSignUp()">[회원가입]</button>
    </div>
  </div>
</template>



<script setup>
  import { ref } from 'vue';
  import { useAccountsStore } from '@/stores/accounts';
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

</style>