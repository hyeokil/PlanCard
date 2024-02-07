import { defineStore } from 'pinia';
import { useCookies } from 'vue3-cookies';

export const useAccountsStore = defineStore('accountsStore', {
  state: () => ({
    isLogin: false,
    memberInfo: null,
  }),
  actions: {
    setLogin(status) {
      this.isLogin = status;
    },
    setMemberInfo(info) {
      this.memberInfo = info;
    },
    setLogout() {
      this.isLogin = false;
      this.memberInfo = null;
    },
    checkLoginStatus() {
      const { cookies } = useCookies();
      this.isLogin = !!cookies.get('accessToken');
    }
  },
  persist: true, 
});
