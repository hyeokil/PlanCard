<template>
  <div class="card p-fluid" id="box">
    <div class="container; card p-fluid" id="logInBox">
      <h1 id="logInTitle">로그인</h1>
      <form @submit.prevent="login()" id="logInForm">
        <div class="box, card p-fluid" id="idInput">
          <img src="/아이디 아이콘.png" alt="아이디" id="idIcon">
          <label for="memberEmail"></label>
          <input type="text" id="memberEmail" v-model.trim="memberEmail" placeholder="EMAIL">
        </div>
        <div class="box, card p-fluid" id="pwInput">
          <img src="/비밀번호 아이콘.png" alt="비밀번호" id="pwIcon">
          <label for="memberPassword"></label>
          <input type="password" id="memberPassword" v-model.trim="memberPassword" placeholder="PW">
        </div>
        <div class="box, card p-fluid" id="loginSubmit">
          <input type="submit" value="LogIn">
        </div>
      </form> 
      <div id="etc1">
          <p id="signUpGuide">아직 회원이 아니신가요?</p>
          <button id="signUpBtn" @click="goSignUp()">[회원가입]</button>
      </div>
      <div id="etc2">
          <p id="pwSearchGuide">비밀번호를 잊어버렸나요?</p>
          <button id="pwSearchBtn">[비밀번호 찾기]</button>
      </div>
      <div class="card p-fluid" id="otherLogin">
        <p id="otherLoginGuide">다른 방법으로 로그인</p>
        <hr id="separator">
        <div id="otherLoginBtn">
          <img src="/네이버 로그인 버튼.png" alt="네이버로그인" id="loginLogoImg" @click="socialLogin('naver')">
          <img src="/카카오 로그인 버튼.png" alt="카카오로그인" id="loginLogoImg" @click="socialLogin('kakao')">
        </div>
      </div>
    </div>
  </div>
</template>



<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { memberLoginApi, startSocialLoginApi } from "@/api/memberApi";
import { useAccountsStore } from '@/stores/accountsStore';

const router = useRouter()

const memberEmail = ref('');
const memberPassword = ref('');

const accountsStore = useAccountsStore();

const login = async () => {
  const param = {
    email: memberEmail.value,
    password: memberPassword.value
  };

  try {
    await memberLoginApi(param,
      (response) => {
        if (response.data.dataHeader.successCode === 0) {
          // 로그인 상태와 사용자 정보를 accountsStore에 저장
          accountsStore.setLogin(true);
          accountsStore.setMemberInfo(response.data.dataBody.memberInfo);
          router.push({ name: 'main' });  // 로그인 성공 후 메인 페이지로 이동  
        } else {
          // 로그인 실패 처리 메시지
          alert(response.data.dataHeader.resultMessage);
        }
      }
    );
  } catch (error) {
    console.error(error);
    alert("로그인 과정 중 문제가 발생했습니다.");
  }
};

const socialLogin = async (oAuthDomain) => {
  // window.location.href = `${import.meta.env.VITE_VUE_API_URL}/oauth/${oAuthDomain}`;

  try {
    const response = await startSocialLoginApi(oAuthDomain);

        if (response.data.dataHeader.successCode === 0) {
          window.location.href = response.data.dataBody;
        } else {
          alert(response.data.dataHeader.resultMessage);
        }
  } catch (error) {
    console.error(error);
    alert("소셜 로그인 완료 과정 중 문제가 발생했습니다.");
  }

};

const goSignUp = function () {
  router.push({ name: 'member-signup' });
}
</script>



<style scoped>
  #box {
  display: flex;
  justify-content: center;
  }
  #logInForm {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  #logInBox {
    border: 1px solid rgba(52, 152, 219, 0.5);

    background-color: #FFFFFF;
    width: 400px;
    height: 50%;
    padding: 40px;

    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
  }
  #logInTitle {
    color: #3498db;
    font-weight: bold;
    padding-bottom: 40px;
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
    margin-right: 10px;
    margin-left: -15px;
  }
  #memberEmail {
    height: 35px;
    width: 250px;
  }
  #memberPassword {
    height: 35px;
    width: 250px;
  }
  #idInput {
    background-color: rgba(245, 245, 245, 0.1);
    width: 90%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }
  #pwInput {
    background-color: rgba(245, 245, 245, 0.1);
    width: 90%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }
  #loginSubmit {
    width: 90%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498db;
    color: #FFFFFF;
    font-weight: bold;
    font-size: medium;
    height: 20px;
    padding: 20px;
    
  }

  #etc1 {
    display: flex;
    margin-top: 10px;
  }
  #signUpGuide {
    color: rgba(0, 0, 0, 0.3);
    font-weight: bold;
    margin: 0;
    margin-right: 10px;
  }
  #signUpBtn {
    font-weight: bold;
    color: #3498db;
  }
  #etc2 {
    display: flex;
  }
  #pwSearchGuide {
    color: rgba(0, 0, 0, 0.3);
    font-weight: bold;
    margin: 0;
    margin-right: 10px;
  }
  #pwSearchBtn {
    font-weight: bold;
    color: #3498db;
  }

  #otherLogin {
    background-color: #FFFFFF;
    color: rgba(0, 0, 0, 0.5);
    font-weight: bold;
    width: 100%;
    padding: 5px;
    margin-top: 20px;
    border: 1px solid rgba(52, 152, 219, 0.5);
  }
  #otherLoginGuide {
    text-align: center;
    padding-top: 5px;
    margin: 0;
  }
  #separator {
    margin-top: 3px;
    border: none;
    border: 1px solid rgba(52, 152, 219, 0.5);
    border-radius: 10px;
  }
  #otherLoginBtn {
    display: flex;
    justify-content: space-around;
    width: 100%;
  }
  #loginLogoImg {
    border: 1px solid rgba(0, 0, 0, 0.1);
    width: 130px;
    margin-right: 5px;
    margin-left: 5px;
    margin-bottom: 5px;
    transition: transform 0.2s, border-color 0.2s; /* 부드러운 전환 효과를 위한 설정 */
    cursor: pointer; /* 마우스 포인터를 손가락 모양으로 변경 */
  }
  #loginLogoImg:hover {
    transform: scale(1.05); /* 이미지를 약간 확대 */
    border-color: #3498db; /* 테두리 색상 변경 */
  }
</style>