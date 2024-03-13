import { createRouter, createWebHistory } from "vue-router";


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


const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: "/signup", component: SignUpPage },
    { path: "/login", component: LoginPage },
    { path: "/", component: MainPage },

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

