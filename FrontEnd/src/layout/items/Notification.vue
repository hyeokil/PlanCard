<template>
  <v-card id="popUp">
    <div style="display: flex; justify-content: space-between; margin-bottom: -15px;">
      <p id="notificatonSet">알림 내역</p>
      <v-btn id="clearBtn" @click="deleteAlarmAll()">모두 지우기</v-btn>
    </div>
    <div v-if="notifications.length > 0" id="notificationsList" ref="notificationsList" @scroll="onScroll">
      <div id="notificationDivider1"></div>
      <div v-for="notification in notifications" :key="notification.index" style="width: 95%;">
        <div style="display: flex;">
          <p id="notification">{{ notification.content }}</p>
          <div style="display: flex; align-items: center;">
            <i class="pi pi-check" id="acceptBtn" @click="handleAlarm(notification.alarmId, 'ACCEPT')"></i>
            <i class="pi pi-times" id="rejectBtn" @click="handleAlarm(notification.alarmId, 'REJECT')"></i>
          </div>
        </div>
        <div id="notificationDivider2"></div>
      </div>
    </div>
    <div v-else id="notificationsList">
      <p style="text-align: center; color: rgba(0, 0, 0, 0.5); padding: 10px;">수신된 알림이 없습니다.</p>
    </div>
    <v-card-actions>
      <v-btn variant="text" @click="$emit('closeNotification')" id="closeBtn">
        <p>닫기</p>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>



<script setup>
import { ref, watch } from 'vue';
import { alarmGetListApi, alarmActionApi, alarmDeleteAllApi } from "@/api/alarmApi";

// props로 상태를 받습니다.
const props = defineProps({
  notificationActive: Boolean
});

// props.notificationActive의 변화를 감지합니다.
watch(() => props.notificationActive, (newVal) => {
  if (newVal) {
    fetchAlarms(); // 알람 팝업이 활성화될 때마다 알람 목록을 새로고침합니다.
  }
});


const lastAlarmId = ref(null);
const notifications = ref([]);

const notificationsList = ref(null); // 알람 목록을 담는 DOM 요소의 ref

  // 스크롤 이벤트 핸들러
  const onScroll = () => {
    const container = notificationsList.value;
    // 스크롤 끝에 도달했는지 확인
    if (container.scrollHeight - container.scrollTop === container.clientHeight) {
      // 마지막 알람 ID로 API 호출
      fetchAlarms();
    }
  };

// 알람 가져오기 메서드
const fetchAlarms = async () => {
  try {
    const response = await alarmGetListApi(lastAlarmId.value);
    if (response.data.dataHeader.successCode === 0) {
      const fetchedNotifications = response.data.dataBody;
      if (fetchedNotifications.length) {
         // 중복 제거
         const uniqueNotifications = fetchedNotifications.filter(notification => 
          !notifications.value.some(existing => existing.alarmId === notification.alarmId));
        
        notifications.value.push(...uniqueNotifications);
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

  // console.log(notifications.value);
};

// 알람 처리 메서드 (수락/거절)
const handleAlarm = async (alarmId, action) => {
  try {
    // alarmApi.js에서 export한 함수를 사용하여 백엔드 API 호출
    const response = await alarmActionApi({ alarmId, action });
    if (response.data.dataHeader.successCode === 0) {

      // 알람 유형이 CONFERENCE인 경우, URL로 이동
      const alarm = notifications.value.find(n => n.alarmId === alarmId);

      // 화상 회의 알람을 수락한 경우
      if (alarm && alarm.type === 'CONFERENCE' && action === 'ACCEPT') {
        window.location.href = alarm.url; // 해당 화상회의 링크로 이동
      } 
    
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

const deleteAlarmAll = async () => {
  try {
    const response = await alarmDeleteAllApi();
    if (response.data.dataHeader.successCode === 0) {
      // 알람 목록을 비우고 새로고침
      notifications.value = []; // 기존 알람 목록을 비웁니다.
      lastAlarmId.value = null; // 마지막 알람 ID를 리셋합니다.
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
}

</script>



<style scoped>
#popUp {
  width: 330px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  /* border-radius: 5%; */
  padding: 7px;
  top: 60px;
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

#notificationDivider1 {
  margin-left: 10px;
  width: 90%;
  height: 1px;
  background-color: #FFFFFF;
  margin-bottom: 10px;
}

#notification {
  margin: 10px;
  width: 80%;
}

#acceptBtn {
  font-size: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  padding: 5px;
  background-color: #3498DB;
  color: white;
  width: 35px;
  height: 35px;
  margin-right: 3px;
}

#acceptBtn:hover {
  transform: scale(1.1);
}

#rejectBtn {
  font-size: 1.5rem;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  padding: 5px;
  background-color: red;
  color: white;
  width: 35px;
  height: 35px;
  margin-left: 3px;
}

#rejectBtn:hover {
  transform: scale(1.1);
}

#notificationDivider2 {
  margin-left: 10px;
  width: 100%;
  height: 1px;
  background-color: #ccc;
  margin-top: 10px;
  margin-bottom: 10px;
}

#closeBtn {
  text-align: center;
  color: #FFFFFF;
  background-color: #3498DB;
  position: relative;
  width: 100%;
  height: 25px;
  line-height: 25px;
  border-radius: 5cm;
  border: 1px solid rgba(0, 0, 0, 0.1);
  top: 15px;
}

#closeBtn:hover {
  transform: scale(1.05);
  border-color: #3498db;
}
</style>