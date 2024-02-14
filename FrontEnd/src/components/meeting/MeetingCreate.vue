<template>
  <div class="card p-fluid" style="justify-content: center; padding-bottom: 5px;">
    <div>
      <h2 style="color: black;">미팅 생성</h2>
    </div>
    <hr id="separator">
    <div>
      <form @submit.prevent="goMeeting" style="width: 800px;">

        <div style="display: flex;">
          <div style="width: 295px;">
            <h3>여행 이름</h3>
            <input type="text" class="box, card p-fluid font-content" id="tripTitleInput" v-model.trim="tripTitle" placeholder="여행 이름을 알려주세요">
            <div class="font-content flex-auto">
              <h3>여행 일정 선택</h3>
              <Calendar v-model="selectedDates" dataFormat="yy/mm/dd" selectionMode="range" :manualInput="false"
                locale="ko-KR" placeholder="여행 일정을 선택하세요" class="field box font-content"
                id="selectDateCalendar"
                showIcon
                iconDisplay="input"
                :inputStyle="{'border-radius':'5px', 'width':'280px'}"
                :panelStyle="{
                  'width':'200px', 
                  'display':'flex', 
                  'align-items':'center', 
                  'justify-content':'center', 
                  'font-family':'Pretendard Variable',
                  'padding':'0'
                  }"
                  panelClass="panel"
              />
            </div>
          </div>
          <div style="width: 295px;">
            <h3>친구 선택 ({{ selectedFriends.length }})</h3>
            <div style="display: flex; justify-content: space-between;">
              <div>
                <div class="box, card p-fluid" id="selectFriendsDiv">
                  <div style="width: 150px; ;">
                    <v-chip v-for="selectedFriend in selectedFriends" :key="selectedFriend.id" class="font-content" id="selectedFriend" @click="removeFriend(selectedFriend)">
                      <p style="margin: 0;">{{ selectedFriend.name }}&nbsp;&nbsp;</p>
                      <i class="pi pi-times-circle"></i>
                    </v-chip>
                  </div>
                </div>
                <div style="display: flex;">
                  <div class="box, card p-fluid" id="FriendsDiv" ref="FriendsDiv" @scroll="handleScroll">
                    <div v-for="friend in friends" :key="friend.friendId" @click="addFriend(friend)" id="friendList">
                      <p class=" font-content" style="
                            font-weight: bold;
                            font-size: medium;
                            color: #3498DB;
                            margin-right: 10px;
                            margin-bottom: 7px;">{{ friend.name }}</p>
                      <p class=" font-content" style="
                          font-size: small;
                          margin-top: 2px;
                          color: rgba(0, 0, 0, 0.5);">{{ friend.email }}</p>
                    </div>
                  </div>
                  <div>
                    <input class="box, card p-fluid font-content" id="searchFriendsDiv" type="text" v-model="searchText" placeholder="이메일 검색">
                    <div class="box, card p-fluid" id="FriendsDiv2">
                      <div v-for="friend in filteredUsers" :key="friend.id" @click="addFriend(friend)" id="friendList">
                        <p class=" font-content"
                            style="
                              font-weight: bold;
                              font-size: medium;
                              color: #3498DB;
                              margin-right: 10px;
                              margin-bottom: 7px;">{{ friend.name }}</p>
                        <p class=" font-content" style="
                            font-size: small;
                            color: rgba(0, 0, 0, 0.5);">{{ friend.email }}</p>
                      </div>
                    </div>
                  </div>
                </div>


              </div>
            </div>
          </div>
        </div>

        <div>
          <input class="card p-fluid" type="submit" id="createSubmit" value=시작하기>
        </div>
        <div style="margin-top: 10px;">
          <v-btn class="card p-fluid" id="closeBtn" @click="$emit('closeMeetingCreate')">
            닫기
          </v-btn>
        </div>

      </form>
    </div>
  </div>
</template>



<script setup>
import { ref, watch, onMounted, onUnmounted, nextTick } from "vue";
import Calendar from 'primevue/calendar'
import { useRouter } from "vue-router";
import { planCreateApi } from "@/api/planApi"; // planApi.js에서 API 함수를 import
import { friendListGetApi, userSearchGetApi } from '@/api/friendApi';
import { alarmPlanRequestApi } from "@/api/alarmApi";

