<template>
    <div class="layout-topbar">

        <!-- 로고 -->
        <router-link :to="{name: 'main'}" class="layout-topbar-logo">
            <img src="/로고 3.png" alt="로고" style="height: 100%; width: 55px; border-radius: 50%;"/>
            <span>Plan Card</span>
        </router-link>

        <v-spacer></v-spacer>

        <!-- Button -->
        <v-btn class="myPlanBtn" v-show="accountsStore.isLogin">
            <router-link :to="{name: 'mypage-myplan'}" class="router-link-active"><b>My Plan</b></router-link>
        </v-btn>

        <v-btn class="startBtn" v-show="accountsStore.isLogin">
            <p class="router-link-active" @click="showCreateMeeting"><b>Start</b></p>
        </v-btn>


        <div class="layout-topbar-menu" :class="topbarMenuClasses">
            <!-- Notification -->
            <button @click="onTopBarMenuNotificationButton()" class="p-link layout-topbar-button" v-show="accountsStore.isLogin">
                <i class="pi pi-bell"></i>
                <span>Plan</span>
            </button>
            <!-- Profile -->
            <button @click="onTopBarMenuProfileButton()" class="p-link layout-topbar-button" v-show="accountsStore.isLogin">
                <i class="pi pi-user"></i>
                <span>Profile</span>
            </button>
            <p id="userName" @click="onTopBarMenuProfileButton()" :class="{'userName-hover': !accountsStore.isLogin}">
              {{ accountsStore.isLogin ? accountsStore.memberInfo.nickname : '로그인' }}
            </p>
            <p style="padding-top: 10px; font-weight: 900;" v-show="accountsStore.isLogin">님</p>
        </div>



        <!-- 알림 클릭 시 나오는 창 -->
        <div>
            <v-card :class="[topbarNotificationActive? 'notificationActive':'notificationHidden']" id="popUp">
                <div style="display: flex; justify-content: space-between; margin-bottom: -15px;">
                    <p id="notificatonSet">알림 내역</p>
                    <button id="clearBtn">모두 지우기</button>
                </div>
                <div id="notificationsList">
                    <div id="notificationDivider1"></div>
                    <div v-for="notification in notifications" :key="notification.index">
                        <p id="notification">{{ notification.name }}&nbsp;{{ notification.content }}</p>
                        <div id="notificationDivider2"></div>
                    </div>
                </div>
                <v-card-actions>
                    <v-btn variant="text" @click="topbarNotificationActive = false" id="closeBtn">
                        <p>Close</p>
                    </v-btn>
                </v-card-actions>
            </v-card>
        </div>
        
        <!-- 프로필 클릭 시 나오는 창 -->
        <div>
            <v-card :class="[topbarProfileActive? 'profileActive':'profileHidden']" id="popUp">
                <v-list id="profileInfo">
                    <v-list-item prepend-avatar="/로고 3.png">
                        <template #title>
                            <span style="font-size: 18px; font-weight: bold; color: #3498DB;">{{ accountsStore.memberInfo?.name }}</span>
                        </template>
                        <template #subtitle>
                            <span style="font-size: 15px; ">{{ accountsStore.memberInfo?.email }}</span>
                        </template>
                    </v-list-item>
                </v-list>
                <div @click="goMyPage()" style="text-align: center;">
                    <button id="myPageBtn">마이 페이지</button>
                    <button id="logOutBtn" @click="logout()">로그아웃</button>
                </div>
                <v-divider></v-divider>
                <div id="friendsList">
                    <div style="display: flex;">
                        <p style="font-weight: bold; font-size: 20px; padding-left: 10px; margin-bottom: 0px; color: rgba(0, 0, 0, 0.5);">친구 목록</p>
                        <!-- <button id="requestedBtn">받은 신청</button> -->
                        <button id="requestBtn" @click="FriendRequest()">친구 요청</button>
                    </div>
                    <div v-for="friend in friends" :key="friend.index" id="friendInfo">
                        <div>
                            <img :src=friend.profileImg alt="프로필 이미지" id="friendProfileImg">
                        </div>
                        <div>
                            <div style="display: flex;">
                                <p id="friendName">{{ friend.name}} </p>
                                <p :class="friend.status ? 'onLine' : 'offLine'">&nbsp;●</p>
                            </div>
                            <p id="friendEmail">{{ friend.email }}</p>
                        </div>
                    </div>
                </div>
                <v-divider></v-divider>
                <v-card-actions>
                    <v-btn variant="text" @click="topbarProfileActive = false" id="closeBtn">
                        <p>Close</p>
                    </v-btn>
                </v-card-actions>
            </v-card>
        </div>  

        <!-- 친구 요청 창 -->
        <div id="overlay" v-if="friendRequestActive"></div>
        <div class="card p-fluid" v-if="friendRequestActive" id="friendRequestBox">
            <h3 id="friendRequestTitle">친구 요청</h3>
                <form class="box, card p-fluid" @submit.prevent="SendFriendRequest()" id="friendRequestForm">
                    <input class="box card p-fluid" type="email" id="friendRequestInput" v-model="friendEmail" placeholder="친구의 Email">
                    <input class="box card p-fluid" type="submit" id="friendRequestSubmit" value="요청 보내기">
                </form>
            <div>
                <button @click="FriendRequest()" id="friendsRequestcloseBtn">CLOSE</button>
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

