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
          <select v-model="gooutTypeId">
            <option value="">선택하세요</option>
            <option v-for="gooutType in gooutTypes" :key="gooutType.id" :value="gooutType.id">{{ gooutType.name }}</option>
          </select><br> 
          <p>신청직원</p>
          <select v-model="employeeId">
            <option value="">선택하세요</option>
            <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
          </select><br>
          <p>대리인</p>
          <select v-model="agentId">
            <option value="">선택하세요</option>
            <option v-for="agent in employees" :key="agent.id" :value="agent.id">{{ agent.name }}</option>
          </select><br>
          
          <p>시작 날짜</p>
          <input type="date" v-model="first"><br>
          <p>종료 날짜</p>
          <input type="date" v-model="last"><br>

          <p>결재자1</p>
            <select v-model="confirmer1Id">
              <option value="">선택하세요</option>
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select><br>

            <p>결재자2</p>
            <select v-model="confirmer2Id">
              <option value="">선택하세요</option>
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
      
    };
  },

  async created() {
    await this.loadUpdateInfo();
    await this.fetchGooutTypes();
    await this.fetchEmployees();
  },

  methods: {

    loadUpdateInfo() {
      const storedInfo = localStorage.getItem('updateGooutInfo');
      if (storedInfo) {
        this.updateInfo = JSON.parse(storedInfo);
        // 불러온 정보를 바탕으로 각 필드의 초기값 설정
        this.gooutTypeId = this.updateInfo.gooutTypeId;
        this.agentId = this.updateInfo.agentId;
        this.employeeId = this.updateInfo.employeeId;
        this.first = this.updateInfo.first;
        this.last = this.updateInfo.last;
        this.confirmer1Id = this.updateInfo.confirmer1Id;
        this.confirmer2Id = this.updateInfo.confirmer2Id;
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
    if (this.employeeId) gooutLineUpdateReq.employeeId = this.employeeId;

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

  // try {
  //   // 사용자가 선택한 값이 있으면 해당 값을 사용하고, 그렇지 않으면 기존의 정보를 사용
  //   const updateData = {
  //     id: this.updateInfo.id,
  //     gooutTypeId: this.gooutTypeId || this.updateInfo.gooutTypeId,
  //     first: this.first || this.updateInfo.first,
  //     last: this.last || this.updateInfo.last,
  //     employeeId: this.employeeId || this.updateInfo.employeeId,
  //     agentId: this.agentId || this.updateInfo.agentId,
  //   };

  //   // Goout 정보 업데이트 실행
  //   await axios.patch(`${this.backend}/goout/update`, updateData, {
  //     headers: {
  //       'Content-Type': 'application/json',
  //     },
  //   });
  //   console.log("Goout 정보 수정 성공");

  //   const gooutLineUpdateReq = {
  //     gooutId: this.updateInfo.id, // 수정된 Goout의 ID
  //     confirmer1Id: this.confirmer1Id || this.updateInfo.confirmer1Id,
  //     confirmer2Id: this.confirmer2Id || this.updateInfo.confirmer2Id,
  //     employeeId: this.employeeId || this.updateInfo.employeeId, // 중복되므로 필요에 따라 제거 가능
  //   };

  //   // GooutLine 정보 업데이트 실행
  //   await axios.patch(`${this.backend}/gooutLine/update`, gooutLineUpdateReq, {
  //     headers: {
  //       'Content-Type': 'application/json',
  //     },
  //   });
  //   console.log("GooutLine 정보 수정 성공");

  //   alert("휴가 정보 및 결재라인 정보가 성공적으로 수정되었습니다.");
  //   this.$router.push("/goout/list"); // 수정 완료 후, 리스트 페이지로 리다이렉션
  // } catch (error) {
  //   console.error("휴가 정보 또는 결재라인 정보 수정 실패:", error);
  //   alert("휴가 정보 또는 결재라인 정보 수정에 실패하였습니다.");
  // }

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
  