import axios from "axios";
import { ref } from "vue";
import { defineStore } from "pinia";
import { useRouter } from "vue-router";

export const useAccountsStore = defineStore(
  "accounts",
  () => {
    const router = useRouter();
    const API_URL = "/api/v1";

    // 변수
    const token = ref(null);
    const userName = ref("");
    const userId = ref("");
    const password = ref("");

    // 회원가입
    const signUp = function (info) {
      const { userEmail, userName, userNickname, password1, password2, userPhoto } = info;
      axios({
        method: "post",
        url: `${API_URL}/member/signup`,
        data: {
          userEmail,
          userName,
          userNickname,
          password1,
          password2,
          userPhoto,
        },
      })
        .then((response) => {
          console.log(response.data);
          console.log(response.data.key);
          token.value = response.data.key;
          alert("회원가입이 완료되었습니다.");
          router.push({ name: "main" });
        })
        .catch((error) => {
          alert("회원가입에 실패했습니다.");
          console.log(error);
        });
    };

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

    // 로그인
    const logIn = function (info) {
      const { userName, password } = info;
      axios({
        method: "post",
        url: `${API_URL}/member/login`,
        data: {
          userName,
          password,
        },
      })
        .then((response) => {
          console.log(response.data);
          console.log(response.data.key);
          token.value = response.data.key;
          console.log(response);
          router.push({ name: "main" });
        })
        .catch((err) => {
          console.log(err);
          alert("로그인에 실패했습니다.");
        });
    };

    // 로그아웃
    const logOut = function () {
      axios({
        method: "post",
        url: `${API_URL}/member/logout`,
      })
        .then((res) => {
          token.value = null;
          console.log(res);
          alert("로그아웃 되었습니다.");
          router.push({ name: "main" });
        })
        .catch((err) => {
          console.log(err);
        });
    };

    // 비밀번호 변경
    const passwordResetting = function (info) {
      const { currentPassword, newPassword1, newPassword2 } = info;
      axios({
        method: "patch",
        url: `${API_URL}/member/update/password`,
        data: {
          currentPassword,
          newPassword1,
          newPassword2,
        },
      })
        .then((response) => {
          console.log(response.data);
          alert("비밀번호가 변경되었습니다.");
        })
        .catch((error) => {
          alert("비밀번호 변경 실패");
          console.log(error);
        });
    };

    return {
      API_URL,
      token,
      userName,
      userId,
      password,
      isLogin,
      signUp,
      logIn,
      logOut,
      passwordResetting,
    };
  },
  { persist: true }
);
