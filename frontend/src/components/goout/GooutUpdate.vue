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
  <option v-for="gooutType in gooutTypes" :key="gooutType.id" :value="gooutType.id.toString()">{{ gooutType.name }}</option>
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
            <select v-model="updateInfo2.confirmer1Id">
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select><br>

            <p>결재자2</p>
            <select v-model="updateInfo2.confirmer2Id">
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
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      backend: "http://localhost:8080",
      updateInfo: {
        gooutTypeName: "",
        agentName: "",
        employeeName: "",
        first: "",
        last: "",
      },
      gooutTypes: [],
      employees: [],  
      loggedInUserId: null, // 로그인한 사용자 ID 저장
      updateInfo2: {
        confirmer1Id: "",
        confirmer2Id: "",
      }
    };
  },

  async created() {
    await this.fetchGooutTypes();
  await this.fetchEmployees();
  await this.loadUpdateInfo();
  await this.loadUpdateInfo2();
  },

  mounted() {
    this.setLoggedInUser();
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
    setLoggedInUser() {
      const token = sessionStorage.getItem('token'); // 로컬 스토리지 또는 적절한 저장소에서 토큰 가져오기
      if (token) {
        const decoded = jwtDecode(token);
        this.loggedInUserId = decoded.ID; // 실제 토큰 구조에 따라 변경될 수 있음
      }
    },


    loadUpdateInfo() {
      const storedInfo = localStorage.getItem('updateGooutInfo');
      if (storedInfo) {
        const info = JSON.parse(storedInfo);
        this.updateInfo = info; // Directly bind the loaded data to updateInfo
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
      }
    },

    loadUpdateInfo2() {
      const storedInfo = localStorage.getItem('updateGooutLineInfo');
      if (storedInfo) {
        const info = JSON.parse(storedInfo);
        this.updateInfo2 = info;
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
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
        this.employees = response.data; // 백엔드 응답에 'result' 키가 없다면 이렇게 접근합니다.
      } catch (error) {
        console.error("직원 목록 로딩 실패:", error);
      }
    },

        async updateGoout() {
      console.log("updateData:", this.updateInfo);
      console.log("updateInfo2:", this.updateInfo2);

      if (this.updateInfo.employeeId === this.updateInfo.agentId) {
        alert("결재 수정 실패: 신청직원의 ID와 대리자의 ID는 같을 수 없습니다.");
        return;
      }
      if (this.updateInfo2.confirmer1Id === this.updateInfo2.confirmer2Id) {
        alert("결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
        return;
      }

      try {
        let updateData = {
          id: Number(this.updateInfo.id),
          gooutTypeId: Number(this.updateInfo.gooutTypeId),
          first: this.updateInfo.first,
          last: this.updateInfo.last,
          employeeId: Number(this.updateInfo.employeeId),
          agentId: Number(this.updateInfo.agentId),
        };

        await axios.patch(`${this.backend}/goout/update`, updateData, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
        console.log("Goout 정보 수정 성공");

        let gooutLineUpdateReq = {
          gooutId: Number(this.updateInfo.id),
          confirmer1Id: Number(this.updateInfo2.confirmer1Id),
          confirmer2Id: Number(this.updateInfo2.confirmer2Id),
          employeeId: Number(this.loggedInUserId),
        };

        await axios.patch(`${this.backend}/gooutLine/update`, gooutLineUpdateReq, {
          headers: {
            'Content-Type': 'application/json',
          },
        });
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
    margin: 0 auto;
    text-align: center;
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #f7f8fa;
  }

  .gooutUpdateBannerTxt {
    font-size: 40px;
    font-weight: 600;
    color: rgb(85, 85, 85);
  }

  .ReqBox {
    text-align: center;
    background-color: #f7f8fa;
    border: 1px solid #f7f8fa;
    border-radius: 12px;
    padding: 20px;
  }

  .ReqInputBox {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 20px;
    margin-top: 20px;
  }

  .ReqInput {
    width: 300px;
  }

  .ReqInput p {
    font-size: 18px;
    font-weight: bold;
    margin-top: 10px;
    margin-bottom: 5px;
  }

  .ReqInput select,
  .ReqInput input[type="date"] {
    width: 100%;
    padding: 8px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  .button-container {
    margin-top: 20px;
  }

  .button-container button {
    font-size: 18px;
    font-weight: bold;
    padding: 10px 20px;
    color: white;
    background-color: #fae14a;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .button-container button:hover {
    background-color: #ffd700;
  }
  </style>