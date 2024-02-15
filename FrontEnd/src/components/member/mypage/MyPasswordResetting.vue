<template>
    <div class="card p-fluid">
      <div>
        <h1>비밀번호 변경</h1>
      </div>
      
      <hr id="separator">

      <div id="box">

        <div class="container; card p-fluid" id="resettingBox">
          <h1 id="passwordResettingTitle">비밀번호 변경</h1>
          
          <form @submit.prevent="passwordResetting()" id="passwordResettingForm">
            
            <div class="box, card p-fluid" id="nowPasswordInput">
              <input type="password" id="nowPassword" v-model.trim="nowPassword" placeholder="기존 비밀번호">
            </div>
            
            <div class="box, card p-fluid" id="changePassword1Input">
              <input type="password" id="changePassword1" v-model.trim="changePassword1" placeholder="새로운 비밀번호">
            </div>
            
            <div class="box, card p-fluid" id="changePassword2Input">
              <input type="password" id="changePassword2" v-model.trim="changePassword2" placeholder="새로운 비밀번호 확인">
            </div>
            
            <input type="submit" value="변경하기" class="box, card p-fluid"  id="resettingSubmit" style=" text-align: center;">
          </form>
        </div>
      </div>
    </div>
    </template>




<script setup>
  import { ref } from 'vue';
  import { passwordResettingApi } from "@/api/memberApi";
  import { useRouter } from 'vue-router';
  const router = useRouter();


  const nowPassword = ref(null);  // 현재 비밀번호
  const changePassword1 = ref(null);  // 변경할 비밀번호
  const changePassword2 = ref(null);  // 변경할 비밀번호 확인
  
  // 비밀번호를 변경하는 함수
  const passwordResetting = async () => {
    if (changePassword1.value !== changePassword2.value) {
      alert("변경할 비밀번호가 같지 않습니다.")
    } else {
      const passwordResettingData = {
        nowPassword: nowPassword.value,
        changePassword: changePassword1.value
      };
      try {
        await passwordResettingApi(passwordResettingData,
        (response) => {
          if (response.data.dataHeader.successCode === 0) {
            alert("비밀번호 변경에 성공했습니다.");
            router.push("/member/mypage");
          } else {
            alert(response.data.dataHeader.resultMessage);
          }
        });
      } catch (error) {
        console.error(error);
        alert("비밀번호 변경 중 오류가 발생했습니다.");
      }
    }
  }
  </script>



<style scoped>
  h1 {
    /* font-weight: bold; */
  }
  #separator {
    border: none;
    border-top: 4px solid #3498DB;
    border-radius: 10px;
  }

  #box {
  display: flex;
  justify-content: center;
  }

  #resettingBox {
    background-color: #FFFFFF;
    border: 1px solid rgba(52, 152, 219, 0.5);
    width: 400px;
    height: 50%;
    padding: 40px;

    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
  }
  #passwordResettingTitle {
    color: #3498db;
    /* font-weight: bold; */
  }
  #passwordResettingForm {
    display: flex;
    flex-direction: column;
    align-items: center;
  }


  #nowPassword, #changePassword1, #changePassword2 {
    height: 35px;
    width: 250px;
    padding-left: 10px;
  }
  #nowPasswordInput, #changePassword1Input, #changePassword2Input {
    background-color: rgba(245, 245, 245, 0.1);
    width: 90%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }


  #resettingSubmit {
    width: 90%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498db;
    color: #FFFFFF;
    /* font-weight: bold; */
    font-size: medium;
    height: 40px;
    padding: 0px;
    transition: transform 0.2s, border-color 0.2s; /* 부드러운 전환 효과를 위한 설정 */
    cursor: pointer;
  }
  #resettingSubmit input[type="submit"] {
    height: 100%; /* 버튼의 높이를 기준으로 텍스트를 수직 정렬 */
  }
  #resettingSubmit:hover {
    transform: scale(1.05);
    border-color: #3498db;
  }

</style>