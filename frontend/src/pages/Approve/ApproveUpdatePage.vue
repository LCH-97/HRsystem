<template>
  <HeaderComponent />
  <SideBar />
  <main>
    <div class="container">
      <div class="header">
        <h1>결재 수정하기</h1>
      </div>
      <div class="content">
        <form @submit.prevent="handleFormSubmission">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label for="title">제목</label>
                <textarea id="title" class="input" rows="1" v-model="updateInfo.title" required></textarea>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label for="content">내용</label>
                <textarea id="content" class="input" rows="7" v-model="updateInfo.content" required></textarea>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="form-group">
              <span class="label">결재자1:</span>
              <span class="input">{{ updateInfo.confirmer1Name }}</span>
              <button class="modalButton" @click="showModalForConfirmer1">결재자1 선택</button>
            </div>
            <div class="form-group">
              <span class="label">결재자2:</span>
              <span class="input">{{ updateInfo.confirmer2Name }}</span>
              <button class="modalButton" @click="showModalForConfirmer2">결재자2 선택</button>
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
        <div class="form-group">
            <label for="files">첨부파일</label>
            <input type="file" multiple @change="handleFilesUpload" />
          </div>
          <div class="button">
            <button type="submit" class="btn-submit">제출</button>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script>
import axios from "axios";
import DepartmentListModal from "../../components/DepartmentListModal.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import SideBar from "@/components/SideBar.vue";

export default {
  components: {
    SideBar,
    HeaderComponent,
    DepartmentListModal,
  },
  data() {
  return {
    backend: "http://192.168.0.51/api",
    updateInfo: {
      id: "",
      title: "",
      content: "",
      first: "",
      last: "",
      confirmer1Id: "",
      confirmer2Id: "",
      confirmer1Name: "",
      confirmer2Name: "",
    },
    employees: [],
    gooutTypes: [], // gooutTypes 배열 추가
    isModalVisibleForConfirmer1: false,
    isModalVisibleForConfirmer2: false,
    departments: [],
    files: [],
  };
},

  async created() {
    await this.fetchEmployees();
    await this.loadUpdateInfo();
    this.fetchDepartments();
  },

  watch: {
    // watch 속성 추가
    employees: {
      immediate: true,
      handler() {
        if (this.gooutTypes.length > 0 && this.employees.length > 0) {
          this.loadUpdateInfo();
        }
      },
    },
  },

  methods: {
    showModalForConfirmer1() {
      this.isModalVisibleForConfirmer1 = true;
    },
    showModalForConfirmer2() {
      this.isModalVisibleForConfirmer2 = true;
    },
    handleEmployeeSelectionForConfirmer1({ id, name }) {
      this.updateInfo.confirmer1Id = id;
      this.updateInfo.confirmer1Name = name;
      this.closeModalForConfirmer1();
    },
    handleEmployeeSelectionForConfirmer2({ id, name }) {
      this.updateInfo.confirmer2Id = id;
      this.updateInfo.confirmer2Name = name;
      this.closeModalForConfirmer2();
    },
    closeModalForConfirmer1() {
      this.isModalVisibleForConfirmer1 = false;
    },
    closeModalForConfirmer2() {
      this.isModalVisibleForConfirmer2 = false;
    },
    fetchDepartments() {
      const token = sessionStorage.getItem("token");
      axios
        .get(`${this.backend}/department/list`, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          this.departments = response.data.result; // 수정됨
        })
        .catch((error) => {
          console.error("부서 목록 조회 실패:", error);
        });
    },

    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },

    loadUpdateInfo() {
      const storedInfo = localStorage.getItem("updateApproveInfo");
      if (storedInfo) {
        const info = JSON.parse(storedInfo);
        this.updateInfo = info; // Directly bind the loaded data to updateInfo
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
      }
    },

    async fetchEmployees() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(`${this.backend}/employee/list`, {
          headers: {
            Authorization: "Bearer " + token,
          },
        });
        this.employees = response.data; // 백엔드 응답에 'result' 키가 없다면 이렇게 접근합니다.
      } catch (error) {
        console.error("직원 목록 로딩 실패:", error);
      }
    },

    async updateApprove() {
      if (this.updateInfo.employeeId === this.updateInfo.agentId) {
        alert(
          "결재 수정 실패: 신청직원의 ID와 대리자의 ID는 같을 수 없습니다."
        );
        return;
      }
      if (this.updateInfo.confirmer1Id === this.updateInfo.confirmer2Id) {
        alert(
          "결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다."
        );
        return;
      }

      const token = sessionStorage.getItem("token");
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

      // 여러 파일을 formData에 추가
      for (let i = 0; i < this.files.length; i++) {
        formData.append("uploadFiles", this.files[i]); // 'uploadFiles'로 변경
      }

      // 요청 헤더에 인증 토큰 추가
      const config = {
        headers: {
          // 여기에서 Authorization 헤더에 토큰 추가
          Authorization: `Bearer ${token}`,
        },
      };

      try {
        const response = await axios.post(
          `${this.backend}/approve/create`,
          formData,
          config
        );
        console.log(response);

        const approveId = response.data.result;
        console.log("Created approve ID:", approveId);

        alert("결재가 재등록되었습니다.");
        this.$router.push(`/approve/list`);
      } catch (error) {
        console.error("결재 등록 실패:", error);
        if (error.response) {
          alert("결재 등록 실패: " + error.response.data.message);
        } else {
          alert("결재 등록 실패: 서버에서 응답이 없습니다.");
        }
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1254px;
  margin: 0 auto;
  margin-left: 250px;
  margin-top: 50px;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  position: relative;
  left: 4px;
  top: 14px;
}
.header h1 {
  font-size: 24px;
  margin-top: 15px;
  margin-top: 15px;
  margin-bottom: 32px;
  color: black;
  font-weight: bold;
}
.content {
  padding: 15px;
}
.row {
  display: flex;
  margin-bottom: 10px;
}
.col-md-6 {
  flex: 1;
}
.form-group {
  margin-bottom: 15px;
}
.label {
  width: 100px;
  text-align: right;
  margin-right: 10px;
  font-weight: bold;
}
.input {
  flex: 1;
  padding: 5px;
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 4px;
}
textarea {
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.button {
  display: flex;
  justify-content: flex-end;
}
.btn-submit {
  background-color: black;
  color: white;
  border: none;
  border-radius: 8px;
  width: 100px;
  height: 50px;
  font-size: 16px;
  font-weight: bold;
  transition: 0.3s;
}
.btn-submit:hover {
  background-color: #f7941e;
}
.modalButton {
  font-size: 13px;
  padding: 5px 10px;
  cursor: pointer;
  background-color: #111111;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 10px;
}
</style>
