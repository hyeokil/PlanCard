<template>
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
      <Notification :notificationActive="topbarNotificationActive"
        :class="[topbarNotificationActive ? 'notificationActive' : 'notificationHidden']"
        @close-notification="closeNotification" />
    </div>

    <!-- 프로필 클릭 시 나오는 창 -->
    <div>
      <Profile :class="[topbarProfileActive ? 'profileActive' : 'profileHidden']" @close-profile="closeProfile" />
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
import { usePlanStore } from "@/stores/planStore";
import MeetingCreate from "@/components/meeting/MeetingCreate.vue";
import Notification from "@/layout/items/Notification.vue"
import Profile from "@/layout/items/Profile.vue"
const accountsStore = useAccountsStore()
const planStore = usePlanStore()
const router = useRouter()

const outsideClickListener = ref(null);
const topbarMenuActive = ref(false);


onMounted(() => {
  bindOutsideClickListener();
});

onBeforeUnmount(() => {
  unbindOutsideClickListener();
});

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


// MeetingCreate 팝업
let isMeeting = computed(() => planStore.isMeetingView)

// topbar에서 미팅 생성
const showCreateMeetingModal = ref(false)
const closeMeetingCreate = () => {
  showCreateMeetingModal.value = false;
}
const showCreateMeeting = () => {
  showCreateMeetingModal.value = !showCreateMeetingModal.value
};



// Notification 팝업
const topbarNotificationActive = ref(false);  // 알림 팝업 유무 변수
const closeNotification = () => {
  topbarNotificationActive.value = false;
}
// Notification on/off
const onTopBarMenuNotificationButton = () => {
  if (accountsStore.isLogin) {
    topbarMenuActive.value = !topbarMenuActive.value;
    topbarNotificationActive.value = !topbarNotificationActive.value;
    topbarProfileActive.value = false;

  } else if (!accountsStore.isLogin) {
    // router.push({name: "member-login"})
  }
};

// Profile 팝업
const topbarProfileActive = ref(false);  // 프로필 팝업 유무 변수
const closeProfile = () => {
  topbarProfileActive.value = false;
}
// Profile on/off
const onTopBarMenuProfileButton = () => {
  if (accountsStore.isLogin) {
    topbarMenuActive.value = !topbarMenuActive.value;
    topbarProfileActive.value = !topbarProfileActive.value;
    topbarNotificationActive.value = false;
  } else if (!accountsStore.isLogin) {
    router.push({ name: "member-login" })
  }
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
  background-color: transparent !important;
  /* 배경색을 투명으로 설정 */
  color: inherit !important;
  /* 글자색을 상속 받음 */
}

.myPlanBtn {
  margin: 10px;
}

.startBtn {
  margin: 10px;
  background-color: #3498DB;
  color: white;
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
}</style>

