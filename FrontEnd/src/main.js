import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

import App from "./App.vue";
import router from "./router";
import { useAccountsStore } from "./stores/accountsStore";

import { createVuetify } from "vuetify";
// import 'vuetify/dist/vuetify.min.css'
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import "vuetify/styles";
import VCalendar from "v-calendar";
import "v-calendar/style.css";
import PrimeVue from "primevue/config";
import "@/assets/styles.scss";
import ConfirmationService from "primevue/confirmationservice";

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
// app.use(createPinia())
app.use(router);

const vuetify = createVuetify({
  components,
  directives,
});
app.use(vuetify);
app.use(PrimeVue, { ripple: true });
app.use(VCalendar, {});
app.use(ConfirmationService);


// 전역 가드를 사용하여 페이지 이동 시 로그인 상태 체크
router.beforeEach((to, from, next) => {
  const accountsStore = useAccountsStore(pinia);
  accountsStore.checkLoginStatus();
  
  // 로그인이 필요한 페이지에 대한 접근 제어 로직을 여기에 추가할 수 있습니다.
  // 예: if (to.meta.requiresAuth && !authStore.isLogin) { ... }

  next();
});

app.mount("#app");

// Vue.config.productionTip = false

// new Vue({
//   render: h => h(App),
// }).$mount('#app')
