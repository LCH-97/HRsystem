<template>
  <div>
    <div class="gooutUpdateBanner">
      <div class="gooutUpdateBannerTxt">
        <h2>휴가 정보 수정하기</h2>
      </div>
    </div>
    <div class="ReqBox">
      <article class="ReqInputBox">
        <div class="ReqInput">
          <p>휴가 유형</p>
          <select v-model="updateInfo.gooutTypeId">
            <option v-for="gooutType in gooutTypes" :key="gooutType.id" :value="gooutType.id">{{ gooutType.name }}</option>
          </select><br>
          <p>신청직원</p>
          <select v-model="updateInfo.employeeId">
            <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
          </select><br>
          <p>대리인</p>
          <select v-model="updateInfo.agentId">
            <option v-for="agent in employees" :key="agent.id" :value="agent.id">{{ agent.name }}</option>
          </select><br>
          <p>시작 날짜</p>
          <input type="date" v-model="updateInfo.first"><br>
          <p>종료 날짜</p>
          <input type="date" v-model="updateInfo.last"><br>
          <p>결재자1</p>
          <select v-model="updateInfo.confirmer1Id">
            <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
          </select><br>
          <p>결재자2</p>
          <select v-model="updateInfo.confirmer2Id">
            <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
          </select><br>
        </div>
      </article>
      <div class="button-container">
        <button @click="updateGoout">수정</button><br><br>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      backend: "http://localhost:8080",
      updateInfo: {
        gooutTypeId: "",
        agentId: "",
        employeeId: "",
        first: "",
        last: "",
        confirmer1Id: "",
        confirmer2Id: "",
      },
      gooutTypes: [],
      employees: [],
      gooutId: this.$route.params.id, // Assuming you're passing the goout ID as a route param
    };
  },

  methods: {
    async created() {
  await this.loadInitialData();
  await this.fetchGooutTypes();
  await this.fetchEmployees();
  },
  async loadInitialData() {
      const gooutId = this.$route.params.id;
      try {
        // 휴가 정보와 결재라인 정보를 불러오는 로직
        const [gooutResponse, gooutLineResponse] = await Promise.all([
          axios.get(`${this.backend}/goout/${gooutId}`),
          axios.get(`${this.backend}/gooutLine/2/${gooutId}`)
        ]);

        if (gooutResponse.data && gooutResponse.data.result) {
          // 휴가 정보를 updateInfo에 할당
        }

        if (gooutLineResponse.data && gooutLineResponse.data.result) {
          // 결재라인 정보를 updateInfo에 할당
        }
      } catch (error) {
        console.error("데이터 로딩 실패:", error);
        alert("데이터 로딩 중 오류가 발생했습니다.");
      }
    },



    async fetchGooutTypes() {
      try {
        const response = await axios.get(`${this.backend}/gooutType/list`);
        this.gooutTypes = response.data.result;
      } catch (error) {
        console.error("휴가 유형 목록 로딩 실패:", error);
      }
    },

    async fetchEmployees() {
      try {
        const response = await axios.get(`${this.backend}/employee/list`);
        this.employees = response.data.result;
      } catch (error) {
        console.error("직원 목록 로딩 실패:", error);
      }
    },

    async updateGoout() {
  try {
    // Goout 정보 업데이트 실행
    await axios.patch(`${this.backend}/goout/update`, this.updateInfo);
    console.log("Goout 정보 수정 성공");

    // GooutLine 정보 업데이트 준비
    const gooutLineUpdateInfo = {
      gooutId: this.gooutId,
      confirmer1Id: this.updateInfo.confirmer1Id,
      confirmer2Id: this.updateInfo.confirmer2Id,
      employeeId: this.updateInfo.employeeId,
    };

    // GooutLine 정보 업데이트 실행
    await axios.patch(`${this.backend}/gooutLine/update`, gooutLineUpdateInfo);
    console.log("GooutLine 정보 수정 성공");

    alert("휴가 정보 및 결재라인 정보가 성공적으로 수정되었습니다.");
    this.$router.push("/goout/list");
  } catch (error) {
    console.error("휴가 정보 또는 결재라인 정보 수정 실패:", error);
    alert("휴가 정보 또는 결재라인 정보 수정에 실패하였습니다.");
  }
}
  }
};
</script>
  
  <style scoped>
 .gooutUpdateBanner {
  margin: 0 auto; /* 수평 중앙 정렬 */
  text-align: center; /* 텍스트 중앙 정렬 */
  padding-top: 15px;
  padding-bottom: 20px;
  background-color: #F7F8FA;
}

.gooutUpdateBannerTxt {
  font-size: 40px;
  font-weight: 600;
  color: rgb(85, 85, 85);
}

.ReqBox {
  margin: 0 auto;
  text-align: center;
  background-color: #F7F8FA;
  border: 1px solid #F7F8FA;
  border-radius: 12px;
}

.ReqInput {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  flex-basis: 0px;
  font-size: 20px;
  font-weight: bold;
  color: rgb(85, 85, 85);
  margin-top: 28px;
  margin-bottom: 28px;
  text-align: center;
  width: 30%;
}

.ReqInputBox {
  margin: 0 auto;
  text-align: center;
  width: 30%;
  display: flex;
  justify-content: center;
}

.button-container button {
  font-size: 18px;
  font-weight: bold;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  background-color: #fae14a;
}

.button-container button:hover {
  color: #555555;
}
  </style>
  