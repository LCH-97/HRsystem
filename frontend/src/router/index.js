import { createRouter, createWebHistory } from "vue-router";
import VueJwtDecode from "vue-jwt-decode";
import jwtDecode from 'jwt-decode';

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
    {
      path: '/overtimeapprovea',
      component: OvertimeApproveaPage,
      beforeEnter: (to, from, next) => {
        const token = sessionStorage.getItem('token');
        if (token) {
          const decoded = jwtDecode(token);  // 올바른 디코드 함수 사용
          if (decoded.ROLE === 'ROLE_ADMIN') {
            next();
          } else {
            alert('인사 관리자만 접근 가능합니다.');
            next(false);  // 현재 라우트 이동 취소
          }
        } else {
          alert('인증 정보가 필요합니다.');
          next(false);  // 현재 라우트 이동 취소
        }
      }
    },
    // { path: "/overtimeapprovea", component: OvertimeApproveaPage },
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

function isTokenExpired(token) {
  const decoded = VueJwtDecode.decode(token);
  const currentTime = Math.floor(Date.now() / 1000);
  return decoded.exp < currentTime;
}

async function refreshToken() {
  try {
    const refreshToken = sessionStorage.getItem("refreshToken");
    const response = await axios.post(`http://www.lch-hr-api.kro.kr/refresh/accessToken`, {
      refreshToken: refreshToken
    }, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + refreshToken,
      }
    });

    sessionStorage.setItem("token", response.data.result.token);
    sessionStorage.setItem("refreshToken", response.data.result.refreshToken);
    return true;
  } catch (error) {
    console.error('Error refreshing token:', error);
    return false;
  }
}

router.beforeEach(async (to, from, next) => {
  const authRequired = authRequiredRoutes.includes(to.path) || isDynamicAuthRequired(to.path);

  if (!authRequired) {
    next();
    return;
  }

  const token = sessionStorage.getItem("token");
  if (token && !isTokenExpired(token)) {
    next();
    return;
  }

  if (await refreshToken()) {
    next();
  } else {
    forceLogout();
  }
});
