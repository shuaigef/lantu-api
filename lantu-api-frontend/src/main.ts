import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ArcoVue from '@arco-design/web-vue';
import ArcoVueIcon from '@arco-design/web-vue/es/icon';
import '@arco-design/web-vue/dist/arco.css';
// import './style.css'
import App from './App.vue'
import router from "./config/routes";
import 'highlight.js/styles/stackoverflow-light.css'
import hljs from "highlight.js/lib/core";
import hljsVuePlugin from "@highlightjs/vue-plugin";
import json from 'highlight.js/lib/languages/json'

hljs.registerLanguage("json", json);

const pinia = createPinia()
const app = createApp(App)

app.use(hljsVuePlugin)
app.use(router)
app.use(pinia)
app.use(ArcoVue)
app.use(ArcoVueIcon)
app.mount('#app')



