import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useMemberStore = defineStore('member', () => {

  // 알람 modal true/false
  const notificationOnOff = ref(false);

  // 알람 modal 열기
  const openMyNotification = () => {
      notificationOnOff.value = true;
      console.log(notificationOnOff.value)
  };

  // 알람 modal 닫기
  const closeMyNotification = () => {
      notificationOnOff.value = false;
      console.log(notificationOnOff.value)
  };

  // 내정보 modal
  const myProfile = ref(false);

  // 내정보 modal 열기
  const openMyProfile = () => {
    myProfile.value = true;
    console.log(myProfile.value)
};

// 내정보 modal 닫기
const closeMyProfile = () => {
    myProfile.value = false;
    console.log(myProfile.value)
};


  return {
    notificationOnOff,
    myProfile,
    openMyNotification,
    closeMyNotification,
    openMyProfile,
    closeMyProfile,
  }

}, { persist: true })
