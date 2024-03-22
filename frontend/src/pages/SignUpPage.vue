<template>
  <HeaderComponent />
  <SideBar />

  <div id="layoutSidenav_content">
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
                <input type="password" class="form-control" id="inputPasswordConfirm" placeholder="주소를 입력하세요"
                  v-model="address">
              </div>
              <h2 class="sub-heading">직원 커스텀 필드</h2>
              <div class="form-group">
                <label for="inputEmail">아이디</label>
                <input type="email" class="form-control" id="inputEmail" placeholder="이메일 주소를 입력하세요" v-model="username">
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
    <footer class="py-4 bg-light mt-auto">
      <div class="container-fluid px-4">
        <div class="d-flex align-items-center justify-content-between small">
          <div class="text-muted">Copyright © Your Website 2023</div>
          <div>
            <a href="#">Privacy Policy</a>
            ·
            <a href="#">Terms &amp; Conditions</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
  <!-- popup창 -->

  <div id="popup-bg" v-show="popUpStatus"></div>

  <div id="popup-container" v-show="popUpStatus">
    <h2>회원가입이 완료 됐습니다.</h2>
    <p>인사담당자에게 연락하세요</p>
    <p>인사담당자 번호</p>
    <p>02-1111-2222</p>
    <button id="close-btn" @click="goMainPage">메인 페이지로 이동</button>
  </div>




</template>


<script>
import SideBar from '../components/SideBar.vue'
import HeaderComponent from '../components/HeaderComponent.vue'
import axios from 'axios';
export default {
  name: 'SignUpPage',
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      responseData: null,
      name: '등록 중',
      birth: '1999-11-11',
      address: 'test',
      age: '18',
      apositionId: '1',
      departmentId: '1',
      username: 'test1',
      password: 'qwer1234',

      popUpStatus: false,
    };
  },
  methods: {
    popUp() {
      this.popUpStatus = true;
    },
    goMainPage() {
      this.$router.push('/');
    },
    register() {
      console.log("click");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = 'http://192.168.0.51/api';
      console.log(api);
      let formData = new FormData();
      formData.append('name', this.name);
      formData.append('phoneNum', this.phoneNum);
      formData.append('birth', this.birth);
      formData.append('address', this.address);
      formData.append('age', this.age);
      formData.append('positionId', this.apositionId);
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
          if(response.data.code == 200){
            this.popUp();
          }
          

        })
        .catch(error => {
          console.error('Error updating data:', error);
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
  width: 400px;
  height: 300px;
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