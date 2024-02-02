import { defineStore } from 'pinia';

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
    }
  },
  persist: true, 
});
