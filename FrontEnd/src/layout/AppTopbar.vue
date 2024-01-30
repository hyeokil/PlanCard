<template>
    <div class="layout-topbar mb-0 pb-0">
        <!-- 로고 -->
        <router-link to="/main" class="layout-topbar-logo">
            <img src="/로고 3.png" alt="로고" style="height: 100%; width: 55px; border-radius: 50%;"/>
            <span>Plan Card</span>
        </router-link>

        <v-spacer></v-spacer>


        <!-- Button -->
        <v-btn class="myPlanBtn">
            <router-link :to="{name: 'mypage-myplan'}" class="router-link-active"><b>My Plan</b></router-link>
        </v-btn>
        
        <v-btn class="startBtn">
            <router-link :to="{name: 'meeting-create'}" class="router-link-active"><b>Start</b></router-link>
        </v-btn>
        <!--  -->

        <div class="layout-topbar-menu" :class="topbarMenuClasses">
            <!-- Notification -->
            <button @click="onTopBarMenuNotificationButton()" class="p-link layout-topbar-button">
                <i class="pi pi-bell"></i>
                <span>Plan</span>
            </button>
            <!-- Profile -->
            <button @click="onTopBarMenuProfileButton()" class="p-link layout-topbar-button">
                <i class="pi pi-user"></i>
                <span>Profile</span>
            </button>
            <p id="userName" @click="onTopBarMenuProfileButton()" >김싸피</p>
            <p style="padding-top: 10px; font-weight: 900;">님</p>
        </div>





        <!-- 알림 클릭 시 나오는 창 -->
        <div>
            <v-card :class="[topbarNotificationActive? 'notificationActive':'notificationHidden']" id="popUp">
                <div style="display: flex; justify-content: space-between; margin-bottom: -15px;">
                    <p id="notificatonSet">Notification</p>
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
                            <span style="font-size: 18px; font-weight: bold; color: #3498DB;">김싸피</span>
                        </template>
                        <template #subtitle>
                            <span style="font-size: 15px; ">ssafy@ssafy.com</span>
                        </template>
                    </v-list-item>
                </v-list>
                <div @click="goMyPage()" style="text-align: center;">
                    <button id="myPageBtn">My Page</button>
                    <button id="logOutBtn">LogOut</button>
                </div>
                <v-divider></v-divider>
                <div id="friendsList">
                    <div style="display: flex;">
                        <p style="font-weight: bold; font-size: 20px; padding-left: 10px; margin-bottom: 0px; color: rgba(0, 0, 0, 0.5);">My Friends</p>
                        <button id="requestedBtn">받은 신청</button>
                        <button id="requestBtn">친구 요청</button>
                    </div>
                    <div v-for="friend in friends" :key="friend.index" id="friendInfo">
                        <div>
                            <img :src=friend.profileImg alt="프로필 이미지" id="friendProfileImg">
                        </div>
                        <div>
                            <p id="friendName">{{ friend.name}} </p>
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
            
            
        </div>
    </template>



<script setup>
    import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
    import { useRouter } from 'vue-router';
    import { useAccountsStore } from '@/stores/accountsStore';
    const accountsStore = useAccountsStore()
    const router = useRouter()
    
    const outsideClickListener = ref(null);
    const topbarMenuActive = ref(false);
    const topbarNotificationActive = ref(false);  // 알림 팝업 유무 변수
    const topbarProfileActive = ref(false);  // 프로필 팝업 유무 변수

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










// 알림 팝업 부분 코드 시작
    const onTopBarMenuNotificationButton = () => {
        if (accountsStore.isLogin) {
            topbarMenuActive.value = !topbarMenuActive.value;
            topbarNotificationActive.value = !topbarNotificationActive.value;
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
            profileImg: "/로고 3.png"
        },
        {
            name: "박정인",
            email: "qkrwjddls96@ssafy.com",
            profileImg: "/로고 3.png"
        },
        {
            name: "강지수",
            email: "rkdwltn96@ssafy.com",
            profileImg: "/로고 3.png"
        },
        {
            name: "김혁일",
            email: "rlagurdlf97@ssafy.com",
            profileImg: "/로고 3.png"
        },
        {
            name: "김재훈",
            email: "rlawogns98@ssafy.com",
            profileImg: "/로고 3.png"
        },
        {
            name: "이세은",
            email: "dltpdms99@ssafy.com",
            profileImg: "/로고 3.png"
        }
    ]);
// 프로필 팝업 부분 코드 끝
</script>



<style scoped>
.topbar {top: -100px; transition:top .2s;}

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
    overflow-y: scroll;
    width: 100%;
    border-radius: 15px;
    border: 1px solid rgba(0, 0, 0, 0.1);
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
  
  #popUp {
    width: 300px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 5%;
    padding: 7px;
    top: 60px;
  }
  #profileInfo {
    width: 100%;
    border-radius: 15px;
    border: 1px solid rgba(0, 0, 0, 0.1);
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
    border: 1px solid rgba(0, 0, 0, 0.1);

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
    border: 1px solid rgba(0, 0, 0, 0.1);
  }

  #friendsList {
    background-color: #FFFFFF;
    border-radius: 15px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    max-height: 200px;
    overflow-y: scroll;
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
    width: 20%;
    height: 10%;
    font-size: 11px;
    margin-right: 2px;
    margin-left: 50px;
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
    width: 20%;
    height: 10%;
    font-size: 11px;
    margin-left: 2px;
    margin-top: 4px;
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

</style>