const router = useRouter();

const tripTitle = ref("");  // 여행 이름
const selectedDates = ref(null); // 선택된 여행 일정
const selectedFriends = ref([]);  // 선택된 친구

const friends = ref([]); // 현재 페이지에 표시된 친구들의 목록
const page = ref(0); // 현재 페이지 번호
const size = ref(10); // 페이지당 아이템 수
const isLastPage = ref(false); // 마지막 페이지 여부


const goMeeting = async () => {
  if (!tripTitle.value || !selectedDates.value || selectedDates.value.length < 2) {
    alert("여행 이름과 일정을 모두 입력해주세요.");
    return;
  }

  const [startDate, endDate] = selectedDates.value;

  const param = {
    name: tripTitle.value,
    startDate: startDate.toLocaleDateString('en-CA'), // 'YYYY-MM-DD' 형식
    endDate: endDate.toLocaleDateString('en-CA'), // 'YYYY-MM-DD' 형식
  };

  try {
    await planCreateApi(param,
      (response) => {
        // MeetingCreate.vue 내 goMeeting 함수 수정 부분
        // 성공 후 created 이벤트를 발생시키는 부분
        if (response.data.dataHeader.successCode === 0) {
          alert("여행 계획이 성공적으로 생성되었습니다.");
          const planId = response.data.dataBody;
          fetchAlarmPlan(planId);
          router.push(`/meeting/view/${planId}`); // 생성된 planId를 사용하여 라우팅
        } else {
          alert(response.data.dataHeader.resultMessage);
        }

      }
    );
  } catch (error) {
    console.error("여행 계획 생성 중 오류가 발생했습니다:", error);
    alert("여행 계획 생성 중 오류가 발생했습니다.");
  }
}

