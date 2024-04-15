<template>
  <div class="container">
    <div class="header">
      <h1>휴가 정보 수정하기</h1>
    </div>
    <div class="content">
      <div @submit.prevent="updateGoout">
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="employee">신청직원</label>
              <div class="input">
                <select v-model="updateInfo.employeeId">
                  <option
                    v-for="employee in employees"
                    :key="employee.id"
                    :value="employee.id"
                  >
                    {{ employee.name }}
                  </option></select
                ><br />
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="agent">대리인</label>
              <div class="input">
                <select v-model="updateInfo.agentId">
                  <option
                    v-for="agent in employees"
                    :key="agent.id"
                    :value="agent.id"
                  >
                    {{ agent.name }}
                  </option></select
                ><br />
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="reason">근태 사유</label>
              <div class="input">
                <select v-model="updateInfo.gooutTypeId">
                  <option
                    v-for="gooutType in gooutTypes"
                    :key="gooutType.id"
                    :value="gooutType.id.toString()"
                  >
                    {{ gooutType.name }}
                  </option></select
                ><br />
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="start-date">시작 날짜</label>
              <div class="input">
                <input type="date" v-model="updateInfo.first" /><br />
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="end-date">종료 날짜</label>
              <div class="input">
                <input type="date" v-model="updateInfo.last" /><br />
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="files">첨부파일</label>
              <div class="input">
                <input type="file" multiple @change="handleFilesUpload" />
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="row">
            <label class="approval-label">결재라인</label>
          </div>

          <div class="goout">
            <span class="goout1">결재자1:</span>
            <span class="goout1-name">{{ updateInfo2.confirmer1Name }}</span>
            <button class="modalButton1" @click="showModalForConfirmer1">
              결재자1 선택
            </button>
          </div>

          <div class="goout">
            <span class="goout2">결재자2:</span>
            <span class="goout2-name">{{ updateInfo2.confirmer2Name }}</span>
            <button class="modalButton2" @click="showModalForConfirmer2">
              결재자2 선택
            </button>
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
        <div class="row">
          <div class="button">
            <button @click="updateGoout" type="submit" class="btn-submit">
              제출
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import DepartmentListModal from "../DepartmentListModal.vue";

