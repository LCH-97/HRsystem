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
.gooutTypeCreateBanner{
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #F7F8FA;
}

.gooutTypeCreateBannerTxt{
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
