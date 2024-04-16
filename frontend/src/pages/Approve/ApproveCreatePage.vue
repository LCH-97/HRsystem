<template>
  <HeaderComponent />
  <SideBar />
  <main>
    <div class="container">
      <div class="header">
        <h1>결재 신청</h1>
      </div>
      <div class="content">
        <div class="row">
          <div class="form-group">
            <label for="title">제목</label>
            <textarea placeholder="제목을 입력하세요." id="title" class="form-control" rows="1" v-model="title" required></textarea>
            <br /><br />
            <label for="content">내용:</label>
            <textarea placeholder="내용을 입력하세요." id="content" class="form-control" rows="7" v-model="content" required></textarea>
          </div>
          <br /><br />
          <div class="row">
            <div class="col-md-6">
              <div class="goout">
                <span class="goout1">결재자1:</span>
                <span class="goout1-name">{{ confirmer1Name }}</span>
                <button class="modalButton1" @click="showModalForConfirmer1">결재자1 선택</button>
              </div>
            </div>
            <div class="col-md-6">
              <div class="goout">
                <span class="goout2">결재자2:</span>
                <span class="goout2-name">{{ confirmer2Name }}</span>
                <button class="modalButton2" @click="showModalForConfirmer2">결재자2 선택</button>
              </div>
            </div>
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
          <div class="button">
            <button @click="handleFormSubmission" class="btn-submit">제출</button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import axios from "axios";
import jwtDecode from "jwt-decode";
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import DepartmentListModal from "@/components/DepartmentListModal.vue";

export default {
  name: "ApproveCreatePage",
  components: {
    SideBar,
    HeaderComponent,
    DepartmentListModal,
  },
  data() {
    return {
      backend: "http://192.168.0.51/api", // 변경된 백엔드 URL 적용
      title: "",
      content: "",
      confirmer1Id: "",
      confirmer2Id: "",
      confirmer1Name: "",
      confirmer2Name: "",
      employees: [],
      files: [],
      loggedInUserId: null,
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
      this.isModalVisibleForConfirmer1 = false;
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
      axios.get(`${this.backend}/department/list`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      }).then((response) => {
        this.departments = response.data.result;
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
      this.files = event.target.files;
    },
    async fetchEmployees() {
      const token = sessionStorage.getItem("token");
      const response = await axios.get(`${this.backend}/employee/list`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });
      this.employees = response.data;
    },
    async handleFormSubmission() {
      await this.ApproveCreate();
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
      Array.from(this.files).forEach((file) => {
        formData.append("uploadFiles", file);
      });
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.post(
          `${this.backend}/approve/create`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              Authorization: "Bearer " + token,
            },
          }
        );
        this.id = response.data.result;
        console.log(response);
        if (response.data && response.data.isSuccess) {
          alert("결재가 성공적으로 생성되었습니다.");
          this.$router.push("/approve/list");
        }
      } catch (error) {
        if (error.response && error.response.data) {
          alert(`에러: ${error.response.data.message}`);
        }
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1240px; /* 더 큰 너비로 조정 */
  margin: 50px auto; /* 중앙 정렬 및 위쪽 여백 추가 */
  padding: 15px 20px; /* 내부 패딩은 변경하지 않음 */
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  position: relative;
  left: 100px; /* 코드 1과 동일한 위치 조정 */
  top: 15px; /* 코드 1과 동일한 위치 조정 및 아래쪽 여백 추가 */
}

.header h1 {
  font-size: 22px;
  margin-bottom: 15px;
  color: black;
  font-weight: bold;
}
.content {
  padding: 10px;
}
.row {
  display: flex;
  margin-bottom: 5px;
}
.input, .form-control {
  flex: 1;
  padding: 8px;
}
input, select, textarea {
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 4px;
  width: 100%;
}
.button {
  display: flex;
  justify-content: flex-end;
}
.btn-submit, .modalButton1, .modalButton2 {
  background-color: black;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 14px;
  font-weight: bold;
  transition: background-color 0.3s;
}
.btn-submit:hover {
  background-color: #F7941E;
}
.modalButton1, .modalButton2 {
  margin-left: 20px;
}
.goout1-name, .goout2-name {
  margin-left: 10px;
  min-width: 100px;
  display: inline-block;
  height: 25px;
}
.goout {
  margin-bottom: 20px; /* 결재자 1과 2 사이 간격 조정 */
}

.label {
  margin-top: 20px; /* 첨부파일 위쪽 간격 조정 */
}
.form-group {
  margin-bottom: 20px; /* 내용과 결재자 1, 결재자 2 사이 간격 조정 */
}

</style>