const accountsStore = useAccountsStore()
const router = useRouter()

const outsideClickListener = ref(null);
const topbarMenuActive = ref(false);
const topbarNotificationActive = ref(false);  // 알림 팝업 유무 변수
const topbarProfileActive = ref(false);  // 프로필 팝업 유무 변수
const friendRequestActive = ref(false);  // 친구요청창

const friendEmail = ref("");  // 친구 요청할 때 입력하는 email


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
    console.error(error);
    alert("로그아웃 과정 중 문제가 발생했습니다.");
  }
}










// 알림 팝업 부분 코드 시작
    const onTopBarMenuNotificationButton = () => {
        if (accountsStore.isLogin) {
            topbarMenuActive.value = !topbarMenuActive.value;
            topbarNotificationActive.value = !topbarNotificationActive.value;
            topbarProfileActive.value = false;

        } else if (!accountsStore.isLogin) {
            router.push({name: "member-login"})
        }
    };
    // 알림 목록 배열 (test)
    const notifications = ref([
        {
            name: "신동근",
            content: "님이 친구 요청을 하셨습니다.",
        },
        {
            name: "신동근",
            content: "님이 회의를 시작 하셨습니다. (홍대 테이스티버거 뿌수기)",
        },
        {
            name: "신동근",
            content: "님이 친구 요청을 하셨습니다.",
        },
        {
            name: "신동근",
            content: "님이 회의를 시작 하셨습니다. (홍대 테이스티버거 뿌수기)",
        },
        {
            name: "신동근",
            content: "님이 친구 요청을 하셨습니다.",
        },
        {
            name: "신동근",
            content: "님이 회의를 시작 하셨습니다. (홍대 테이스티버거 뿌수기)",
        },
        {
            name: "신동근",
            content: "님이 친구 요청을 하셨습니다.",
        },
        {
            name: "신동근",
            content: "님이 회의를 시작 하셨습니다. (홍대 테이스티버거 뿌수기)",
        },
    ]);
// 알림 팝업 부분 코드 끝



// 프로필 팝업 부분 코드 시작
    // 프로필 팝업 on/off
    const onTopBarMenuProfileButton = () => {
        if (accountsStore.isLogin) {
            topbarMenuActive.value = !topbarMenuActive.value;
            topbarProfileActive.value = !topbarProfileActive.value;
            topbarNotificationActive.value = false;
        } else if (!accountsStore.isLogin) {
            router.push({name: "member-login"})
        }
    };
    // MyPage로 이동
    const goMyPage = function () {
      router.push({name: "member-mypage"});
      topbarProfileActive.value = false;
    }
    // 친구 목록 배열 (test)
    const friends = ref([
        {
            name: "신동근",
            email: "tlsehdrms95@ssafy.com",
            profileImg: "/로고 3.png",
            status : true
        },
        {
            name: "박정인",
            email: "qkrwjddls96@ssafy.com",
            profileImg: "/로고 3.png",
            status : false
        },
        {
            name: "강지수",
            email: "rkdwltn96@ssafy.com",
            profileImg: "/로고 3.png",
            status : true
        },
        {
            name: "김혁일",
            email: "rlagurdlf97@ssafy.com",
            profileImg: "/로고 3.png",
            status : false
        },
        {
            name: "김재훈",
            email: "rlawogns98@ssafy.com",
            profileImg: "/로고 3.png",
            status : true
        },
        {
            name: "이세은",
            email: "dltpdms99@ssafy.com",
            profileImg: "/로고 3.png",
            status : true
        }
    ]);

    // 친구요청 팝업 on/off
    const FriendRequest = () => {
        friendRequestActive.value = !friendRequestActive.value;
        friendEmail.value = "";
    };

    const SendFriendRequest = () => {
        // 친구 요청 로직
    }
