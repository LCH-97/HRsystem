<template>
  <HeaderComponent />
  <SideBar />
  <main>
    <div class="approve-create-all">
      <h2 class="mt-4">결재 만들기</h2>
      <br />
      <div class="row">
        <div class="form-group">
          <label for="title">제목</label>
          <textarea placeholder="제목을 입력하세요." id="title" class="form-control" rows="1" v-model="title" required></textarea>
          <br /><br />
          <label for="content">내용:</label>
          <textarea placeholder="내용을 입력하세요." id="content" class="form-control" rows="10" v-model="content" required></textarea>
        </div>
        <br /><br />
        <div class="approver">
            <span class="approver1">결재자1:</span>
             <span class="approver1-name"> {{ confirmer1Name }} </span>
            <button class="modalButton1" @click="showModalForConfirmer1">결재자1 선택</button>
        </div>
        <div class="approver">
            <span class="approver2">결재자2:</span>
            <span class="approver2-name">{{ confirmer2Name }}</span>
            <button class="modalButton2" @click="showModalForConfirmer2">결재자2 선택</button>
        </div>

        <department-list-modal
          v-if="isModalVisibleForConfirmer1"
          :departments="departments"
          :backend="backend"
          v-model:isVisible="isModalVisibleForConfirmer1"
          @confirm="handleEmployeeSelectionForConfirmer1"
          @close="closeModalForConfirmer1"
        ></department-list-modal>

        <department-list-modal
          v-if="isModalVisibleForConfirmer2"
          :departments="departments"
          :backend="backend"
          v-model:isVisible="isModalVisibleForConfirmer2"
          @confirm="handleEmployeeSelectionForConfirmer2"
          @close="closeModalForConfirmer2"
        ></department-list-modal>
        <br />
        <div class="row">
          <div class="label">첨부파일</div>
          <div class="input">
            <input type="file" multiple @change="handleFilesUpload">
          </div>
        </div>
        <br/>
        <button @click="handleFormSubmission">제 출</button>
      </div>
    </div>
  </main>
