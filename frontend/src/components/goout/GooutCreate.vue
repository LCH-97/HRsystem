<template>
  <div class="container">
    <div class="header">
      <h1>휴가 신청</h1>
    </div>
    <div class="content">
      <div @submit.prevent="handleFormSubmission">
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="employee">신청자</label>
              <div class="input">
                <select v-model="employeeId">
                  <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                    {{ employee.name }}
                  </option>
                </select><br />
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="agent">대리인</label>
              <div class="input">
                <select v-model="agentId">
                  <option v-for="agent in employees" :key="agent.id" :value="agent.id">
                    {{ agent.name }}
                  </option>
                </select><br />
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="reason">근태 사유</label>
              <div class="input-and-remaining-days">
                <select v-model="gooutTypeId" class="select-reason">
                  <option v-for="gooutType in gooutTypes" :key="gooutType.id" :value="gooutType.id">
                    {{ gooutType.name }}
                  </option>
                </select>
                <div v-if="remainingVacationDays !== null" class="remaining-days">
                  남은 휴가 일수: {{ remainingVacationDays }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="start-date">시작 날짜</label>
              <div class="input"><input type="date" v-model="first" /><br /></div>
            </div>
          </div>
          <div class="col-md-6">
            <label for="end-date">종료 날짜</label>
            <div class="input"><input type="date" v-model="last" /><br /></div>
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
          <label class="approval-label">결재라인</label>
        </div>
        <div class="goout">
            <span class="goout1">결재자1:</span>
             <span class="goout1-name"> {{ confirmer1Name }} </span>
            <button class="modalButton1" @click="showModalForConfirmer1">결재자1 선택</button>
        </div>
        <div class="goout">
            <span class="goout2">결재자2:</span>
            <span class="goout2-name">{{ confirmer2Name }}</span>
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
        <!-- <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <div class="input">
                <p>결재자1</p>
                <select v-model="confirmer1Id">
                  <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                    {{ employee.name }}
                  </option>
                </select>
              </div>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <p>결재자2</p>
              <select v-model="confirmer2Id">
                <option v-for="employee in employees" :key="employee.id" :value="employee.id">
                  {{ employee.name }}
                </option>
              </select>
            </div>
          </div>
        </div> -->
        <div class="row">
          <div class="button">
            <button @click="createGooutRequest" type="submit" class="btn-submit">제출</button>
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
  components:{
    DepartmentListModal,
  },
  data() {
    return {
      backend: "http://192.168.0.51/api",
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
      confirmer1Name: "",
      confirmer2Name: "",
      loggedInUserId: null, // 로그인한 사용자 ID 저장
      sortColumn: null, // 분류할 열 (예: 'name' 또는 'startDate')
      sortOrder: "asc", // 분류 순서 (오름차순 또는 내림차순)
      remainingVacationDays: null, // 남은 휴가 일수
      isModalVisibleForConfirmer1: false,
      isModalVisibleForConfirmer2: false,
      departments: [],
    };
  },
  async created() {
    await this.fetchGooutTypes();
    await this.fetchEmployees();
    this.fetchDepartments();
  },
  watch: {
    // 근태 사유(gooutTypeId) 또는 신청자(employeeId)가 변경될 때마다 남은 휴가 일수 조회
    gooutTypeId: {
      handler() {
        this.fetchRemainingVacationDays();
      },
      immediate: true, // 컴포넌트가 마운트될 때 즉시 실행
    },
    employeeId: {
      handler() {
        this.fetchRemainingVacationDays();
      },
      immediate: true, // 컴포넌트가 마운트될 때 즉시 실행
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
    sortBy(column) {
      if (this.sortColumn === column) {
        this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
      } else {
        this.sortColumn = column;
        this.sortOrder = "asc";
      }
    },
    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchGooutTypes() {
      const token = sessionStorage.getItem("token");
      const response = await axios.get(`${this.backend}/gooutType/list`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });
      this.gooutTypes = response.data.result;
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
    async createGooutRequest() {
      if (this.confirmer1Id === this.confirmer2Id) {
        alert(
          "결재라인 생성 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다."
        );
        return; // 메소드 실행을 중단
      }
      const token = sessionStorage.getItem("token");
      let formData = new FormData();
      formData.append(
        "gooutCreateReq",
        new Blob(
          [
            JSON.stringify({
              agentId: this.agentId,
              employeeId: this.employeeId,
              gooutTypeId: this.gooutTypeId,
              first: this.first,
              last: this.last,
              confirmer1Id: this.confirmer1Id,
              confirmer2Id: this.confirmer2Id,
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
        alert("휴가가 등록되었습니다.");
        this.$router.push(`/goout/list`);
      } catch (error) {
        if (error.response && error.response.data) {
          // 서버로부터 받은 에러 메시지를 alert로 표시
          alert(`에러: ${error.response.data.message}`);
        } else {
          alert("휴가 등록 중 문제가 발생했습니다.");
        }
      }
    },
    async handleFormSubmission() {
      await this.createGooutRequest();
    },
    async fetchRemainingVacationDays() {
      const token = sessionStorage.getItem("token");
      if (this.gooutTypeId && this.employeeId) {
        try {
          const response = await axios.get(
            `${this.backend}/goout/remainingVacationDays`,
            {
              params: {
                employeeId: this.employeeId,
                gooutTypeId: this.gooutTypeId,
              },
              headers: {
                // 여기에서 Authorization 헤더에 토큰 추가
                Authorization: `Bearer ${token}`,
              }
            }
          );
          this.remainingVacationDays = response.data.result;
          if (this.remainingVacationDays < 0) {
            alert("최대 사용한도를 초과하였습니다. 관리자에게 문의하세요.");
          }
        } catch (error) {
          console.error("남은 휴가 일수를 가져오지 못했습니다.:", error);
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
  background-color: #F7941E;
}
label {
  display: inline-block;
  font-weight: bold;
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
  .goout {
    position: relative;
  }
  .goout1-name, .goout2-name {
    margin-left: 10px;
    min-width: 100px; /* 최소 너비 설정 */
    display: inline-block; /* 블록 요소처럼 너비와 높이 적용 */
    height: 30px;
  }
</style>