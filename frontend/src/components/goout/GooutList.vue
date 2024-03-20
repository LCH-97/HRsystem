<template>
  <div>
    <div class="button-container">
      <button @click="filterGoouts(null)">전체 보기</button>
      <button @click="filterGoouts(0)">대기중</button>
      <button @click="filterGoouts(1)">결재자1 승인</button>
      <button @click="filterGoouts(2)">최종 승인</button>
      <button @click="filterGoouts(3)">반려</button>
    </div>
    <div class="gooutList">
      <table>
        <thead>
          <tr>
            <th>이름</th>
            <th>휴가 유형</th>
            <th>상태</th>
            <th>시작 날짜</th>
            <th>종료 날짜</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="goout in filteredGoouts" :key="goout.id" @click="goToGooutReadPage(goout.id)" class="gooutItem">
            <td>{{ goout.name }}</td>
            <td>{{ goout.gooutTypeName }}</td>
            <td>{{ getStatusText(goout.status) }}</td>
            <td>{{ goout.first }}</td>
            <td>{{ goout.last }}</td>
          </tr>
        </tbody>
      </table>
  </div>
   <div class="button-container2">
      <button @click="goToGooutCreate">휴가 등록</button>
    </div>
</div>
</template>
<script>
import axios from 'axios';
export default {
  name: 'VacationPage',
  data() {
    return {
      goouts: [],
      filteredGoouts: [], // 필터링된 휴가 목록
    }
  },
  created() {
    this.fetchGoouts();
  },
  methods: {
    getStatusText(status) {
      const statusMap = {
        0: '대기중',
        1: '결재자1 승인',
        2: '최종 승인',
        3: '반려',
        // 필요한 다른 상태들...
      };
      return statusMap[status] || '알 수 없음';
    },
    goToGooutCreate() {
      this.$router.push("/goout/create");
    },
    fetchGoouts() {
      // 여기서 백엔드 API를 호출하여 휴가타입 목록을 가져옵니다.
      axios.get('http://localhost:8080/goout/check')
        .then(response => {
          this.goouts = response.data.result;
          this.filteredGoouts = this.goouts; // fetchGoouts 호출 후 초기 필터링 상태로 설정
        })
        .catch(error => {
          console.error("휴가 목록 가져오기 실패:", error);
        });
    },
    goToGooutReadPage(id) {
      if (id) {
        this.$router.push(`/goout/read/${id}`);
      } else {
        console.error("ID is undefined");
      }
    },
    filterGoouts(status) {
      if (status === null) {
        this.filteredGoouts = this.goouts;
      } else {
        this.filteredGoouts = this.goouts.filter(goout => goout.status === status);
      }
    }
  }
};
</script>
<style scoped>
.button-container {
  text-align: left;
  padding-right: 40px;
  background-color: #F7F8FA;
  margin-bottom: 20px;
}
.button-container2 {
  text-align: right;
  padding-right: 40px;
  background-color: #F7F8FA;
  margin-bottom: 20px;
}
.button-container button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  background-color: #F75C29;
  margin-left: 10px; /* 버튼 간격 조정 */
}
.button-container2 button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  background-color: #F75C29;
  margin-left: 10px; /* 버튼 간격 조정 */
}
.button-container button:hover {
  color: #555555;
}
.gooutList ul {
  list-style: none;
  padding: 0;
}
.gooutItem {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}
.gooutItem:hover {
  color: #007BFF; /* 마우스를 올렸을 때 색상 변경 */
}

.gooutList table {
  width: 100%;
  border-collapse: collapse;
}
.gooutList th, .gooutList td {
  border: 1px solid #ddd;
  padding: 8px;
}
.gooutItem:hover {
  background-color: #f5f5f5;
}
</style>