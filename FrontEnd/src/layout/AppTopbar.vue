<template>
  <!-- 로고
<router-link :to="{ name: 'main' }" class="layout-topbar-logo">
<img src="/로고 3.png" alt="로고" style="height: 100%; width: 55px; border-radius: 50%;" />
<span>Plan Card</span>
</router-link> -->
  <div class="layout-topbar" v-if="!isMeeting">
    <!-- 로고 -->
    <router-link :to="{ name: 'main' }" class="layout-topbar-logo">
      <img src="/로고 3.png" alt="로고" style="height: 100%; width: 55px; border-radius: 50%;" />
      <span>Plan Card</span>
    </router-link>

    <v-spacer></v-spacer>

    <!-- Button -->
    <v-btn class="myPlanBtn" v-show="accountsStore.isLogin">
      <router-link :to="{ name: 'mypage-myplan' }" class="router-link-active"><b>나의 계획</b></router-link>
    </v-btn>

    <v-btn class="startBtn" v-show="accountsStore.isLogin">
      <p class="router-link-active" @click="showCreateMeeting"><b>시작</b></p>
    </v-btn>


    <div class="layout-topbar-menu" :class="topbarMenuClasses">
      <!-- Notification -->
      <button @click="onTopBarMenuNotificationButton()" class="p-link layout-topbar-button"
        v-show="accountsStore.isLogin">
        <i class="pi pi-bell"></i>
        <span>Plan</span>
      </button>
      <!-- Profile -->
      <button @click="onTopBarMenuProfileButton()" class="p-link layout-topbar-button" v-show="accountsStore.isLogin">
        <i class="pi pi-user"></i>
        <span>Profile</span>
      </button>
      <p id="userName" @click="onTopBarMenuProfileButton()" :class="{ 'userName-hover': !accountsStore.isLogin }">
        {{ accountsStore.isLogin ? accountsStore.memberInfo?.nickname : '로그인' }}
      </p>
      <p style="padding-top: 10px;" v-show="accountsStore.isLogin">님</p>
    </div>



    <!-- 알림 클릭 시 나오는 창 -->
    <div>
      <v-card :class="[topbarNotificationActive ? 'notificationActive' : 'notificationHidden']" id="popUp">
        <div style="display: flex; justify-content: space-between; margin-bottom: -15px;">
          <p id="notificatonSet">알림 내역</p>
          <v-btn id="clearBtn">모두 지우기</v-btn>
        </div>
        <div id="notificationsList">
          <div id="notificationDivider1"></div>
          <div v-for="notification in notifications" :key="notification.index" style="width: 95%;">
            <div style="display: flex;">
              <p id="notification">{{ notification.content }}</p>
              <div style="display: flex; align-items: center;">
                <i class="pi pi-check" id="acceptBtn" @click="handleAlarm(notification.alarmId, 'ACCEPT')" style="font-size: 1.5rem; 
              border: 1px solid rgba(255, 255, 255, 0.5);
              border-radius: 50%;
              padding: 5px;
              background-color: #3498DB;
              color: white;
              width: 35px;
              height: 35px;
              margin-right: 3px;"></i>
                <i class="pi pi-times" id="rejectBtn" @click="handleAlarm(notification.alarmId, 'REJECT')" style="font-size: 1.5rem; 
              border: 1px solid rgba(255, 255, 255, 0.5);
            border-radius: 50%;
              padding: 5px;
              background-color: red;
              color: white;
              width: 35px;
              height: 35px;
              margin-left: 3px;"></i>
              </div>
            </div>
            <div id="notificationDivider2"></div>
          </div>
        </div>
        <v-card-actions>
          <v-btn variant="text" @click="topbarNotificationActive = false" id="closeBtn">
            <p>닫기</p>
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>

    <!-- 프로필 클릭 시 나오는 창 -->
    <div>
      <v-card :class="[topbarProfileActive ? 'profileActive' : 'profileHidden']" id="popUp">
        <v-list id="profileInfo">
          <v-list-item :prepend-avatar="accountsStore.memberInfo?.image ? accountsStore.memberInfo.image : '/로고 3.png'">
            <template #title>
              <span style="font-size: 18px; color: #3498DB;">
                {{ accountsStore.memberInfo?.name }}
              </span>
            </template>
            <template #subtitle>
              <span style="font-size: 15px; ">{{ accountsStore.memberInfo?.email }}</span>
            </template>
          </v-list-item>
        </v-list>
        <div @click="goMyPage()" style="text-align: center;">
          <v-btn id="myPageBtn">내 정보</v-btn>
          <v-btn id="logOutBtn" @click="logout()">로그아웃</v-btn>
        </div>
        <v-divider></v-divider>
        <div id="friendsList">
          <div style="display: flex;">
            <p
              style="font-size: 20px; padding-left: 10px; margin-bottom: 0px; margin-top: 3px; color: rgba(0, 0, 0, 0.5);">
              친구 목록</p>
            <v-btn id="requestBtn" @click="FriendRequest()">친구 요청</v-btn>
          </div>
          <div v-for="friend in friends" :key="friend.index" id="friendInfo">
            <div>
              <img :src=friend.profileImg alt="프로필 이미지" id="friendProfileImg">
            </div>
            <div>
              <div style="display: flex;">
                <p id="friendName">{{ friend.name }} </p>
                <p :class="friend.status ? 'onLine' : 'offLine'">&nbsp;●</p>
              </div>
              <p id="friendEmail">{{ friend.email }}</p>
            </div>
          </div>
        </div>
        <v-divider></v-divider>
        <v-card-actions>
          <v-btn variant="text" @click="topbarProfileActive = false" id="closeBtn">
            <p>닫기</p>
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>

    <!-- 친구 요청 창 -->
    <div id="overlay" v-if="friendRequestActive"></div>
    <div class="card p-fluid" v-if="friendRequestActive" id="friendRequestBox">
      <h3 id="friendRequestTitle">친구 요청</h3>
      <form class="box, card p-fluid" @submit.prevent="SendFriendRequest()" id="friendRequestForm">
        <input class="box card p-fluid" type="email" id="friendRequestInput" v-model="friendEmail"
          placeholder="친구의 Email">
        <input class="box card p-fluid" type="submit" id="friendRequestSubmit" value="요청 보내기">
      </form>
      <div>
        <v-btn @click="FriendRequest()" id="friendsRequestcloseBtn">닫기</v-btn>
      </div>
    </div>

    <!-- 미팅 생성 창 -->
    <div id="overlay" v-if="showCreateMeetingModal"></div>
    <div id="createMeetingBox">
      <MeetingCreate v-if="showCreateMeetingModal" @close-meeting-create="closeMeetingCreate" />
    </div>



  </div>
