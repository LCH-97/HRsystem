<template>

  <nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
    <!-- Navbar Brand-->
    <a class="navbar-brand ps-3" href="/main">Hello_R_loha</a>
    <!-- Sidebar Toggle-->
    <button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!"><svg
        class="svg-inline--fa fa-bars" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="bars"
        role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" data-fa-i2svg="">
        <path fill="currentColor"
          d="M0 96C0 78.3 14.3 64 32 64H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32H416c17.7 0 32 14.3 32 32z">
        </path>
      </svg><!-- <i class="fas fa-bars"></i> Font Awesome fontawesome.com --></button>
    <!-- Navbar Search-->


    <a class="navbar-brand ps-3" href="/manager" v-if="isAdmin">인사관리</a>

    <!-- Navbar-->

  </nav>
  <div id="popup-bg"></div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'HeaderComponent',
  components: {

  },
  data() {
    return {
      isAdmin: false,

    };
  },
  methods: {
    cookieCheck() {
      const cookieName = 'myCookie';
      const cookieValue = document.cookie.split('; ').find(row => row.startsWith(cookieName + '='));
      const isCookieExists = cookieValue !== undefined;
      console.log(isCookieExists); // 쿠키가 존재하면 true, 그렇지 않으면 false

    },
    checkIsAdmin() {
      console.log("getAuthorizeList START");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://192.168.0.51/api";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      axios.get(api + "/manager/check", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
        .then((response) => {
          console.log("checkIsAdmin is Loading");
          console.log(response);
          if (response.data.code == 200) {
            this.isAdmin = true;

            this.cookieCheck();
          }
          else {
            this.isAdmin = false;
          }

        })
        .catch((error) => {
          console.error("Error checkIsAdmin:", error);
          // alert();
          //throw new Error("권한 확인 실패");

        }).finally(() => {
          console.log("getAuthorizeList END");
        });

    },
  },
  mounted() {
    try {
      this.checkIsAdmin();
    } catch (error) {
      console.error("Error Header Init:", error);
    }


  },
}
</script>



<style scoped></style>