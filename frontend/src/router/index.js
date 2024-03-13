import { createRouter, createWebHistory } from "vue-router";


import SignUpPage from "../pages/SignUpPage.vue";
import LoginPage from "../pages/LoginPage.vue";
import MainPage from "../pages/MainPage.vue";
import ApproveListPage from "@/pages/Approve/ApproveListPage.vue";
import ApproveCreatePage from "@/pages/Approve/ApproveCreatePage.vue"
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/", component: LoginPage },
    { path: "/login", component: LoginPage },
    { path: "/main", component: MainPage },
    { path: "/approve/list", component: ApproveListPage },
    { path: "/approve/create", component: ApproveCreatePage },

  ],
});

export default router;

