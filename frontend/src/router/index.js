import { createRouter, createWebHistory } from "vue-router";


import SignUpPage from "../pages/SignUpPage.vue";
import LoginPage from "../pages/LoginPage.vue";
import MainPage from "../pages/MainPage.vue";
import OvertimeCreatePage from '../pages/OvertimeCreatePage.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/", component: LoginPage },
    { path: "/login", component: LoginPage },
    { path: "/main", component: MainPage },
    { path: "/overtimecreate", component: OvertimeCreatePage},
  ],
});

export default router;

