<template>
  <div>
    <div class="updateBanner">
      <div class="updateBannerTxt">
        <h2>휴가타입 수정하기</h2>
      </div>
    </div>
    <div class="ReqBox">
      <article class="ReqInputBox">
        <div class="ReqInput">
          <p>휴가타입 이름</p>
          <input type="text" v-model="gooutTypeInfo.name" placeholder="휴가타입 이름" class="inputEx"/><br />

          <p>휴가타입 설명</p>
          <input type="text" v-model="gooutTypeInfo.detail" placeholder="휴가타입 설명" class="inputEx"/><br />

          <p>연간 최대 사용 횟수</p>
          <input type="text" v-model="gooutTypeInfo.maxHoliday" placeholder="숫자만 적어주세요" class="inputEx"/><br /><br/>
        </div>
      </article>
      <div class="button-container">
        <button @click="updateGooutType">수정</button><br /><br />
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      gooutTypeInfo: {
        id: null,
        name: "",
        detail: "",
        maxHoliday: "",
      },
    };
  },
  methods: {
        updateGooutType() {
      axios.patch(`http://localhost:8080/gooutType/update`, this.gooutTypeInfo, {
        headers: {
          'Content-Type': 'application/json',
          Authorization: "Bearer " + token,
        }
      })
        .then(() => {
          alert("휴가타입이 성공적으로 수정되었습니다.");
          this.$router.push("/gooutType/read/"+this.gooutTypeInfo.id);
        })
        .catch((error) => {
          console.error("Error:", error);
          alert("휴가타입 수정에 실패하였습니다.");
        });
    },
  },
  mounted() {
    
    const storedInfo = localStorage.getItem('updateGooutTypeInfo');
    if (storedInfo) {
      this.gooutTypeInfo = JSON.parse(storedInfo);
    }
  }
};
</script>
  
  <style scoped>
  .updateBanner {
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #f7f8fa;
  }
  
  .updateBannerTxt {
    font-size: 40px;
    font-weight: 600;
    color: rgb(85, 85, 85);
  }
  
  .ReqBox {
    margin: 0 auto;
    text-align: center;
    background-color: #f7f8fa;
    border: 1px solid #f7f8fa;
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
  .nameEx,
  .detailEx,
  .maxHolidayEx {
    text-align: center;
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
  