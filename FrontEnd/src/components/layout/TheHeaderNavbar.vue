<template>
  <div>
    <v-app-bar flat>
    <!-- Logo -->
    <router-link to="/">
      <img src="/플랜카드.png" alt="로고" id="logo">
    </router-link>
    <!--  -->

    <v-container class="mx-auto d-flex align-center justify-center">
      
      <!-- navbar -->
      <v-btn class="btn">
        <router-link :to="{name: 'mypage-myplan'}" class="router-link-active"><b>My Plan</b></router-link>
      </v-btn>
      
      <v-btn class="btn" style="background-color: #3498DB; color: white;">
        <router-link :to="{name: 'meeting-create'}" class="router-link-active"><b>Start</b></router-link>
      </v-btn>
      <!--  -->

      <v-spacer></v-spacer>

      <div v-if="accountsStore.isLogin">
        <!-- Notification -->
        <div>
          <v-card-text>
            <v-menu v-model="notificationMenu" :close-on-content-click="false" location="end">
              <template v-slot:activator="{ props }">
                <v-btn color="indigo" v-bind="props">
                  <img src="/알림.png" alt="알림" id="notification">
                </v-btn>
              </template>

              <v-card min-width="300">
                <h2>Notification</h2>
                <button id="notificationClearBtn">모두지우기</button>
                <v-divider></v-divider>
                  <div>
                    <p>div에 v-for로 알림 데이터 받기</p>
                    <p>css 이쁘게</p>
                    <p>수락, 거절 버튼</p>
                    <p>회의 시작, 회의 초대, 친구 요청 수신 등</p>
                  </div>
                <v-divider></v-divider>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn variant="text" @click="notificationMenu = false" id="closeBtn">
                    <p><b>닫기</b></p>
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-menu>
          </v-card-text>
        </div>
        <!--  -->
      </div>

      <div v-if="accountsStore.isLogin">
        <!-- My Profile -->
        <div>
          <v-card-text>
            <v-menu v-model="profileMenu" :close-on-content-click="false" location="end">
              <template v-slot:activator="{ props }">
                <v-btn color="indigo" v-bind="props">
                  <!-- 추후에 src값을 url로 바인딩 -->
                  <img src="/프로필사진(예시).jpg" alt="프로필사진" id="profile">
                </v-btn>
              </template>
              
              <v-card min-width="300">
                <v-list>
                  <!-- 추후에 prepend-avatar값을 url로 바인딩 -->
                  <!-- title, subtitle을 이름과 이메일로 각각 바인딩 -->
                  <v-list-item prepend-avatar="/프로필사진(예시).jpg" title="이름" subtitle="이메일"></v-list-item>
                </v-list>
                <div style="text-align: center;" @click="goMyPage()">
                  <button id="myPageBtn">My Page</button>
                </div>
                <div style="text-align: center;">
                  <button id="logOutBtn">로그아웃</button>
                </div>
                <v-divider></v-divider>
                <div>
                  <p>친구목록 v-for로 만들기 !</p>
                </div>
                <v-divider></v-divider>
                <v-card-actions>
                  <v-spacer></v-spacer>
                    <v-btn variant="text" @click="notificationMenu = false" id="closeBtn">
                      <p><b>닫기</b></p>
                    </v-btn>
                </v-card-actions>
              </v-card>
            </v-menu>
          </v-card-text>
        </div>
        <!--  -->
      </div>

      <div v-else>
        <router-link :to="{name: 'member-login'}">
          <v-card-text>
            <v-menu v-model="profileMenu" :close-on-content-click="false" location="end">
              <template v-slot:activator="{ props }">
                <v-btn color="indigo" v-bind="props">
                  <img src="/프로필로고(로그아웃상태).png" alt="프로필사진" id="profile">
                </v-btn>
              </template>
            </v-menu>
          </v-card-text>
        </router-link>
      </div>
        
    </v-container>
  </v-app-bar>
  </div>
</template>



<script setup>

  import { ref } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAccountsStore } from '@/stores/accounts';


  const router = useRouter()
  const accountsStore = useAccountsStore()


  // 마이페이지로 이동 (미완성-component, router추가)
  const goMyPage = function () {
    router.push({name: ""})
  }

  // modal 상태 변수
  const notificationMenu = ref(false);
  const profileMenu = ref(false);

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
    padding-bottom: 13px;
  }

  #notification {
    width : auto;
    height: 30px;
    border-radius: 50%;
  }

  .btn {
    margin: 10px;
  }

  .router-link-active { /* router-link의 글자 색이 변하지 않게 하는 css */
  background-color: transparent !important; /* 배경색을 투명으로 설정 */
  color: inherit !important; /* 글자색을 상속 받음 */
}

  #closeBtn {
    border: solid black 1px;
  }

  #myPageBtn {
    border: solid black 1px;
  }

  #logOutBtn {
    border: solid black 1px;
  }

  #notificationClearBtn {
    border: solid black 1px;
  }
</style>@/stores/accountStore