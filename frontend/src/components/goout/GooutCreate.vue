<template>
  <div>
    <div class="gooutCreateBanner">
        <div class="gooutCreateBannerTxt">
            <h2>휴가 등록하기</h2>
        </div>      
    </div>
    <div class="ReqBox">  
      <article class="ReqInputBox">
        <div class="ReqInput">
          <p>휴가 유형</p>
          <select v-model="gooutTypeId">
            <option v-for="gooutType in gooutTypes" :key="gooutType.id" :value="gooutType.id">{{ gooutType.name }}</option>
          </select><br> 
          <p>신청직원</p>
          <select v-model="employeeId">
            <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
          </select><br>
          <p>대리인</p>
          <select v-model="agentId">
            <option v-for="agent in employees" :key="agent.id" :value="agent.id">{{ agent.name }}</option>
          </select><br>
          
          <p>시작 날짜</p>
          <input type="date" v-model="first"><br>
          <p>종료 날짜</p>
          <input type="date" v-model="last"><br>

          <p>파일 첨부</p>
            <input type="file" @change="handleFilesUpload" multiple class="fileUploadEx"><br>


          <p>결재자1</p>
            <select v-model="confirmer1Id">
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select><br>

            <p>결재자2</p>
            <select v-model="confirmer2Id">
              <option v-for="employee in employees" :key="employee.id" :value="employee.id">{{ employee.name }}</option>
            </select><br>
        </div>
      </article>
      <div class="button-container">
        <button @click="handleFormSubmission">휴가 등록</button><br><br>
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
    }
  },
  async created() {
    await this.fetchGooutTypes();
    await this.fetchEmployees();
  },
  methods: {
    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
    },
    async fetchGooutTypes() {
      const response = await axios.get(`${this.backend}/gooutType/list`);
      this.gooutTypes = response.data.result;
    },
    async fetchEmployees() {
      const response = await axios.get(`${this.backend}/employee/list`);
      this.employees = response.data;
    },
    async getGooutCreate() {

      if (this.confirmer1Id === this.confirmer2Id) {
    alert("결재라인 생성 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
    return; // 메소드 실행을 중단
  }

  let formData = new FormData();
  formData.append('gooutCreateReq', new Blob([JSON.stringify({
    agentId: this.agentId,
    employeeId: this.employeeId,
    gooutTypeId: this.gooutTypeId,
    first: this.first,
    last: this.last,
  })], {type : 'application/json'}));

  // 여러 파일을 formData에 추가
  for (let i = 0; i < this.files.length; i++) {
  formData.append('uploadFiles', this.files[i]); // 'uploadFiles'로 변경
}

  try {
    const response = await axios.post(`${this.backend}/goout/create`, formData);
    console.log(response);
    const gooutId = response.data.result;
    console.log('Created goout ID:', gooutId);
    await this.createGooutLine(gooutId);
  } catch (error) {
    console.error("등록 실패:", error);
    alert("휴가 등록 실패: " + error.response.data.message); // 서버에서 반환한 오류 메시지를 사용자에게 보여줌
  }
},

async createGooutLine(gooutId) {
  try {
    const gooutLineReq = {
      confirmer1Id: this.confirmer1Id,
      confirmer2Id: this.confirmer2Id,
      gooutId: gooutId,
      employeeId: this.employeeId,  
    };
    const response = await axios.post(`${this.backend}/gooutLine/create`, gooutLineReq, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log("GooutLine 생성 성공:", response);
    alert("휴가 등록 및 결재라인 생성 완료");
    this.$router.push("/goout/list");
  } catch (error) {
    console.error("결재라인 생성 실패:", error);
    alert("결재라인 생성 실패: " + error.response.data.message);
  }
},

    async handleFormSubmission() {
        await this.getGooutCreate();
    }
  },
}
</script>

<style scoped>
.gooutCreateBanner{
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #F7F8FA;
}

.gooutCreateBannerTxt{
    font-size: 40px;
    font-weight: 600;
    color: rgb(85, 85, 85);
}

.ReqBox{
    margin: 0 auto;
    text-align: center;
    background-color: #F7F8FA;
    border: 1px solid  #F7F8FA;
    border-radius: 12px;
}

.ReqInput{
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
.nameEx, .detailEx, .maxHolidayEx{
    text-align: center;
}

.ReqInputBox{
    margin: 0 auto;
    text-align: center;
    width: 30%;
    display: flex;
    justify-content: center;
}

.button-container button{
  font-size:18px;
  font-weight: bold;
  font-weight:600;
  padding: 5px 10px;
  color:white;
  letter-spacing:0.2px;
  border: none;
  background-color: #fae14a;
}
.button-container button:hover {
  color: #555555;
}

</style>
