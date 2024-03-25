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

          <div class="row">
           <div class="label">첨부파일</div>
           <div class="input">
            <input type="file" multiple @change="handleFilesUpload">
           </div>
          </div>

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
        writerId: "",
      },
      gooutTypes: [],
      employees: [],  
      loggedInUserId: null, // 로그인한 사용자 ID 저장
      updateInfo2: {
        confirmer1Id: "",
        confirmer2Id: "",
      },
      files: [] // 여러 파일을 저장할 배열
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

    handleFilesUpload(event) {
      this.files = event.target.files; // 선택된 파일들을 files 배열에 저장
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

      if (this.updateInfo.employeeId === this.updateInfo.agentId) {
        alert("결재 수정 실패: 신청직원의 ID와 대리자의 ID는 같을 수 없습니다.");
        return;
      }
      if (this.updateInfo2.confirmer1Id === this.updateInfo2.confirmer2Id) {
        alert("결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다.");
        return;
      }


        let formData  = new FormData();
  formData.append('gooutCreateReq', new Blob([JSON.stringify({
          gooutTypeId: Number(this.updateInfo.gooutTypeId),
          first: this.updateInfo.first,
          last: this.updateInfo.last,
          employeeId: Number(this.updateInfo.employeeId),
          agentId: Number(this.updateInfo.agentId),
          writerId:  Number(this.loggedInUserId),
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
        await this.reCreateGooutLine1(gooutId);
        await this.reCreateGooutLine2(gooutId);
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
      const response = await axios.post(`${this.backend}/gooutLine/create`, gooutLineReq, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
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
      const response = await axios.post(`${this.backend}/gooutLine/create`, gooutLineReq, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      
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