</template>
<script>
import axios from "axios";
import  jwtDecode  from "jwt-decode";
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import DepartmentListModal from "@/components/DepartmentListModal.vue"
export default {
  name: "ApproveCreatePage",
  components: {
    SideBar,
    HeaderComponent,
    DepartmentListModal,
  },
  data() {
    return {
      backend: "http://www.hello-r-loha.kro.kr/api",
      title: "",
      content: "",
      confirmer1Id: "",
      confirmer2Id: "",
      confirmer1Name: "",
      confirmer2Name: "",
      employees: [],
      files: [],
      loggedInUserId: null, // 로그인한 사용자 ID 저장
      isModalVisibleForConfirmer1: false,
      isModalVisibleForConfirmer2: false,
      departments: [],
    };
  },
  async created() {
    await this.fetchEmployees();
    this.fetchDepartments();
  },
  mounted() {
    this.setLoggedInUser();
  },
  methods: {
    showModalForConfirmer1() {
      this.isModalVisibleForConfirmer1 = true;
    },
    showModalForConfirmer2() {
      this.isModalVisibleForConfirmer2 = true;
    },
    handleEmployeeSelectionForConfirmer1({ id, name }) {
      this.confirmer1Id = id;
      this.confirmer1Name = name;
      this.isModalVisibleForConfirmer1 = false; // 모달 닫기
    },
    handleEmployeeSelectionForConfirmer2({ id, name }) {
      this.confirmer2Id = id;
      this.confirmer2Name = name;
      this.isModalVisibleForConfirmer2 = false;
     },
    closeModalForConfirmer1() {
      this.isModalVisibleForConfirmer1 = false;
    },
    closeModalForConfirmer2() {
      this.isModalVisibleForConfirmer2 = false;
    },
    fetchDepartments() {
      const token = sessionStorage.getItem("token");
      axios.get(`${this.backend}/department/list`,{
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      }).then((response) => {
        this.departments = response.data.result; // 수정됨
      }).catch(error => {
        console.error("부서 목록 조회 실패:", error);
      });
    },
    setLoggedInUser() {
      const token = sessionStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        console.log("Decoded:", decoded);
        this.loggedInUserId = decoded.ID;
        console.log("Logged In User ID:", this.loggedInUserId);
      }
    },

    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchEmployees() {
      const token = sessionStorage.getItem("token");
      const response = await axios.get(`${this.backend}/employee/list`,{
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });
      this.employees = response.data;
    },
    async ApproveCreate() {
      if (this.confirmer1Id === this.confirmer2Id) {
        alert("결재자1과 결재자2는 동일할 수 없습니다.");
        return;
      }
      this.setLoggedInUser();
      let formData = new FormData();
      formData.append(
        "approveCreateReq",
        new Blob(
          [
            JSON.stringify({
              title: this.title,
              content: this.content,
              employeeId: this.loggedInUserId,
              confirmer1Id: this.confirmer1Id,
              confirmer2Id: this.confirmer2Id
            }),
          ],
          { type: "application/json" }
        )
      );

      // 파일들을 formData에 추가
      Array.from(this.files).forEach((file) => {
        formData.append("uploadFiles", file);
      });

      try {
        // 서버에 결재 생성 요청 전송
        const token = sessionStorage.getItem("token");
        const response = await axios.post(
          `${this.backend}/approve/create`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",Authorization: "Bearer " + token,
            },
          }
        );
        this.id = response.data.result;
        console.log(response);
        if (response.data && response.data.isSuccess) {
          // 결재 생성 성공 처리
          alert("결재가 성공적으로 생성되었습니다.");
          this.$router.push("/approve/list"); // 예시: 결재 목록 페이지로 이동
        } 
      } catch (error) {
        if(error.response && error.response.data) {
        // 에러 처리
        alert(`에러: ${error.response.data.message}`);
      }
    }
    },
    async createApproveLine1(approveId) {
      try{
        const approveLineReq = {
          confirmerId: this.confirmer1Id,
          approveId: approveId,
        };
        const response = await axios.post(`${this.backend}/approve/line/create`, approveLineReq, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    console.log("ApproveLine1 생성 성공:", response);
  } catch (error) {
    console.error("결재라인 생성 실패:", error);
    alert("결재라인 생성 실패: " + error.response.data.message);
  }
},
async createApproveLine2(approveId) {
      try{
        const approveLineReq = {
          confirmerId: this.confirmer2Id,
          approveId: approveId,
        };
        const response = await axios.post(`${this.backend}/approve/line/create`, approveLineReq, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    console.log("ApproveLine2 생성 성공:", response);
    alert("휴가 등록 및 결재라인1, 2 생성 완료");
    this.$router.push("/approve/list");
  } catch (error) {
    console.error("결재라인 생성 실패:", error);
    alert("결재라인 생성 실패: " + error.response.data.message);
  }
},
    
    async handleFormSubmission() {
      await this.ApproveCreate();
    },
  },
};
</script>
<style scoped>
.approve-create-all {
  display: 100%;
  margin-left: 650px;
  margin-top: 100px;
}
button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: 15px 0px 15px 10px;
  width: 100px;
}
button:hover {
  background-color: #f75c29;
}


  .modalButton1, .modalButton2 {
    font-size: 13px;
    padding: 5px 10px; 
    cursor: pointer; 
    background-color: #111111; 
    color: white; 
    border: none; 
    border-radius: 5px;
    margin-left: 100px;
  }
  .approver {
    position: relative;
  }
  .approver1-name, .approver2-name {
    margin-left: 10px;
    min-width: 100px; /* 최소 너비 설정 */
    display: inline-block; /* 블록 요소처럼 너비와 높이 적용 */
    height: 20px;
  }


.form-control {
  width: 50%;
  resize: none;
}

.confirmer {
  margin-top: 50px;
  width: 100px;
  margin-bottom: 10px;
}
.upload {
  margin-top: -12px;
}
</style>