</template>


<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import { useRouter } from 'vue-router';
import { useAccountsStore } from '@/stores/accountsStore';
import MeetingCreate from "@/components/meeting/MeetingCreate.vue";
import { memberLogoutApi } from "@/api/memberApi";
import { alarmFriendRequestApi, alarmGetListApi, alarmActionApi } from "@/api/alarmApi";
import { usePlanStore } from "@/stores/planStore";
const accountsStore = useAccountsStore()
const planStore = usePlanStore()
const router = useRouter()

const outsideClickListener = ref(null);
const topbarMenuActive = ref(false);
const topbarNotificationActive = ref(false);  // 알림 팝업 유무 변수
const topbarProfileActive = ref(false);  // 프로필 팝업 유무 변수
const friendRequestActive = ref(false);  // 친구요청창

const friendEmail = ref(``);  // 친구 요청할 때 입력하는 email

let isMeeting = computed(() => planStore.isMeetingView)

onMounted(() => {
  bindOutsideClickListener();
});

onBeforeUnmount(() => {
  unbindOutsideClickListener();
});


// const onTopBarMenuNotificationButton = () => {
//     topbarMenuActive.value = !topbarMenuActive.value;
//     topbarNotificationActive.value = !topbarNotificationActive.value;    
// };
const topbarMenuClasses = computed(() => {
  return {
    'layout-topbar-menu-mobile-active': topbarMenuActive.value
  };
});
const bindOutsideClickListener = () => {
  if (!outsideClickListener.value) {
    outsideClickListener.value = (event) => {
      if (isOutsideClicked(event)) {
        topbarMenuActive.value = false;
      }
    };
    document.addEventListener('click', outsideClickListener.value);
  }
};
const unbindOutsideClickListener = () => {
  if (outsideClickListener.value) {
    document.removeEventListener('click', outsideClickListener);
    outsideClickListener.value = null;
  }
};
const isOutsideClicked = (event) => {
  if (!topbarMenuActive.value) return;

  const sidebarEl = document.querySelector('.layout-topbar-menu');
  const topbarEl = document.querySelector('.layout-topbar-menu-button');

  return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target));
};


