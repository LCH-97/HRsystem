import { createRouter, createWebHistory } from "vue-router";
import VueJwtDecode from "vue-jwt-decode";

import OvertimeCreatePage from "../pages/OvertimeCreatePage.vue";
import OvertimeListPage from "../pages/OvertimeListPage.vue";
// import OvertimeModifyPage from "../pages/OvertimeModifyPage.vue";
import OvertimeApproveaPage from "../pages/OvertimeApproveaPage.vue";
import OvertimeDetailPage from "../pages/OvertimeDetailPage.vue";

import ApproveListPage from "@/pages/Approve/ApproveListPage.vue";
import ApproveUpdatePage from "@/pages/Approve/ApproveUpdatePage.vue";
import ApproveCreatePage from "@/pages/Approve/ApproveCreatePage.vue";
import ApproveReadPage from "@/pages/Approve/ApproveReadPage.vue";

import SignUpPage from "@/pages/SignUpPage.vue";
import LoginPage from "@/pages/LoginPage.vue";
import MainPage from "@/pages/MainPage.vue";
import GooutCreate from "@/components/goout/GooutCreate";
import GooutList from "@/components/goout/GooutList.vue";
import GooutRead from "@/components/goout/GooutRead.vue";
import GooutUpdate from "@/components/goout/GooutUpdate.vue";
import GooutTypeCreate from "@/components/goout/gooutType/GooutTypeCreate.vue";
import GooutTypeList from "@/components/goout/gooutType/GooutTypeList.vue";
import GooutTypeRead from "@/components/goout/gooutType/GooutTypeRead.vue";
import GooutTypeUpdate from "@/components/goout/gooutType/GooutTypeUpdate.vue";
import GooutPage from "@/pages/goout/GooutPage.vue";
import GooutTypePage from "@/pages/goout/GooutTypePage.vue";

import BoardCreatePage from "@/pages/BoardCreatePage.vue";
import BoardListPage from "@/pages/BoardListPage.vue";
import BoardReadPage from "@/pages/BoardReadPage.vue";
import BoardUpdatePage from "@/pages/BoardUpdatePage.vue";

import ManagerPage from "@/pages/ManagerPage.vue";
import SalaryListPage from "@/pages/Salary/SalaryListPage.vue";

import axios from 'axios';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/", component: LoginPage },
    { path: "/login", component: LoginPage },

    { path: "/main", component: MainPage },
    { path: "/manager", component: ManagerPage },
    { path: "/overtimecreate", component: OvertimeCreatePage },
    { path: "/overtimelist", component: OvertimeListPage },
    // { path: "/overtimemodify", component: OvertimeModifyPage },
    { path: "/overtimeapprovea", component: OvertimeApproveaPage },
    { path: "/overtime/read/:id", component: OvertimeDetailPage },

    { path: "/approve/list", component: ApproveListPage },
    { path: "/approve/update", component: ApproveUpdatePage },
    { path: "/approve/create", component: ApproveCreatePage },
    { path: "/approve/read/:id", component: ApproveReadPage },

    { path: "/board/create", component: BoardCreatePage },
    { path: "/board/list", component: BoardListPage },
    { path: "/board/read/:id", component: BoardReadPage },
    { path: "/board/update", component: BoardUpdatePage },

    { path: "/salary/list", component: SalaryListPage },

    {
      path: "/goout",
      component: GooutPage,
      children: [
        { path: "create", component: GooutCreate },
        { path: "list", component: GooutList },
        { path: "read/:id", component: GooutRead },
        { path: "update", component: GooutUpdate },
      ],
    },

    {
      path: "/gooutType",
      component: GooutTypePage,
      children: [
        { path: "create", component: GooutTypeCreate },
        { path: "list", component: GooutTypeList },
        { path: "read/:id", component: GooutTypeRead },
        { path: "update", component: GooutTypeUpdate },
      ],
    },
  ],
});

export default router;

router.beforeEach(async (to, from, next) => {
  console.log("현재 경로:", to.path);
  const authRequiredRoutes = ["/main", "/manager", "/overtimecreate", "/overtimelist", "/overtimemodify", "/overtimeapprovea",
   "/approve/list", "/approve/create","/board/list", "/board/create", "/board/update","/goout/list", "/gooutType",
   "/salary/list","/goout/list","/goout/create","/goout/update","/gooutType/list","/gooutType/create","/gooutType/update",];
  // 동적 경로를 확인하는 함수 정의
  const isDynamicAuthRequired = (path) => {
    if (path.match(/^\/overtime|approve|board|goout|gooutType\/read\/\d+$/)) {
      // "/board/read/숫자" 형식의 경로에 대해 true 반환
      return true;
    }
    return false;
  };
  
  const authRequired = authRequiredRoutes.includes(to.path) || isDynamicAuthRequired(to.path);
  console.log("인증 필요 여부:", authRequired);

  const token = sessionStorage.getItem("token");
  const refreshToken = sessionStorage.getItem("refreshToken");

  if (!authRequired) {
    console.log("인증 필요 없음");
    next();
    return;
  }

  console.log("체크포인트1")
  if (!refreshToken) {
    next("/login");
    return;
  }

  // 토큰의 만료 여부를 확인합니다.
  if (token) {
    try {
      console.log("체크포인트2")
      const tokenData = VueJwtDecode.decode(token);
      const currentTime = Math.floor(Date.now() / 1000);

      if (tokenData.exp < currentTime && refreshToken) {
        // accessToken 만료 & refreshToken 존재
        console.log("체크포인트3")
        const response = await axios.post(`http://192.168.0.51/api/refresh/accessToken`, 
        {
          refreshToken: sessionStorage.getItem("refreshToken")
        }, { headers: { "Content-Type": "application/json",Authorization: "Bearer " + refreshToken, }});
   
        sessionStorage.setItem("token", response.data.result.token);
        sessionStorage.setItem("refreshToken", response.data.result.refreshToken);
        console.log("token 재발급 성공")
        next();
      } else {
        next();
      }
    } catch (error) {
      console.error('Token Processing Error:', error);
      forceLogout();
    }
  } else {
    forceLogout();
  }
}

);

function forceLogout() {
  sessionStorage.clear();
  router.push("/login");
}