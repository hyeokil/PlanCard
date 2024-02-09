<template>
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
                <i class="pi pi-check" id="acceptBtn" @click="handleAlarm(notification.alarmId, 'ACCEPT')"></i>
                <i class="pi pi-times" id="rejectBtn" @click="handleAlarm(notification.alarmId, 'REJECT')"></i>
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
</template>



<script setup>

</script>



<style scoped>
#popUp {
  width: 330px;
  border: 1px solid rgba(52, 152, 219, 0.5);
  border-radius: 5%;
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