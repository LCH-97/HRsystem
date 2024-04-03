<template>
  <div>
    <div class="gooutTypeCreateBanner">
        <div class="gooutTypeCreateBannerTxt">
            <h2>휴가타입 등록하기</h2>
        </div>      
    </div>
    <div class="ReqBox">  
      <article class="ReqInputBox">
        <div class="ReqInput">
          <p>휴가타입 이름</p>
          <input type="text" v-model="name" placeholder="휴가타입 이름 입력" class="nameEx"><br>

          <p>휴가타입 설명</p>
          <input type="text" v-model="detail" placeholder="휴가타입 설명 입력" class="detailEx"><br>

          <p>연간 최대 사용 횟수</p>
          <input type="text" v-model="maxHoliday" placeholder="숫자만 적어주세요" class="maxHolidayEx"><br>
        </div>
      </article>
      <div class="button-container">
        <button @click="handleFormSubmission">휴가타입 등록</button><br><br>
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
          name: "",
          detail: "",
          maxHoliday: "",
      }
  },
  methods: {
    async getGooutTypeCreate(name, detail, maxHoliday) {
    const jsonData = {
        name: name,
        detail: detail,
        maxHoliday: maxHoliday
    };

    let response = await axios.post(`${this.backend}/gooutType/create`, jsonData, {
        headers: {
            'Content-Type': 'application/json'
        }
    });

    return response.data.result;
},
      async handleFormSubmission() {
          await this.getGooutTypeCreate(this.name, this.detail, this.maxHoliday);
          alert("등록 되었습니다");
          this.$router.push("/gooutType/list");
      }
  },
}
</script>

<style scoped>
.container {
  width: 50%;
  margin: 0 auto;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.header h1 {
  font-size: 24px;
  font-weight: bold;
}

.content {
  background-color: #F7F8FA;
  border: 1px solid #ddd;
  border-radius: 12px;
  padding: 20px;
}

.row {
  margin-bottom: 15px;
}

.label {
  font-size: 18px;
  font-weight: bold;
}

.input input {
  width: 100%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.button button {
  width: 100%;
  padding: 10px;
  font-size: 18px;
  font-weight: bold;
  color: white;
  background-color: #fae14a;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.button button:hover {
  background-color: #ffd700;
}

</style>
