import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import axios from 'axios';

const app = createApp(App);

// 전역 프로퍼티로 axios 설정
app.config.globalProperties.$http = axios;

app.use(router);

app.mount("#app");
