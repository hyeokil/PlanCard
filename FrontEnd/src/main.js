import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";

import App from "./App.vue";
import router from "./router";

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
app.mount("#app");

// Vue.config.productionTip = false

// new Vue({
//   render: h => h(App),
// }).$mount('#app')