const fetchAlarmPlan = async (planId) => {
  // 친구들에게 여행 요청 알람 보내기
  const alarmRequests = selectedFriends.value.map(friend => ({
        friendId: friend.friendId,
        type: "PLAN",
        planName: tripTitle.value,
        url: `${window.location.origin}/meeting/view/${planId}`
  }));

  try {
    const response = await alarmPlanRequestApi(alarmRequests);
    if (response.data.dataHeader.successCode === 1) {
      alert(response.data.dataBody.resultMessage);
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
}


// 친구 검색어
const searchText = ref("");

const filteredUsers = ref([])
watch(searchText, async(newV, oldV) => {
  console.log('검색어가 바껴요', newV)
  if (newV === "") {
    filteredUsers.value = []
  } else {
    const response = await userSearchGetApi(newV);
    console.log(response)
    if (response.data.dataHeader.successCode === 0) {
      filteredUsers.value = response.data.dataBody
      console.log('검색완?',filteredUsers.value)
    }
  }
  }, { deep: true })


// 친구를 선택하여 selectedFriends 배열에 추가
const addFriend = (friend) => {
  if (!selectedFriends.value.some(fr => fr.email === friend.email)) {
    selectedFriends.value.unshift(friend);
    console.log(selectedFriends.value);
  }
}

// 친구를 선택하여 배열에서 삭제 (email을 추적)
const removeFriend = (friend) => {
  const index = selectedFriends.value.findIndex(fr => fr.email === friend.email);
  if (index !== -1) {
    selectedFriends.value.splice(index, 1);
    console.log(selectedFriends.value);
  }
}

// 스크롤 이벤트 핸들러
const handleScroll = () => {
  const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
  if (scrollTop + clientHeight >= scrollHeight - 5) { // 스크롤이 거의 끝에 도달하면
    fetchFriendList(); // 다음 페이지의 친구 목록을 불러옴
  }
};

// 친구 목록을 불러오는 메서드
const fetchFriendList = async () => {
  if (isLastPage.value) return; // 마지막 페이지이면 더 이상 불러오지 않음
  try {
    const response = await friendListGetApi(page.value, size.value);
    if (response.data.dataHeader.successCode === 0) {
      friends.value.push(...response.data.dataBody.contents); // 받아온 친구 목록을 추가
      await nextTick(); // DOM 갱신을 기다립니다.
      isLastPage.value = !response.data.dataBody.hasNext; // 다음 페이지 존재 유무 업데이트
      page.value++; // 다음 페이지로 이동

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

onMounted(() => {
  fetchFriendList();
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>


<style>
.panel, .p-datepicker-group-container{
  margin: 0px;
  padding: 0px;
}
.p-datepicker-header{
  width: 236.56px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.p-datepicker-calendar{
  width: 100%;
}
.p-datepicker table td {
  padding: 0.5rem;
  cursor: pointer;

}
</style>

<style scoped>
h3 {
  color: #3498db;
  /* font-weight: bold; */
}

#separator {
  border: none;
  border-top: 4px solid #3498DB;
  border-radius: 10px;
}

#tripTitleInput {
  background-color: rgba(245, 245, 245, 0.1);
  width: 90%;
  display: flex;
  align-items: center;
  border: 1px solid rgba(52, 152, 219, 0.5);
  height: 30px;
  padding: 15px;
  margin-bottom: 10px;
}
#tripTitleInput:focus {
  outline: 2px solid #3498DB;
}

#selectDateCalendar {
  background-color: rgba(245, 245, 245, 0.1);
  width: 90%;
  display: flex;
  align-items: center;
  border: 1px solid rgba(52, 152, 219, 0.5);
  border-radius: 10px;
  height: 30px;
  padding: 15px;
  margin-bottom: 10px;
  justify-content: center;
}



#searchFriendsDiv {
  background-color: rgba(245, 245, 245, 0.1);
  width: 245px;
  align-items: center;
  border: 1px solid rgba(52, 152, 219, 0.5);
  height: 30px;
  padding: 15px;
  margin-bottom: 10px;
  margin-left: 5px;
}
#searchFriendsDiv:focus {
  outline: 2px solid #3498DB;
}

#selectFriendsDiv {
  background-color: rgba(245, 245, 245, 0.1);
  width: 500px;
  align-items: center;
  border: 5px solid rgba(52, 152, 219, 0.5);
  height: 40px;
  margin-bottom: 10px;
  display: flex;
  padding: 10;

  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  /* 텍스트 줄 바꿈 방지 */

}

#selectedFriendHolder {
  color: rgba(75, 85, 99, 0.8);
  margin-left: -4px;
}

#selectedFriend {
  color: white;
  background-color: #3498DB;
  position: relative;
  margin: 0;
  margin-right: 10px;
  padding: 7%;
  font-weight: bold;
  border-radius: 5cm;
  white-space: nowrap;
  justify-content: space-between;
  cursor: pointer;
  transition: transform 0.3s ease;
}
#selectedFriend:hover {
  transform: scale(1.03);
}

#FriendsDiv {
  background-color: rgba(245, 245, 245, 0.1);
  width: 245px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  height: 170px;
  margin-bottom: 10px;
  margin-right: 5px;
  padding: 20px;
  padding-top: 10px;

  overflow-y: auto;
}

#FriendsDiv2 {
  background-color: rgba(245, 245, 245, 0.1);
  width: 245px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  height: 128.67px;
  margin-bottom: 10px;
  margin-left: 5px;
  padding: 20px;
  padding-top: 10px;

  overflow-y: auto;
}

h2 {
  color: #3498db;
  /* font-weight: bold; */
}

#createSubmit {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background-color: #3498db;
  color: #FFFFFF;
  /* font-weight: bold; */
  font-size: medium;
  height: 20px;
  padding: 20px;
  line-height: 0px;
  transition: transform 0.3s ease;
}

#createSubmit:hover {
  transform: scale(1.05);
  border-color: #3498db;
}

#closeBtn {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid rgba(52, 152, 219, 0.5);
  background-color: #FFFFFF;
  color: rgba(0, 0, 0, 0.5);
  /* font-weight: bold; */
  font-size: medium;
  height: 20px;
  padding: 20px;
  line-height: 0px;
}

#friendList {
  cursor: pointer;
  display: flex;
  transition: transform 0.3s ease;
}
#friendList:hover {
  transform: scale(1.03);

}
</style>