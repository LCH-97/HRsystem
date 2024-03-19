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

      if (this.employeeId === this.agentId) {
    alert("결재 수정 실패: 신청직원의 ID와 대리자의 ID는 같을 수 없습니다.");
    return; // 메소드 실행을 중단
  }
      if (this.confirmer1Id === this.confirmer2Id) {
    alert("결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
    return; // 메소드 실행을 중단
  }


  try {
    // 데이터 객체 동적 생성
    let updateData = { id: this.updateInfo.id };
    if (this.gooutTypeId) updateData.gooutTypeId = this.gooutTypeId;
    if (this.first) updateData.first = this.first;
    if (this.last) updateData.last = this.last;
    if (this.employeeId) updateData.employeeId = this.employeeId;
    if (this.agentId) updateData.agentId = this.agentId;

    // Goout 정보 업데이트 실행
    await axios.patch(`${this.backend}/goout/update`, updateData, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    console.log("Goout 정보 수정 성공");

    // GooutLine 업데이트를 위한 동적 데이터 객체 생성
    let gooutLineUpdateReq = { gooutId: this.updateInfo.id };
    if (this.confirmer1Id) gooutLineUpdateReq.confirmer1Id = this.confirmer1Id;
    if (this.confirmer2Id) gooutLineUpdateReq.confirmer2Id = this.confirmer2Id;
    gooutLineUpdateReq.employeeId = this.loggedInUserId;

    // GooutLine 정보 업데이트 실행
    await axios.patch(`${this.backend}/gooutLine/update`, gooutLineUpdateReq, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    console.log("GooutLine 정보 수정 성공");

    alert("휴가 정보 및 결재라인 정보가 성공적으로 수정되었습니다.");
    this.$router.push("/goout/list"); // 수정 완료 후, 리스트 페이지로 리다이렉션
  } catch (error) {
    console.error("휴가 정보 또는 결재라인 정보 수정 실패:", error);
    alert("휴가 정보 또는 결재라인 정보 수정에 실패하였습니다.");
  }

}
  }
};
</script>
  
  
  <style scoped>
  </style>