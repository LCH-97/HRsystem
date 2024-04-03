<template>
  <div class="all">
    <div class="goout-type-read-page">
      <h1>휴가타입 세부 정보</h1>
      <br><br><br>
      <div v-if="gooutType">
        <h2>{{ gooutType.name }}</h2>
        <p>설명: {{ gooutType.detail }}</p>
        <p>연간 최대 사용 횟수: {{ gooutType.maxHoliday }}</p>
      </div>
      <div v-else>
        <p>정보를 불러오는 중...</p>
      </div>
    </div>
    <br><br>
    <div class="gooutType-button">
      <button @click="updateGooutType">수정</button> 
      <button @click="deleteGooutType">삭제</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      gooutType: null,
      id: this.$route.params.id,
    };
  },
  methods: {
    fetchGooutType() {
      axios.get(`http://localhost:8080/gooutType/${this.id}`)
        .then(response => {
          if (response.data.isSuccess) {
            this.gooutType = response.data.result;
          } else {
            alert("휴가 정보를 불러오는데 실패했습니다.");
          }
            })
            .catch(error => {
              console.error("휴가 정보를 불러오는 중 오류가 발생했습니다.", error);
            });
          },
          updateGooutType() {
            // localStorage에 수정할 gooutType 정보와 id 저장
            localStorage.setItem('updateGooutTypeInfo', JSON.stringify({ ...this.gooutType, id: this.id }));
            // 수정 페이지로 이동
            this.$router.push('/gooutType/update');
          },

    deleteGooutType() {
      if (confirm("정말로 이 휴가타입을 삭제하시겠습니까?")) {
        axios.delete(`http://localhost:8080/gooutType/delete/${this.id}`)
          .then(() => {
            alert("휴가가 성공적으로 삭제되었습니다.");
            this.$router.push("/gooutType/list"); // 휴가타입 목록 페이지로 리디렉션
          })
          .catch(error => {
            console.error("휴가 삭제 중 오류 발생:", error);
            alert("휴가 삭제 중 오류가 발생했습니다.");
          });
      }
    }
  },
  created() {
    this.fetchGooutType();
  }
};
</script>

<style scoped>
.all {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.goout-type-read-page {
  max-width: 600px;
  padding: 20px;
  text-align: center;
}

.goout-type-button {
  display: flex;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
button:first-of-type {
  margin-right: 10px;
}
</style>