// 로그아웃 요청 메서드
const logout = async () => {
  try {
    await memberLogoutApi(
      (response) => {
        if (response.data.dataHeader.successCode === 0) {
          accountsStore.setLogout();
          router.push({ name: 'main' });  // 로그아웃 성공 후 메인 페이지로 이동
        }
        // 서버에서 실패 응답 받았을 경우 (즉, redis에 저장된 refresh 토큰 만료돼서 삭제 된 경우인데 이럴 일 사실상 거의 없음)
        else {
          alert(response.data.dataHeader.resultMessage);
          accountsStore.setLogout();
        }
      }
    )
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
}










// 알림 팝업 부분 코드 시작
const onTopBarMenuNotificationButton = () => {
  if (accountsStore.isLogin) {
    topbarMenuActive.value = !topbarMenuActive.value;
    // 알림 목록을 새로고침하고 팝업을 토글합니다.
    if (!topbarNotificationActive.value) {
      // 알람 목록을 비우고 새로고침하기 전에, 알람 목록이 열리지 않았을 때만 새로고침하도록 합니다.
      notifications.value = []; // 기존 알람 목록을 비우고
      lastAlarmId.value = null; // 마지막 알람 ID를 리셋합니다.
      fetchAlarms(); // 알람 목록을 새로고침합니다.
    }
    topbarNotificationActive.value = !topbarNotificationActive.value;
    topbarProfileActive.value = false;

  } else if (!accountsStore.isLogin) {
    // router.push({name: "member-login"})
  }
};

// 알림 팝업 부분 코드 끝

const lastAlarmId = ref(null);
const notifications = ref([]);

// 알람 가져오기 메서드
const fetchAlarms = async () => {
  try {
    const response = await alarmGetListApi(lastAlarmId.value);
    if (response.data.dataHeader.successCode === 0) {
      const fetchedNotifications = response.data.dataBody;
      if (fetchedNotifications.length) {
        notifications.value.push(...fetchedNotifications);
        lastAlarmId.value = fetchedNotifications[fetchedNotifications.length - 1].alarmId;
      }
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
};


// 프로필 팝업 부분 코드 시작
// 프로필 팝업 on/off
const onTopBarMenuProfileButton = () => {
  if (accountsStore.isLogin) {
    topbarMenuActive.value = !topbarMenuActive.value;
    topbarProfileActive.value = !topbarProfileActive.value;
    topbarNotificationActive.value = false;
  } else if (!accountsStore.isLogin) {
    router.push({ name: "member-login" })
  }
};
// MyPage로 이동
const goMyPage = function () {
  router.push({ name: "member-mypage" });
  topbarProfileActive.value = false;
}
// 친구 목록 배열 (test)
const friends = ref([
  {
    name: "신동근",
    email: "tlsehdrms95@ssafy.com",
    profileImg: "/로고 3.png",
    status: true
  },
  {
    name: "박정인",
    email: "qkrwjddls96@ssafy.com",
    profileImg: "/로고 3.png",
    status: false
  },
  {
    name: "강지수",
    email: "rkdwltn96@ssafy.com",
    profileImg: "/로고 3.png",
    status: true
  },
  {
    name: "김혁일",
    email: "rlagurdlf97@ssafy.com",
    profileImg: "/로고 3.png",
    status: false
  },
  {
    name: "김재훈",
    email: "rlawogns98@ssafy.com",
    profileImg: "/로고 3.png",
    status: true
  },
  {
    name: "이세은",
    email: "dltpdms99@ssafy.com",
    profileImg: "/로고 3.png",
    status: true
  }
]);

// 친구요청 팝업 on/off
const FriendRequest = () => {
  friendRequestActive.value = !friendRequestActive.value;
  friendEmail.value = "";
};

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
        friendRequestActive.value = false;
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


// 알람 처리 메서드 (수락/거절)
const handleAlarm = async (alarmId, action) => {
  try {
    // alarmApi.js에서 export한 함수를 사용하여 백엔드 API 호출
    const response = await alarmActionApi({ alarmId, action });
    if (response.data.dataHeader.successCode === 0) {
      // 성공적으로 처리된 경우, 사용자에게 알림을 보내거나 목록을 업데이트
      alert("알람 처리 성공");
      // 알람 목록을 새로고침하거나 수정된 항목을 업데이트하는 로직을 추가할 수 있습니다.

      // 알람 목록을 비우고 새로고침
      notifications.value = []; // 기존 알람 목록을 비웁니다.
      lastAlarmId.value = null; // 마지막 알람 ID를 리셋합니다.
      await fetchAlarms();  // 다시 알람 목록 불러오기
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
};

// 프로필 팝업 부분 코드 끝

// topbar에서 미팅 생성
const showCreateMeetingModal = ref(false)
const closeMeetingCreate = () => {
  showCreateMeetingModal.value = false;
}
const showCreateMeeting = () => {
  showCreateMeetingModal.value = !showCreateMeetingModal.value
};

</script>



<style scoped>
.profileActive {
  display: block;
  position: absolute;
  right: 1.8%;
  top: 80%;
}

.profileHidden {
  display: none;
}

.notificationActive {
  display: block;
  position: absolute;
  right: 1.8%;
  top: 80%;
}

.notificationHidden {
  display: none;
}

.router-link-active {
  /* router-link의 글자 색이 변하지 않게 하는 css */
  background-color: transparent !important;  /* 배경색을 투명으로 설정 */
  color: inherit !important;  /* 글자색을 상속 받음 */
}

.myPlanBtn {
  margin: 10px;
}

.startBtn {
  margin: 10px;
  background-color: #3498DB;
  color: white;
}




#notificatonSet {
  font-size: 25px;
  /* font-weight: bold; */
  color: #3498DB;
  margin-left: 3px;
}

#clearBtn {
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  width: 30%;
  height: 25px;
  /* font-weight: bold; */
  border-radius: 5cm;
  border: 1px solid rgba(0, 0, 0, 0.1);
  margin-top: 5px;
  margin-right: 15px;
  font-size: 13px;
}
#clearBtn:hover {
  transform: scale(1.05);
  border-color: #3498db;
}

#notificationsList {
  max-height: 200px;
  overflow-y: auto;
  width: 100%;
  border-radius: 15px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  margin-bottom: -20px;
}

#notification {
  margin: 10px;
  width: 80%;
}
#acceptBtn:hover {
  transform: scale(1.1);
}
#rejectBtn:hover {
  transform: scale(1.1);
}

