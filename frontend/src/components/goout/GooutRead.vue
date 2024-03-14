<template>
  <div class="all">
    <div class="goout-read-page">
      <h1>휴가 세부 정보</h1>
      <br><br><br>
      <div v-if="goout && gooutLine">
        <h2>{{ goout.gooutTypeName }}</h2>
        <p>대리인: {{ goout.agentName }}</p>
        <p>신청직원: {{ goout.employeeName }}</p>
        <p>시작 날짜: {{ goout.first }}</p>
        <p>종료 날짜: {{ goout.last }}</p>
        <p>휴가 사용일 수: {{ daysUsed }}일</p>
        <br>
        <p>결재자1 : {{ gooutLine?.confirmer1Name }}</p>
        <p>결재자2 : {{ gooutLine?.confirmer2Name }}</p>
        <p>상태: {{ getStatusText(goout.status) }}</p>
      </div>
      <div v-else>
        <p>정보를 불러오는 중...</p>
      </div>
    </div>
    <br><br>
    <div class="goout-button">
      <button @click="updateGoout">수정</button>
      <button @click="deleteGoout">삭제</button>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      goout: null,
      gooutLine: null,
      id: this.$route.params.id,
    };
  },
  methods: {
    async fetchGoout() {
    try {
      const gooutResponse = await axios.get(`http://localhost:8080/goout/check/${this.id}`);
      if (gooutResponse.data.isSuccess) {
        this.goout = gooutResponse.data.result;
        await this.fetchGooutLine(this.id); // 결재라인 정보 조회
      } else {
        alert("휴가 정보를 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error("휴가 정보를 불러오는 중 오류가 발생했습니다.", error);
    }
  },
    async fetchGooutLine(gooutId) {
    try {
      const response = await axios.get(`http://localhost:8080/gooutLine/2/${gooutId}`);
      if (response.data.isSuccess) {
        // Assuming response.data.result directly contains the GooutLineRead object
        this.gooutLine = response.data.result;
      } else {
        console.error("결재라인 정보를 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error("결재라인 정보를 불러오는 중 오류가 발생했습니다.", error);
    }
  },
    getStatusText(status) {
      const statusMap = {
        '0': '대기중',
        '1': '결재자1 승인',
        '2': '최종 승인',
        '3': '반려',
      };
      return statusMap[status] || '알 수 없음';
    },
          updateGoout() {
            // localStorage에 수정할 goout 정보와 id 저장
            localStorage.setItem('updateGooutInfo', JSON.stringify({ ...this.goout, id: this.id }));
            // 수정 페이지로 이동
            this.$router.push('/goout/update');
          },

      async deleteGoout() {
      if (confirm("정말로 이 휴가를 삭제하시겠습니까?")) {
        try {
          // First, delete the approval line associated with this vacation request
          await axios.delete(`http://localhost:8080/gooutLine/delete/${this.id}`);
          console.log("결재라인이 성공적으로 삭제되었습니다.");

          // After the approval line is successfully deleted, delete the vacation request
          await axios.delete(`http://localhost:8080/goout/delete/${this.id}`);
          alert("휴가가 성공적으로 삭제되었습니다.");
          this.$router.push("/goout/list"); // Redirect to the list of vacation requests
        } catch (error) {
          console.error("휴가 또는 결재라인 삭제 중 오류 발생:", error);
          alert("휴가 또는 결재라인 삭제 중 오류가 발생했습니다.");
        }
      }
    }
  },
  created() {
    this.fetchGoout();
  },

computed: {
    // 휴가 사용일 수를 계산하는 계산된 속성
    daysUsed() {
      if (this.goout && this.goout.first && this.goout.last) {
        const firstDate = new Date(this.goout.first);
        const lastDate = new Date(this.goout.last);
        const difference = lastDate - firstDate;
        const days = difference / (1000 * 3600 * 24);
        return days + 1; // 포함되는 첫날을 계산에 추가
      }
      return 0; // 또는 적절한 기본값
    }
  }
};
</script>

<style scoped>
.all {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.goout-read-page {
  max-width: 600px;
  padding: 20px;
  text-align: center;
}

.goout-button {
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
