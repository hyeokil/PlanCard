<template>
    <div class="card p-fluid">
      <div>
        <h1>My page</h1>
      </div>
      
      <hr id="separator">


        <div class="box card p-fluid" id="infoBox">
            <div id="profileImg">
                <img :src="memberInfo.image || '/로고 3.png'" alt="프로필 이미지">
            </div>
            <div id="userInfo">
                <p style="font-weight: bold; font-size: 40pt; color: #3498db;">{{ memberInfo.nickname }}</p>
                <p style="font-weight: bold; color: rgba(0, 0, 0, 0.5);"> {{ memberInfo.name }}</p>
                <p style="font-weight: bold; color: rgba(0, 0, 0, 0.5);"> {{  memberInfo.email }}</p>
            </div>

        </div>

        <div id="flexContainer">
            <div class="card p-fluid" id="myplanImg" @click="goMyPlan()">
                <img src="/myplan 아이콘.png" alt="myplan">
                <p>My Plan</p>
            </div>
            <div class="card p-fluid" id="infoModifyImg" @click="goMyInfoModify()">
                <img src="/회원정보수정 아이콘.png" alt="infoResetting">
                <p>회원 정보 수정</p>
            </div>
            <div class="card p-fluid" id="pwResettingImg" @click="goMyPasswordResetting()">
                <img src="/비밀번호변경 아이콘.png" alt="passwordResetting">
                <p>비밀번호 변경</p>
            </div>  
        </div>


    </div>
</template>



<script setup>
import { useRouter } from 'vue-router';
import { useAccountsStore } from '@/stores/accountsStore';

const router = useRouter();
const { memberInfo } = useAccountsStore();

const goMyPlan = () => {
  router.push({ name: "mypage-myplan" })
}

const goMyInfoModify = () => {
  router.push({ name: "mypage-myInfoModify" })
}

const goMyPasswordResetting = () => {
  router.push({ name: "mypage-myPasswordResetting" })
}


</script>



<style scoped>
  h1 {
    font-weight: bold;
  }
  #separator {
    border: none;
    border-top: 4px solid #3498DB;
    border-radius: 10px;
  }

  #infoBox {
    display: flex;
    justify-content: space-between;
    align-items: center; /* 수직 가운데 정렬 */
    width: 75%;
    margin: 0 auto; /* 좌우 마진을 auto로 설정하여 수평 가운데 정렬 */
    margin-bottom: 30px;

  }
  #profileImg {
    border-radius: 50%;
    border: 1px solid rgba(0, 0, 0, 0.3);
    background-color: #FFFFFF;
    width: 250px;
    height: 250px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative; /* 상대 위치 지정 */
    overflow: hidden; /* 원 밖으로 넘치는 부분 숨김 */
    margin-top: 10px;
    margin-bottom: 20px;
  }
  #profileImg img {
    max-width: none; /* 이미지 너비를 원본 크기로 설정 */
    max-height: none; /* 이미지 높이를 원본 크기로 설정 */
    width: 100%; /* 이미지 너비를 자동으로 조정 */
    height: auto; /* 이미지 높이를 자동으로 조정 */
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%); /* 이미지를 수평 및 수직 가운데 정렬 */
  }
  #userInfo {
    flex: 1; /* 남은 공간을 모두 차지하도록 설정 */
    margin-left: 20px; /* 좌측 여백 추가 */
  }


  #flexContainer {
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center; /* 수직 가운데 정렬 */
    widows: 75%;
  }

  img {
    width: 270px;
    height: 270px;
    opacity: 0.7; /* 이미지 투명도 설정 */
  }

  #myplanImg {
    margin-bottom: 0;
    margin-right: 20px;
    border-radius: 15px; /* 라운드 모서리 적용 */
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2); /* 그림자 적용 */
  }
  #infoModifyImg {
    margin-bottom: 0;
    margin-left: 20px;
    margin-right: 20px;
    border-radius: 15px; /* 라운드 모서리 적용 */
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2); /* 그림자 적용 */
  }
  #pwResettingImg {
    margin-left: 20px;
    border-radius: 15px; /* 라운드 모서리 적용 */
    box-shadow: 5px 5px 10px rgba(0, 0, 0, 0.2); /* 그림자 적용 */
  }

  p {
    font-weight: bold;
    font-size: xx-large;
    display: flex;
    justify-content: center; /* 수평 가운데 정렬 */
    align-items: center; /* 수직 가운데 정렬 */
  }
</style>