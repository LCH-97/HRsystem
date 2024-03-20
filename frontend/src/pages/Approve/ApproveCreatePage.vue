<template>
  <div id="layoutSidenav_content">
    <main>
      <div class="container-fluid px-4">
        <h1 class="mt-4">결재 만들기</h1>
        <div class="row">
          <div class="col-xl-3 col-md-6">
            <div class="input-group mb-3">
              <span>제목</span>
              <input
                class="form-control"
                type="text"
                placeholder="제목 입력"
                v-model="title"
              />
            </div>
            <br/><br/>
            <div class="form-group">
              <label for="content">내용:</label>
              <textarea
                id="content"
                class="form-control"
                rows="10"
                v-model="content"
                required
              ></textarea>
            </div>
            <br/><br/>
            <br/><br/>
              <p>결재자1</p>
          <select v-model="confirmer1Id">
            <option
              v-for="employee in employees"
              :key="employee.id"
              :value="employee.id"
            >
              {{ employee.name }}
            </option></select
          ><br />

          <p>결재자2</p>
          <select v-model="confirmer2Id">
            <option
              v-for="employee in employees"
              :key="employee.id"
              :value="employee.id"
            >
              {{ employee.name }}
            </option></select
          ><br />
              <br/><br/>
            <div class="input-group mb-3">
              <span>첨부파일</span>
              <input
                type="file"
                @change="handleFilesUpload"
                class="upload"
                multiple
              />
            </div>
            <br /><br /><br /><br /><br />
            <button @click="handleFormSubmission">제출</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>
<script>
import axios from "axios";
import { jwtDecode } from 'jwt-decode';

export default {
  name: "ApproveCreatePage",
  data() {
    return {
      backend: "http://localhost:8080",
      title: "",
      content: "",
      confirmer1Id: "",
      confirmer2Id: "",
      employees: [],
      files: [],
      loggedInUserId: null, // 로그인한 사용자 ID 저장
    }
  },
  async created() {
    await this.fetchEmployees();

  },
  mounted() {
    this.setLoggedInUser();
  },
  methods: {
    setLoggedInUser() {
  const token = sessionStorage.getItem('token');
  if (token) {
    const decoded = jwtDecode(token);
    console.log('Decoded:', decoded); // 디코드된 토큰 출력
    this.loggedInUserId = decoded.ID;
    console.log('Logged In User ID:', this.loggedInUserId); // 사용자 ID 출력
  }
},
   
    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchEmployees() {
      const response = await axios.get(`${this.backend}/employee/list`);
      this.employees = response.data;
    },
    async getApproveCreate() {

   if (!this.title || !this.content) {
    alert("제목과 내용을 입력해주세요.");
    return;
  }
  if (this.confirmer1Id === this.confirmer2Id) {
    alert("결재자1과 결재자2는 동일할 수 없습니다.");
    return;
  }

  let formData = new FormData();
  formData.append("approveCreateReq", new Blob([JSON.stringify({
    title: this.title,
    content: this.content,
    employeeId: this.loggedInUserId,
  })], { type: "application/json" }));

  // 파일들을 formData에 추가
  Array.from(this.files).forEach(file => {
    formData.append("uploadFiles", file);
  });

  try {
    // 서버에 결재 생성 요청 전송
    const response = await axios.post(`${this.backend}/approve/create`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    this.id = response.data.result;
    console.log(response);
    if (response.data && response.data.isSuccess) {
      // 결재 생성 성공 처리
      alert("결재가 성공적으로 생성되었습니다.");
      this.$router.push("/approve/list"); // 예시: 결재 목록 페이지로 이동
    } else {
      // 서버로부터 받은 에러 메시지 표시
      alert(`결재 생성 실패: ${response.data.message}`);
    }
  } catch (error) {
    // 에러 처리
    console.error("결재 생성 과정에서 에러 발생", error);
    alert("결재 생성에 실패했습니다.");
  }
},

async createApproveLine(approveId) {
  if (!approveId) {
    alert("결재 ID가 제공되지 않았습니다.");
    return;
  }
  this.setLoggedInUser();
  try {
  const approveLineReq = {
    confirmer1Id: this.confirmer1Id,
    confirmer2Id: this.confirmer2Id,
    employeeId: this.loggedInUserId,
    approveId: approveId,
  };

    const response = await axios.post(`${this.backend}/approve/line/create`, approveLineReq, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log("ApproveLine 생성 성공:", response);
    this.$router.push("/approve/list").then(() => {
  location.reload();
});
  } catch (error) {
    console.error("결재라인 생성 실패", error);
    alert("결재라인 생성 실패: " + error.response.data.message);
  }
},
async handleFormSubmission() {
  await this.getApproveCreate();
  await this.createApproveLine(this.id);
}
  }
}


</script>
<style scoped>
/* 여기에 스타일을 추가할 수 있습니다. */
</style>
