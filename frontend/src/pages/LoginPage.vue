<template>
  <div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
      <main>
        <div class="container">
          <div class="row justify-content-center">
            <div class="col-lg-10">
              <div class="card shadow-lg border-0 rounded-lg mt-5">
                <div class="card-header">
                  <h3 class="text-center font-weight-light my-4">Login</h3>
                </div>
                <div class="card-body">
                  <form>
                    <div class="form-floating mb-3">
                      <input class="form-control" id="inputEmail" type="email" placeholder="name@example.com"
                        v-model="username" />
                      <label for="inputEmail">Email address</label>
                    </div>
                    <div class="form-floating mb-3">
                      <input class="form-control" id="inputPassword" type="password" placeholder="Password"
                        v-model="password" />
                      <label for="inputPassword">Password</label>
                    </div>
                    <div class="form-check mb-3">
                      <label class="form-check-label" for="inputRememberPassword">관리자 계정으로 모든 테스트가 가능합니다.</label>
                      <label class="form-check-label" for="inputRememberPassword">id: admin, pw: qwer1234</label>
                    </div>
                    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                      <a class="small" href="password.html">Forgot Password?</a>
                      <a class="btn btn-primary" @click="login">Login</a>
                    </div>
                  </form>
                </div>
                <div class="card-footer text-center py-3">
                  <div class="small"><a href="/signup">Need an account? Sign up!</a></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

  </div>




  <div id="popup-bg" v-show="popUpStatus"></div>

  <div id="popup-container" v-show="popUpStatus">
    <h2 v-text="popTitle"></h2>
    <p v-text="popText"></p>
    <p>담당자 번호</p>
    <p>02-1111-2222</p>
    
    <button id="close-btn" @click="closePop">닫기</button>
  </div>

  <LoadingPage v-if="isLoading" @close-event="close" />

</template>


<script scope>

import axios from 'axios';
import LoadingPage from "@/components/LoadingPage.vue";
export default {
  name: 'LoginPage',
  components: {
    LoadingPage,
  },
  data() {
    return {
      popUpStatus: false,
      popTitle: "",
      popText: "",
      responseData: null,
      username: '',
      password: '',
      isLoading: false,
    };
  },
  methods: {
    close() {
      this.isLoading = false;
    },
    closePop() {
      this.popUpStatus = false;
    },
    login() {
      console.log("click");
      this.isLoading = true;
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = 'http://192.168.0.51/api';
      console.log(api);
      let formData = new FormData();
      formData.append('username', this.username);
      formData.append('password', this.password);
      axios.post(api + '/employee/login', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          console.log('Response:', response.data);
          if (response.data.code === 200) {
            this.responseData = response.data;
            sessionStorage.setItem('token', response.data.result.token);
            sessionStorage.setItem('refreshToken', response.data.result.refreshToken);
            // sessionStorage.setItem('refreshTokenKey', response.data.result.refreshTokenKey);
            // main page로 이동
            sessionStorage.setItem('name', response.data.result.name);
            this.$router.push('/main');
          }

        })
        .catch(error => {
          console.error('Error Login:', error);
          
          if (error.code === "ERR_NETWORK") {
            this.popTitle = "로그인에 실패하였습니다.";
            this.popText = "ERR_NETWORK: 서버와의 통신이 실패하였습니다.";
          }
          else if (error.response.data.code === "USER-004") {
            this.popTitle = "로그인에 실패하였습니다.";
            this.popText = "다시 시도해주세요.";
          } else if (error.response.data.code === "EMPLOYEE-001") {
            this.popTitle = "미승인 계정입니다.";
            this.popText = "인사담당자에게 연락하세요.";
          } else if (axios.isAxiosError(error)){
            this.popTitle = "로그인에 실패하였습니다.";
            this.popText = "AxiosError: 서버와의 통신이 실패하였습니다. ";
          
          }
          else {
            this.popTitle = "예상하지 못한 에러입니다. ";
            this.popText = "서버 관리자에게 연락하세요.";
          }
          this.popUpStatus = true;
        }).finally(()=>{
          this.isLoading = false;
        });
    }
  },
}
</script>



<style>
.container {
  padding-top: 50px;
}

#layoutSidenav_content {
  padding-left: 225px;
  top: 56px;
}
</style>