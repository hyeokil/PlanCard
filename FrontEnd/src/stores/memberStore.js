
import axios from 'axios';
import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";


export const useMemberStore = defineStore('member', () => {

  const API_URL = "/api/v1";

  const friends = ref([])  // 친구 목록을 담은 배열

  // 친구 목록 조회
  const getFriends = function () {
    axios({
      method: "get",
      url: `${API_URL}/friends`,
    })
      .then((response) => {
        console.log(response);
      })
      .catch((err) => {
        console.log(err);
      });
  };
  
  




  return {
    API_URL,
    friends,
    getFriends,


    
  }

}, { persist: true })
