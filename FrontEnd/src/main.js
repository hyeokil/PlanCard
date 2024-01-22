import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'



import App from './App.vue'
import router from './router'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
// import 'vuetify/dist/vuetify.min.css'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'


const app = createApp(App)

const vuetify = createVuetify({
    components,
    directives
})

// app.use(createPinia())
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(pinia)
app.use(router)
app.use(vuetify)


app.mount('#app')