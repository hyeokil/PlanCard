<template>
    <div>
        <v-app-bar flat>
        <router-link to="/">
          <img src="@/assets/플랜카드.png" alt="로고" id="logo" @click="goHome()">
        </router-link>

        <v-container class="mx-auto d-flex align-center justify-center">
          
          <v-btn class="btn">
            <router-link :to="{name: 'mypage-myplan'}" class="router-link-active"><b>My Plan</b></router-link>
          </v-btn>
          
          <v-btn class="btn" style="background-color: #3498DB; color: white;">
            <router-link :to="{name: 'meeting-create'}" class="router-link-active"><b>Start</b></router-link>
          </v-btn>

          <v-spacer></v-spacer>
          <div @click="memberStore.openMyPageAlarm()">
            <img src="@/assets/알림.png" alt="알림" id="notification">
          </div>
          <div>
            <img src="@/assets/프로필로고.png" alt="프로필사진" id="profile">
          </div>
        </v-container>
      </v-app-bar>
      <v-dialog v-model="memberStore.dialog" max-width="600" max-height="900">
        <v-card>
          <v-card-title>모달 제목</v-card-title>
          <v-card-text>
            <h1>MyPageAlarm.vue</h1>
          </v-card-text>
          <v-card-actions>
            <v-btn @click="memberStore.closeMyPageAlarm()">닫기</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
</template>



<script setup>
  import { useRouter } from 'vue-router';
  import { useMemberStore } from '@/stores/memberStore';

  const memberStore = useMemberStore();
  const router = useRouter()

  const goHome = function () {
    router.push({name: 'main'});
  }

</script>



<style scoped>
  #logo {
    width : auto;
    height: 50px;
    border-radius: 50%;
    margin: 10px 0px 10px 50px;
    padding-left: 20px;
  }

  #profile {
    width : auto;
    height: 50px;
    border-radius: 50%;
    margin: 10px;
  }

  #notification {
    width : auto;
    height: 30px;
    border-radius: 50%;
    margin: 10px;
  }

  .btn {
    margin: 10px;
  }

  .router-link-active { /* router-link의 글자 색이 변하지 않게 하는 css */
  background-color: transparent !important; /* 배경색을 투명으로 설정 */
  color: inherit !important; /* 글자색을 상속 받음 */
}
</style>