export default {
  components: {
    DepartmentListModal,
  },
  data() {
    return {
      backend: "http://192.168.0.51/api",
      updateInfo: {
        gooutTypeId: "",
        employeeId: "",
        agentId: "",
        first: "",
        last: "",
      },
      gooutTypes: [],
      employees: [],
      isModalVisibleForConfirmer1: false,
      isModalVisibleForConfirmer2: false,
      departments: [],
      updateInfo2: {
        confirmer1Id: "",
        confirmer2Id: "",
        confirmer1Name: "",
        confirmer2Name: "",
      },
      files: [],
    };
  },

  async created() {
    await this.fetchGooutTypes();
    await this.fetchEmployees();
    await this.loadUpdateInfo();
    await this.loadUpdateInfo2();
    this.fetchDepartments();
  },

  watch: {
    // watch 속성 추가
    gooutTypes: {
      immediate: true,
      handler() {
        if (this.gooutTypes.length > 0 && this.employees.length > 0) {
          this.loadUpdateInfo();
        }
      },
    },
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
      this.updateInfo2.confirmer1Id = id;
      this.updateInfo2.confirmer1Name = name;
      this.closeModalForConfirmer1();
    },
    handleEmployeeSelectionForConfirmer2({ id, name }) {
      this.updateInfo2.confirmer2Id = id;
      this.updateInfo2.confirmer2Name = name;
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
      const storedInfo = localStorage.getItem("updateGooutInfo");
      if (storedInfo) {
        const info = JSON.parse(storedInfo);
        this.updateInfo = info; // Directly bind the loaded data to updateInfo
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
      }
    },

    loadUpdateInfo2() {
      const storedInfo = localStorage.getItem("updateGooutLineInfo");
      if (storedInfo) {
        const info = JSON.parse(storedInfo);
        this.updateInfo2 = info;
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
      }
    },

    async fetchGooutTypes() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(`${this.backend}/gooutType/list`, {
          headers: {
            Authorization: "Bearer " + token,
          },
        });
        this.gooutTypes = response.data.result;
      } catch (error) {
        console.error("휴가 유형 목록 로딩 실패:", error);
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

    async updateGoout() {
      if (this.updateInfo.employeeId === this.updateInfo.agentId) {
        alert(
          "결재 수정 실패: 신청직원의 ID와 대리자의 ID는 같을 수 없습니다."
        );
        return;
      }
      if (this.updateInfo2.confirmer1Id === this.updateInfo2.confirmer2Id) {
        alert(
          "결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다."
        );
        return;
      }

      const token = sessionStorage.getItem("token");
      let formData = new FormData();
      formData.append(
        "gooutCreateReq",
        new Blob(
          [
            JSON.stringify({
              gooutTypeId: Number(this.updateInfo.gooutTypeId),
              first: this.updateInfo.first,
              last: this.updateInfo.last,
              employeeId: Number(this.updateInfo.employeeId),
              agentId: Number(this.updateInfo.agentId),
              confirmer1Id: Number(this.updateInfo2.confirmer1Id),
              confirmer2Id: Number(this.updateInfo2.confirmer2Id),
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
          `${this.backend}/goout/create`,
          formData,
          config
        );
        console.log(response);

        const gooutId = response.data.result;
        console.log("Created goout ID:", gooutId);

        alert("휴가가 재등록되었습니다.");
        this.$router.push(`/goout/list`);
      } catch (error) {
        console.error("휴가 등록 실패:", error);
        if (error.response) {
          alert("휴가 등록 실패: " + error.response.data.message);
        } else {
          alert("휴가 등록 실패: 서버에서 응답이 없습니다.");
        }
      }
    },

    async reCreateGooutLine1(gooutId) {
      try {
        const gooutLineReq = {
          confirmerId: Number(this.updateInfo2.confirmer1Id),
          gooutId: gooutId,
        };
        const response = await axios.post(
          `${this.backend}/gooutLine/create`,
          gooutLineReq,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        console.log("GooutLine1 생성 성공:", response);
      } catch (error) {
        console.error("결재라인 생성 실패:", error);
        alert("결재라인1 생성 실패: " + error.response.data.message);
      }
    },

    async reCreateGooutLine2(gooutId) {
      try {
        const gooutLineReq = {
          confirmerId: Number(this.updateInfo2.confirmer2Id),
          gooutId: gooutId,
        };
        const response = await axios.post(
          `${this.backend}/gooutLine/create`,
          gooutLineReq,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        console.log("GooutLine2 생성 성공:", response);
        alert("휴가 등록 및 결재라인1, 2 생성 완료");
        this.$router.push("/goout/list");
      } catch (error) {
        console.error("결재라인 생성 실패:", error);
        alert("결재라인 생성 실패: " + error.response.data.message);
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1254px;
  margin: 0 auto;
  margin-top: 15px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  position: relative;
  left: 4px;
  top: 14px;
}
.input-and-remaining-days {
  display: flex;
  align-items: center;
  flex: 1;
}
.select-reason {
  width: 50%;
  /* 근태 사유 선택 창의 크기 조절 */
  margin-right: 10px;
  /* 남은 휴가 일수와의 간격 조절 */
}
.remaining-days {
  margin-left: auto;
  /* 남은 휴가 일수를 오른쪽 정렬 */
}
.header h1 {
  font-size: 24px;
  margin-bottom: 32px;
  color: black;
  font-weight: bold;
  /* Added */
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
  font-weight: bold;
  /* Added */
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
  top: 28px;
}
.btn-submit:hover {
  background-color: #f7941e;
}
label {
  display: inline-block;
  font-weight: bold;
}
.modalButton1,
.modalButton2 {
  font-size: 13px;
  padding: 5px 10px;
  cursor: pointer;
  background-color: #111111;
  color: white;
  border: none;
  border-radius: 5px;
  margin-left: 100px;
}
.goout {
  position: relative;
}
.goout1-name,
.goout2-name {
  margin-left: 10px;
  min-width: 100px; /* 최소 너비 설정 */
  display: inline-block; /* 블록 요소처럼 너비와 높이 적용 */
  height: 30px;
}
</style>