#notificationDivider1 {
  margin-left: 10px;
  width: 90%;
  height: 1px;
  background-color: #FFFFFF;
  margin-bottom: 10px;
}

#notificationDivider2 {
  margin-left: 10px;
  width: 100%;
  height: 1px;
  background-color: #ccc;
  margin-top: 10px;
  /* 구분선과 알림 사이의 간격 조절 */
  margin-bottom: 10px;
  /* 구분선 아래의 알림과의 간격 조절 */
}





#userName {
  padding-top: 10px;
  /* font-weight: 900; */
  color: #3498DB;
  cursor: pointer;
}

.userName-hover {
  cursor: pointer;
  /* 마우스 커서를 포인터로 변경 */
  color: #3498DB;
  /* 텍스트 색상 변경 */
  text-decoration: underline;
  /* 밑줄 스타일 적용 */
}

.userName-hover:hover {
  background-color: rgba(52, 152, 219, 0.2);
  /* 호버 시 배경 색상 적용 */
  border-radius: 5px;
  /* 둥근 모서리 스타일 적용 */
  padding: 5px;
  /* 패딩 적용 */
}

#popUp {
  width: 330px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  border-radius: 5%;
  padding: 7px;
  top: 60px;
}

#profileInfo {
  width: 100%;
  border-radius: 15px;
  border: 1px solid rgba(52, 152, 219, 0.5);
}

#myPageBtn {
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  position: relative;
  top: 5px;
  margin-right: 1%;
  width: 49%;
  height: 30px;
  padding: 0.8%;
  border-radius: 5cm;
  border: 1px solid rgba(52, 152, 219, 0.5);
  font-size: 15px;
}
#myPageBtn:hover {
  transform: scale(1.05);
    border-color: #3498db;
}

#logOutBtn {
  text-align: center;
  color: rgba(0, 0, 0, 0.5);
  background-color: #FFFFFF;
  position: relative;
  top: 5px;
  margin-left: 1%;
  width: 49%;
  height: 30px;
  padding: 0.8%;
  border-radius: 5cm;
  /* border: 1px solid rgba(52, 152, 219, 0.5); */
  font-size: 15px;
}
#logOutBtn:hover {
  transform: scale(1.05);
  border-color: #3498db;
}

#friendsList {
  background-color: #FFFFFF;
  border-radius: 15px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: -50px;
  margin-top: -17px;
}

#requestBtn {
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  position: relative;
  /* font-weight: bold; */
  border-radius: 5cm;
  border: 1px solid rgba(0, 0, 0, 0.1);

  width: 22%;
  height: 15%;
  font-size: 11px;

  margin-left: 120px;
  margin-top: 5px;
  padding: 3px 10px 3px 10px;
}
#requestBtn:hover {
  transform: scale(1.05);
  border-color: #3498db;
}

#friendInfo {
  display: flex;
  align-items: center;
  margin-bottom: -10px;
}

#friendProfileImg {
  border-radius: 50%;
  width: 50px;
  height: 50px;
}

#friendName {
  color: #3498DB;
  font-size: 18px;
  /* font-weight: bold; */
  margin-bottom: 0;
}

#friendEmail {
  color: rgba(0, 0, 0, 0.5);
  font-size: 13px;
  margin-top: -7px;
  margin-bottom: 5px;
  margin-left: 1px;
}

#closeBtn {
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
#closeBtn:hover {
  transform: scale(1.05);
  border-color: #3498db;
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

#createMeetingBox {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 998;
  max-height: 120vh;
  max-width: 120vw;
  overflow-y: auto;
}


  #moreBtn {
    display: block;
    margin: 0 auto;
    color: #FFFFFF;
    background-color: #3498DB;
    position: absolute;
    right: 1.8%;
    top: 80%;
  }


.onLine {
  color: #2ECC71;
}

.offLine {
  color: #808080;
}
</style>