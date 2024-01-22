import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useMemberStore = defineStore('member', () => {
  // 알람 modal true/false
  const dialog = ref(false);
  // 알람 modal 열기
  const openMyPageAlarm = () => {
      dialog.value = true;
      console.log(dialog.value)
  };

  // 알람 modal 닫기
  const closeMyPageAlarm = () => {
      dialog.value = false;
  };


  return {dialog,
    openMyPageAlarm,
    closeMyPageAlarm,
  }

}, { persist: true })
