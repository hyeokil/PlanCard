<template>
    <div class="card p-fluid" style="width: 400px; justify-content: center;">
        <div>
            <h1>미팅 생성</h1>
        </div>
        <hr id="separator">

        <div class="card p-fluid">    
            <form @submit.prevent="goMeeting" style="width: 300px;">
                <h2>여행 이름</h2>
                <input type="text" class="box, card p-fluid" id="tripTitleInput" v-model.trim="tripTitle" placeholder="여행 이름을 알려주세요">
    
                <h2>여행 일정 선택</h2>
                    <Calendar 
                    v-model="selectedDates" 
                    dataFormat="yy/mm/dd"
                    selectionMode="range" 
                    :manualInput="false"
                    showButtonBar
                    locale="ko-KR"
                    placeholder="여행 일정을 선택하세요"
                    class="field box, card p-fluid"
                    id="selectDateCalendar"
                    />
                
                <h2>친구 선택</h2>
                <input class="box, card p-fluid" id="searchFriendsDiv" type="text" v-model="searchText" placeholder="친구 검색">
                <div class="box, card p-fluid" id="selectFriendsDiv">
                    <p v-for="selectedFriend in selectedFriends" :key="selectedFriend.id" id="selectedFriend" @click="removeFriend(selectedFriend)">
                        {{ truncateName(selectedFriend.name) }}
                        <span>[X]</span>
                    </p>
                </div>
                <div class="box, card p-fluid" id="FriendsDiv">
                    <div v-for="friend in filteredFriends" :key="friend.id" @click="addFriend(friend)">
                        <p>{{ friend.name }}</p>
                    </div>
                </div>
            
                <div>
                    <input class="card p-fluid" type="submit" id="createSubmit" value = 시작하기>
                </div>
                <div style="margin-top: 10px;">
                    <button class="card p-fluid" id="logoutBtn" @click="$emit('closeMeetingCreate')">
                      닫기
                    </button>
                </div>

            </form>
        </div>


    
    



    </div>
</template>



<script setup>
    import { ref, computed } from "vue";
    import Calendar from 'primevue/calendar'
    import { useRouter } from "vue-router";
    const router = useRouter()
    
    const tripTitle = ref("");  // 여행 이름
    const selectedDates = ref(null); // 선택된 여행 일정
    const selectedFriends = ref([]);  // 선택된 친구


    const goMeeting = () => {
        // 화상회의 생성
        // 여행계획 생성
        // 여행 세부계획 생성
        // 사람들에게 초대 요청
        // 화상회의로 바로 이동
        // router.push({ name: 'meeting-detail', params: { id: 1 } })
    }




    // 친구 리스트 dummy
    const friends = ref([
        { id: 1, name: '김수한무거' },
        { id: 2, name: '김재훈1' },
        { id: 3, name: '김재훈2' },
        { id: 4, name: '김재훈3' },
        { id: 5, name: '김재훈4' },
        { id: 6, name: '김재훈5' },
        { id: 7, name: '김재훈6' },
        { id: 8, name: '김재훈7' },
        { id: 9, name: '김재훈8' },
    ]);
    // 친구 검색어
    const searchText = ref('');

    // 검색어를 기반으로 친구 필터링
    const filteredFriends = computed(() => {
    if (!searchText.value) {
        return friends.value;
    } else {
        return friends.value.filter(friend =>
        friend.name.toLowerCase().includes(searchText.value.toLowerCase())
        );
    }
    });

    // 친구를 선택하여 selectedFriends 배열에 추가
    const addFriend = (friend) => {
        if (!selectedFriends.value.some(fr => fr.id === friend.id)) {
            selectedFriends.value.push(friend);
        }
    }
    // 선택된 친구를 배열에서 삭제
    const removeFriend = (friend) => {
      const index = selectedFriends.value.findIndex(fr => fr.id === friend.id);
      if (index !== -1) {
        selectedFriends.value.splice(index, 1);
      }
    }
    // 이름이 4글자를 넘어가면 요약
    const truncateName = (name) => {
        if (name.length > 4) {
            return name.slice(0, 4) + '...';
        } else {
            return name;
        }
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
  #tripTitleInput {
    background-color: rgba(245, 245, 245, 0.1);
    width: 100%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }
  #selectDateCalendar {
    background-color: rgba(245, 245, 245, 0.1);
    width: 100%;
    display: flex;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
    justify-content: center;
  }

  #searchFriendsDiv {
    background-color: rgba(245, 245, 245, 0.1);
    width: 100%;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
  }
  #selectFriendsDiv {
    background-color: rgba(245, 245, 245, 0.1);
    width: 100%;
    align-items: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 40px;
    margin-bottom: 10px;
    display: flex;
    padding: 10;

    overflow-x: auto;
    overflow-y: hidden;
    white-space: nowrap; /* 텍스트 줄 바꿈 방지 */

  }
  #selectedFriend {
    text-align: center;
    color: white;
    background-color: #3498DB;
    position: relative;
    margin: 0;
    margin-left: 10px;
    width: 50%;
    padding: 2%;
    font-weight: bold;
    border-radius: 5cm;
    white-space: nowrap;
  }
  #FriendsDiv {
    background-color: rgba(245, 245, 245, 0.1);
    width: 100%;
    border: 1px solid rgba(52, 152, 219, 0.5);
    height: 300px;
    margin-bottom: 10px;

    overflow-y: auto;
  }

  h2 {
    color: #3498db;
    font-weight: bold;
  }
  #createSubmit {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(0, 0, 0, 0.1);
    background-color: #3498db;
    color: #FFFFFF;
    font-weight: bold;
    font-size: medium;
    height: 20px;
    padding: 20px;
    line-height: 0px;
  }
  #logoutBtn {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid rgba(52, 152, 219, 0.5);
    background-color: #FFFFFF;
    color: rgba(0, 0, 0, 0.5);
    font-weight: bold;
    font-size: medium;
    height: 20px;
    padding: 20px;
    line-height: 0px;
  }
</style>