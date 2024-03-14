import { createRouter, createWebHistory } from "vue-router";


import SignUpPage from "../pages/SignUpPage.vue";
import LoginPage from "../pages/LoginPage.vue";
import MainPage from "../pages/MainPage.vue";
import OvertimeCreatePage from '../pages/OvertimeCreatePage.vue';
import OvertimeListPage from '../pages/OvertimeListPage.vue';
import OvertimeModifyPage from '../pages/OvertimeModifyPage.vue';
import OvertimeApproveaPage from '../pages/OvertimeApproveaPage.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/", component: LoginPage },
    { path: "/login", component: LoginPage },
    { path: "/main", component: MainPage },
    { path: "/overtimecreate", component: OvertimeCreatePage},
    { path: "/overtimelist", component: OvertimeListPage},
    { path: "/overtimemodify", component: OvertimeModifyPage},
    { path: "/overtimeapprovea", component: OvertimeApproveaPage},
  ],
});

export default router;

