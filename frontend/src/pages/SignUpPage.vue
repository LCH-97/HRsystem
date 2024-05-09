<template>
  <div id="layoutSidenav_content1">
    <main>
      <div class="container-fluid px-4">
        <div class="container">
          <div class="card">
            <div class="card-header">
              <h1>회원가입</h1>
            </div>
            <div class="card-body">

              <div class="form-group">
                <h2 class="sub-heading">직원 정보</h2>
                <label for="inputName">이름</label>
                <input type="text" class="form-control" id="inputName" placeholder="이름을 입력하세요" v-model="name">
              </div>
              <div class="form-group">
                <label for="inputphonenumber">전화번호</label>
                <input type="phonenumber" class="form-control" id="inputphonenumber" placeholder="전화번호를 입력하세요"
                  v-model="phoneNum">
              </div>
              <div class="form-group">
                <label for="inputJobTitle">직급</label>
                <select class="form-control" id="inputJobTitle" v-model="positionId">
                  <option value="">선택하세요</option>
                  <option value="1">사원</option>
                  <option value="2">대리</option>
                  <option value="3">과장</option>
                  <option value="4">차장</option>
                  <option value="5">부장</option>
                  <option value="6">이사</option>
                  <option value="7">상무</option>
                  <option value="8">전무</option>
                  <option value="9">부사장</option>
                  <option value="10">사장</option>
                  <option value="11">부회장</option>
                  <option value="12">회장</option>
                </select>
              </div>
              <div class="form-group">
                <label for="inputdepartment">부서</label>
                <select class="form-control" id="inputdepartment" v-model="departmentId">
                  <option value="">선택하세요</option>
                  <option value="1">개발팀</option>
                  <option value="2">테스터팀</option>
                  <option value="3">디자인팀</option>
                  <option value="4">인사팀</option>
                </select>
              </div>

              <div class="form-group">
                <label for="inputHireDate">생년월일</label>
                <input type="date" class="form-control" id="inputHireDate" v-model="birth">
              </div>
              <div class="form-group">
                <label for="inputPasswordConfirm">주소</label>
                <input type="text" class="form-control" id="inputPasswordConfirm" placeholder="주소를 입력하세요"
                  v-model="address">
              </div>
              <h2 class="sub-heading">직원 커스텀 필드</h2>
              <div class="form-group">
                <label for="inputEmail">아이디</label>
                <input type="email" class="form-control" id="inputEmail" placeholder="아이디를 입력하세요(test1~test15까지는 이미 있습니다.)" v-model="username">
              </div>
              <div class="form-group">
                <label for="inputPassword">비밀번호</label>
                <input type="password" class="form-control" id="inputPassword" placeholder="비밀번호를 입력하세요"
                  v-model="password">
              </div>


              <div class="form-group">
                <button @click="register">회원가입</button>
              </div>

            </div>
          </div>
        </div>


      </div>
    </main>

  </div>
  <!-- popup창 -->

  <div id="popup-bg" v-show="popUpStatus"></div>

  <div id="popup-container" v-show="popUpStatus">
    <h2>{{ popUpTitle }}</h2>
    <p>{{ popUpText }}</p>
    <p>인사담당자 번호</p>
    <p>02-1111-2222</p>
    <p>테스트 중이시라면 admin 계정으로 로그인해주세요.<br /> 인사관리 페이지에서 직접 승인할 수 있습니다.</p>
    <p>id : admin <br /> pw : qwer1234</p>
    <button id="close-btn" @click="goMainPage">로그인 페이지로 이동</button>
  </div>




</template>


<script>
import axios from 'axios';
export default {
  name: 'SignUpPage',
  components: {

  },
  data() {
    return {
      responseData: null,
      name: null,
      birth: null,
      address: null,
      positionId: "",
      departmentId: "",
      username: null,
      password: null,
      popUpTitle: "",
      popUpText: "",
      popUpStatus: false,
      status: false,
    };
  },
  methods: {
    popUp() {
      this.popUpStatus = true;
    },
    goMainPage() {
      if(this.status)
        this.$router.push('/');
      else
        this.popUpStatus = false;
    },
    register() {
      console.log("click");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = 'http://www.lch-hr-api.kro.kr';
      const today = new Date();
      const birthDate = new Date(this.birth);
      let age = today.getFullYear()
        - birthDate.getFullYear()
        + 1;
      console.log(api);
      let formData = new FormData();
      formData.append('name', this.name);
      formData.append('phoneNum', this.phoneNum);
      formData.append('birth', "" + this.birth);
      formData.append('address', this.address);
      formData.append('age', age);
      formData.append('positionId', this.positionId);
      formData.append('departmentId', this.departmentId);
      formData.append('username', this.username);
      formData.append('password', this.password);
      axios.post(api + '/employee/signup', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
        .then(response => {
          console.log('Response:', response.data);
          this.responseData = response.data;

          // 결과가 200 이면 회원가입 성공했고 인사담당자에게 연락하라고 해야함.
          if (response.data.code == 200) {
            this.popUpTitle = "회원가입을 완료했습니다.";
            this.popUpText = "인사담당자에게 연락하세요.";
            this.status = true;
            this.popUp();
          }


        })
        .catch(error => {
          console.error('Error updating data:', error);
          this.popUpTitle = "회원가입에 실패하였습니다.";
          if(error.response.data.message.includes("UK")){
            this.popUpText = "아이디가 중복되었습니다. 다시 시도해주세요.";
          }
          this.popUp();

        });
    }
  },
}
</script>



<style>
.container {
  padding-top: 50px;
}

#layoutSidenav_content1 {
  padding-left: auto;

}



#popup-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 100;

}

/* 팝업창 내용 스타일 */
#popup-container {

  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 500px;
  height: 400px;
  background-color: white;
  border-radius: 10px;
  z-index: 101;
  padding: 20px;
  text-align: center;
}

/* 닫기 버튼 스타일 */
#close-btn {
  position: absolute;
  bottom: 10px;
  right: 10px;
  cursor: pointer;
  width: 180px;
  height: 30px;
  border-radius: 5px;
  background-color: #333;
  color: white;
  font-weight: bold;
}
</style>