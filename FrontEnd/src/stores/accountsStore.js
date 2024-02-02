import axios from "axios";
import { ref, computed } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";
import { getMyInfoApi } from "@/api/memberApi";

export const useAccountsStore = defineStore(
  "accountsStore",
  () => {
    const router = useRouter();
    const API_URL = "/api/v1";

    // 변수
    const token = ref(null);
    // const userName = ref("");
    // const userId = ref("");
    // const password = ref("");

    // // 회원가입
    // const signUp = function (info) {
    //   const { userEmail, userName, userNickname, password1, password2, userPhoto } = info;
    //   axios({
    //     method: "post",
    //     url: `${API_URL}/member/signup`,
    //     data: {
    //       userEmail,
    //       userName,
    //       userNickname,
    //       password1,
    //       password2,
    //       userPhoto,
    //     },
    //   })
    //     .then((response) => {
    //       console.log(response.data);
    //       console.log(response.data.key);
    //       token.value = response.data.key;
    //       alert("회원가입이 완료되었습니다.");
    //       router.push({ name: "member-login" });
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //       alert("회원가입에 실패했습니다.");
    //       router.push({ name: "member-signup" });
    //     });
    // };

    // 로그인 유무(true/false)
    // const isLogin = computed(() => {
    //   if (token.value === null) {
    //     return false
    //   } else {
    //     return true
    //   }
    // })
    // 로그인 유무 (테스트용)
    const isLogin = true;


    // // 로그인
    // const logIn = function (info) {
    //   const { userID, password } = info;
    //   axios({
    //     method: "post",
    //     url: `${API_URL}/member/login`,
    //     data: {
    //       userID,
    //       password,
    //     },
    //   })
    //     .then((response) => {
    //       console.log(response.data);
    //       console.log(response.data.key);
    //       token.value = response.data.key;
    //       console.log(response);
    //       router.push({ name: "main" });
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //       alert("로그인에 실패했습니다.");
    //       router.push({ name: "member-login" });
    //     });
    // };

    // // 소셜로그인1: Naver
    // const naverlogIn = function () {
    //   axios({
    //     method: "get",
    //     url: `${API_URL}/oauth/naver`,
    //   })
    //     .then((response) => {
    //       console.log(response.data);
    //       console.log(response.data.key);
    //       token.value = response.data.key;
    //       console.log(response);
    //       router.push({ name: "main" });
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //       alert("로그인에 실패했습니다.");
    //       router.push({ name: "member-login" });
    //     });
    // };

    // // 소셜로그인2: Kakao
    // const kakaologIn = function () {
    //   axios({
    //     method: "get",
    //     url: `${API_URL}/oauth/kakao`,
    //   })
    //     .then((response) => {
    //       console.log(response.data);
    //       console.log(response.data.key);
    //       token.value = response.data.key;
    //       console.log(response);
    //       router.push({ name: "main" });
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //       alert("로그인에 실패했습니다.");
    //       router.push({ name: "member-login" });
    //     });
    // };


    // // 로그아웃
    // const logOut = function () {
    //   axios({
    //     method: "post",
    //     url: `${API_URL}/member/logout`,
    //   })
    //     .then((res) => {
    //       token.value = null;
    //       console.log(res);
    //       alert("로그아웃 되었습니다.");
    //       router.push({ name: "main" });
    //     })
    //     .catch((err) => {
    //       console.log(err);
    //     });
    // };

    // 비밀번호 변경
    // const passwordResetting = function (info) {
    //   const { currentPassword, newPassword1, newPassword2 } = info;
    //   axios({
    //     method: "patch",
    //     url: `${API_URL}/member/update/password`,
    //     data: {
    //       currentPassword,
    //       newPassword1,
    //       newPassword2,
    //     },
    //   })
    //     .then((response) => {
    //       console.log(response.data);
    //       alert("비밀번호가 변경되었습니다.");
    //     })
    //     .catch((error) => {
    //       alert("비밀번호 변경 실패");
    //       console.log(error);
    //     });
    // };

    const getMyInfo = async () => {
      try {
        await getMyInfoApi(
          (response) => {
            if (response.data.dataHeader.successCode === 0) {
              console.log(response.data.dataBody)
            } else {
              alert(response.data.dataHeader.resultMessage);
            }
          });
      } catch (error) {
        console.error(error);
      }
    };

    return {
      API_URL,
      token,
      // userName,
      // userId,
      // password,
      isLogin,
      // signUp,
      // logIn,
      // naverlogIn,
      // kakaologIn, 
      // logOut,
      // passwordResetting,
      getMyInfo,
    };
  },
  { persist: true }
);
