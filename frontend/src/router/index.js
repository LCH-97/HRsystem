import { createRouter, createWebHistory } from "vue-router";
import VueJwtDecode from "vue-jwt-decode";


import OvertimeCreatePage from '../pages/OvertimeCreatePage.vue';
import OvertimeListPage from '../pages/OvertimeListPage.vue';
import OvertimeModifyPage from '../pages/OvertimeModifyPage.vue';
import OvertimeApproveaPage from '../pages/OvertimeApproveaPage.vue';
import OvertimeDetailPage from '../pages/OvertimeDetailPage.vue';



import ApproveListPage from "@/pages/Approve/ApproveListPage.vue";

import ApproveCreatePage from "@/pages/Approve/ApproveCreatePage.vue"
import ApproveReadPage from "@/pages/Approve/ApproveReadPage.vue"


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

import BoardListPage from "@/pages/BoardListPage.vue";
import BoardReadPage from "@/pages/BoardReadPage.vue";
import ManagerPage from "@/pages/ManagerPage.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/", component: LoginPage },
    { path: "/login", component: LoginPage },

    { path: "/main", component: MainPage },
    { path: "/manager", component: ManagerPage },
    { path: "/overtimecreate", component: OvertimeCreatePage},
    { path: "/overtimelist", component: OvertimeListPage},
    { path: "/overtimemodify", component: OvertimeModifyPage},
    { path: "/overtimeapprovea", component: OvertimeApproveaPage},
    { path: "/overtime/read/:id", component: OvertimeDetailPage},

    { path: "/approve/list", component: ApproveListPage },

    { path: "/approve/create", component: ApproveCreatePage },
    { path: "/approve/read/:id", component: ApproveReadPage },


    { path: "/board", component: BoardListPage },
    { path: "/board/read/:id", component: BoardReadPage },



    { path: "/goout",
      component: GooutPage,
      children: [
        { path: "create", component: GooutCreate },
        { path: "list", component: GooutList },
        { path: "read/:id", component: GooutRead },
        { path: "update", component: GooutUpdate },
      ],
    },
    
    { path: "/gooutType",
      component: GooutTypePage,
      children: [
        { path: "create", component: GooutTypeCreate },
        { path: "list", component: GooutTypeList },
        { path: "read/:id", component: GooutTypeRead },
        { path: "update", component: GooutTypeUpdate },
      ],
    }

  ],
},
);

export default router;

router.beforeEach((to, from, next) => {
  // 로그인이 필요한 페이지
  const authPages = [
    "/main",
    "/manager",
    "/overtimecreate",
    "/overtimelist",
    "/overtimemodify",
    "/overtimeapprovea",
    "/overtime",
    "/approve",
    "/goout",
    "/gooutType",
    
  ];

  if (authPages.includes(to.fullPath)) {
    const storedToken = sessionStorage.getItem("token");
    if (storedToken === null) {
      next("/UserLogIn");
    } else {
      const tokenData = VueJwtDecode.decode(storedToken);

      console.log(tokenData);
      const currentTime = Math.floor(Date.now() / 1000);
      if (tokenData.exp < currentTime) {
        sessionStorage.removeItem("token");
        next("/UserLogIn");
      } else {
        next();
      }
    }
  } else { // 권한이 필요한 페이지가 아니라면
    

    next();
  }
});


