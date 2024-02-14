<template>
    <div class="card p-fluid" id="friendRequestBox">
      <h3 id="friendRequestTitle">친구 요청</h3>
      <form class="box, card p-fluid" @submit.prevent="SendFriendRequest()" id="friendRequestForm">
        <input class="box card p-fluid" type="email" id="friendRequestInput" v-model="friendEmail"
        placeholder="친구의 Email">
        <input class="box card p-fluid" type="submit" id="friendRequestSubmit" value="요청 보내기">
      </form>
      <div>
        <v-btn @click="$emit('closeFriendRequest')" id="friendsRequestcloseBtn">닫기</v-btn>
      </div>
    </div>
</template>



<script setup>
  import { alarmFriendRequestApi } from "@/api/alarmApi";
  import { ref, getCurrentInstance } from 'vue';
  const { emit } = getCurrentInstance();

  const friendEmail = ref(``);  // 친구 요청할 때 입력하는 email


  // 친구 요청 메서드
  const SendFriendRequest = async () => {
  // 이메일 형식을 검사하는 정규 표현식
  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

  // 입력된 이메일이 정규 표현식과 일치하는지 확인
  if (emailRegex.test(friendEmail.value)) {
      try {
      const param = {
          email: friendEmail.value,
          type: "FRIEND"
      };
      const response = await alarmFriendRequestApi(param);
      if (response.data.dataHeader.successCode === 0) {
          alert("친구 요청을 보냈습니다.");
          emit('closeFriendRequest');
      } else {
          alert(response.data.dataHeader.resultMessage);
      }
      } catch (error) {
      if (error.response) {
          console.error(error);
          const errorResponse = error.response.data;
          alert(errorResponse.dataHeader.resultMessage);
      } else if (error.message === 'Network Error' || error.code === 'ERR_NETWORK') {
          // 네트워크 에러 처리
          alert("서버에 연결할 수 없습니다. 네트워크 연결을 확인해주세요.");
      }
      }
  } else {
      alert('유효하지 않은 이메일 형식입니다.'); // 이메일 형식이 유효하지 않을 때의 메시지
  }
  };
</script>



<style scoped>
#overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  /* 어두운 배경 */
  z-index: 997;
  /* 모달보다 한 단계 낮은 z-index */
}
#friendRequestBox {
  position: fixed;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999;
}

#friendRequestTitle {
  color: #3498db;
  /* font-weight: bold; */
  margin: 0;
}

#friendRequestForm {
  border: none;
  margin-bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  padding-bottom: 0;
}
#friendRequestInput {
  background-color: rgba(245, 245, 245, 0.1);
  width: 300px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  margin: 5px;
  text-align: center;
  /* 입력 내용을 가운데 정렬합니다. */
  padding: 8px;
  font-size: large;
}
#friendRequestSubmit {
  width: 100px;
  height: 30px;
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  position: relative;
  /* font-weight: bold; */
  border-radius: 5cm;
  border: 1px solid rgba(0, 0, 0, 0.1);
  padding: 10px;
  margin-top: 5px;
  line-height: 0px;
  transition: transform 0.3s ease;

}
#friendRequestSubmit:hover {
  transform: scale(1.05);
  border-color: #3498db;
}
#friendsRequestcloseBtn {
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  position: relative;
  width: 100%;
  height: 25px;
  line-height: 25px;
  /* font-weight: bold; */
  border-radius: 5cm;
  border: 1px solid rgba(0, 0, 0, 0.1);
  top: 15px;
}
</style>