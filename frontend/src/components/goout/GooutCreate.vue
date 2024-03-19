<template>
  <div class="container">
    <div class="header">
      <h1>휴가 신청</h1>
    </div>
    <div class="content">
      <form @submit.prevent="handleFormSubmission">
        <div class="row">
          <div class="label">신청자</div>
          <div class="input">
            <input type="text" v-model="name" required>
          </div>
        </div>
        <div class="row">
          <div class="label">대리인</div>
          <div class="input">
            <input type="text" v-model="name" required>
          </div>
        </div>
        <div class="row">
          <div class="label">근태 사유</div>
          <div class="input">
            <select v-model="leaveReason" required>
              <option value="">선택</option>
              <option value="연차휴가">연차휴가</option>
              <option value="병가">병가</option>
              <option value="출장">출장</option>
              <option value="기타">기타</option>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="label">기간</div>
          <div class="input">
            <input type="date" v-model="startDate" required>
            <span>~</span>
            <input type="date" v-model="endDate" required>
          </div>
        </div>
        <div class="row">
          <div class="label">비고</div>
          <div class="input">
            <textarea v-model="note"></textarea>
          </div>
        </div>
          <div class="row">
            <div class="label">첨부파일</div>
            <div class="input">
              <input type="file" multiple @change="handleFilesUpload">
            </div>
          </div>
        <div class="row">
          <div class="label">결재라인</div>
          <div class="input">
            <select v-model="confirmer1Id" required>
              <option value="">결재자 1 선택</option>
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select>
            <select v-model="confirmer2Id" required>
              <option value="">결재자 2 선택</option>
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="button">
            <button type="submit">제출</button>
          </div>
        </div>
      </form>
    </div>
  </div>
  </template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      backend: "http://localhost:8080",
      gooutTypeId: "",
      agentId: "",
      employeeId: "",
      first: "",
      last: "",
      gooutTypes: [],
      employees: [],
      files: [], // 여러 파일을 저장할 배열
      confirmer1Id: "",
      confirmer2Id: "",
      loggedInUserId: null, // 로그인한 사용자 ID 저장
    }
  },

  async created() {
    await this.fetchGooutTypes();
    await this.fetchEmployees();
  },
  mounted() {
    this.setLoggedInUser();
  },
  methods: {

    
    setLoggedInUser() {
      const token = sessionStorage.getItem('token'); // 로컬 스토리지 또는 적절한 저장소에서 토큰 가져오기
      if (token) {
        const decoded = jwtDecode(token);
        this.loggedInUserId = decoded.ID; // 실제 토큰 구조에 따라 변경될 수 있음
      }
    },

    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchGooutTypes() {
      const response = await axios.get(`${this.backend}/gooutType/list`);
      this.gooutTypes = response.data.result;
    },
    async fetchEmployees() {
      const response = await axios.get(`${this.backend}/employee/list`);
      this.employees = response.data;
    },
    async getGooutCreate() {

      if (this.confirmer1Id === this.confirmer2Id) {
    alert("결재라인 생성 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
    return; // 메소드 실행을 중단
  }

  let formData = new FormData();
  formData.append('gooutCreateReq', new Blob([JSON.stringify({
    agentId: this.agentId,
    employeeId: this.employeeId,
    gooutTypeId: this.gooutTypeId,
    first: this.first,
    last: this.last,
  })], {type : 'application/json'}));

  // 여러 파일을 formData에 추가
  for (let i = 0; i < this.files.length; i++) {
  formData.append('uploadFiles', this.files[i]); // 'uploadFiles'로 변경
}

  try {
    const response = await axios.post(`${this.backend}/goout/create`, formData);
    console.log(response);
    const gooutId = response.data.result;
    console.log('Created goout ID:', gooutId);
    await this.createGooutLine(gooutId);
  } catch (error) {
    console.error("등록 실패:", error);
    alert("휴가 등록 실패: " + error.response.data.message); // 서버에서 반환한 오류 메시지를 사용자에게 보여줌
  }
},

async createGooutLine(gooutId) {
  this.setLoggedInUser();
  try {
    const gooutLineReq = {
      confirmer1Id: this.confirmer1Id,
      confirmer2Id: this.confirmer2Id,
      gooutId: gooutId,
      employeeId: this.loggedInUserId,  
    };
    const response = await axios.post(`${this.backend}/gooutLine/create`, gooutLineReq, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log("GooutLine 생성 성공:", response);
    alert("휴가 등록 및 결재라인 생성 완료");
    this.$router.push("/goout/list");
  } catch (error) {
    console.error("결재라인 생성 실패:", error);
    alert("결재라인 생성 실패: " + error.response.data.message);
  }
},

    async handleFormSubmission() {
        await this.getGooutCreate();
    }
  },
}






</script>

<style>
.container {
  width: 500px;
  margin: 20px auto;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.header {
  background-color: #f7f8fa;
  padding: 10px;
  text-align: center;
}

.header h1 {
  font-size: 20px;
  margin: 0;
}

.content {
  padding: 20px;
}

.row {
  display: flex;
  margin-bottom: 10px;
}

.label {
  width: 100px;
  text-align: right;
  margin-right: 10px;
}

.input {
  flex: 1;
}

input,
select,
textarea {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 5px;
  width: 100%;
}

textarea {
  height: 100px;
}

.button {
  text-align: center;
}

button {
  padding: 5px 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f7f8fa;
  cursor: pointer;
}

button:hover {
  background-color: #ddd;
}
</style>