// 프로필 팝업 부분 코드 끝

    // topbar에서 미팅 생성
    const showCreateMeetingModal = ref(false)
    const closeMeetingCreate = () => {
        showCreateMeetingModal.value = false;
    }
    const showCreateMeeting = () => {
        showCreateMeetingModal.value = !showCreateMeetingModal.value
    }
  
  // 로그아웃
  const logOut = async () => {
    try {
      await memberLogoutApi((response) => {
        if (response.data.dataHeader.successCode === 0) {
          alert("로그아웃 되었습니다.");
          router.push('');
        } else {
          alert(response.data.dataHeader.resultMessage);
        }
      });
    } catch (error) {
      console.error(error);
      alert("로그아웃 중 오류가 발생했습니다.");
    }
  }
</script>



<style scoped>
  .profileActive{
    display: block;
    position: absolute;
    right: 1.8%;
    top: 80%;
  }
  .profileHidden{
    display: none;
  }
  .notificationActive{
    display: block;
    position: absolute;
    right: 1.8%;
    top: 80%;
  }
  .notificationHidden{
    display: none;
  }
  .router-link-active { /* router-link의 글자 색이 변하지 않게 하는 css */
    background-color: transparent !important; /* 배경색을 투명으로 설정 */
    color: inherit !important; /* 글자색을 상속 받음 */
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
    font-weight: bold;
    color: #3498DB;
    margin-left: 3px;
  }
  #clearBtn {
    text-align: center;
    color: #FFFFFF;
    background-color: #3498DB;
    width: 30%;
    height: 100%;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 5px;
    font-size: 13px;
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
    margin-left: 12px;
    width: 90%;
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
    width: 90%;
    height: 1px;
    background-color: #ccc;
    margin-top: 10px; /* 구분선과 알림 사이의 간격 조절 */
    margin-bottom: 10px; /* 구분선 아래의 알림과의 간격 조절 */
  }





  #userName {
    padding-top: 10px;
    font-weight: 900;
    color: #3498DB;
  }
  .userName-hover {
    cursor: pointer; /* 마우스 커서를 포인터로 변경 */
    color: #3498DB; /* 텍스트 색상 변경 */
    text-decoration: underline; /* 밑줄 스타일 적용 */
  }
  .userName-hover:hover {
    background-color: rgba(52, 152, 219, 0.2); /* 호버 시 배경 색상 적용 */
    border-radius: 5px; /* 둥근 모서리 스타일 적용 */
    padding: 5px; /* 패딩 적용 */
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
    padding: 0.8%;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(52, 152, 219, 0.5);

  }
  #logOutBtn {
    text-align: center;
    color: rgba(0, 0, 0, 0.5);
    background-color: #FFFFFF;
    position: relative;
    top: 5px;
    margin-left: 1%;
    width: 49%;
    padding: 0.8%;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(52, 152, 219, 0.5);
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
  #requestedBtn {
    text-align: center;
    color: #FFFFFF;
    background-color: #3498DB;
    position: relative;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    width: 22%;
    height: 10%;
    font-size: 11px;
    margin-right: 2px;
    margin-left: 40px;
    margin-top: 4px;
  }
  #requestBtn {
    text-align: center;
    color: #FFFFFF;
    background-color: #3498DB;
    position: relative;
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    width: 22%;
    height: 10%;
    font-size: 11px;
    margin-left: 100px;
    margin-top: 4px;
    margin-right: 3px;
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
    font-weight: bold;
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
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    top: 15px;
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
    font-weight: bold;
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
    text-align: center; /* 입력 내용을 가운데 정렬합니다. */
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
    font-weight: bold;
    border-radius: 5cm;
    border: 1px solid rgba(0, 0, 0, 0.1);
    padding: 10px;
    margin-top: 5px; /* 위쪽 여백 추가 */
    line-height: 0px;

  }
  #friendsRequestcloseBtn {
    text-align: center;
    color: #FFFFFF;
    background-color: #3498DB;
    position: relative;
    width: 100%;
    height: 25px;
    line-height: 25px;
    font-weight: bold;
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
    background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 */
    z-index: 997; /* 모달보다 한 단계 낮은 z-index */
  }
  #createMeetingBox {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 998;
    max-height: 70vh;
    overflow-y: auto;
  }



  .onLine {
    color: #2ECC71;
  }
  .offLine {
    color: #808080;
  }
</style>
