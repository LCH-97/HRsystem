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
      <ul>
        <li v-for="goout in filteredGoouts" :key="goout.id" @click="goToGooutReadPage(goout.id)" class="gooutItem">
          <div><strong>이름:</strong> {{ goout.name }}</div>
          <div><strong>휴가 유형:</strong> {{ goout.gooutTypeName }}</div>
          <div><strong>상태:</strong> {{ getStatusText(goout.status) }}</div>
          <div><strong>시작 날짜:</strong> {{ goout.first }}</div>
          <div><strong>종료 날짜:</strong> {{ goout.last }}</div>
        </li>
      </ul>
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
      '0': '대기중',
      '1': '결재자1 승인',
      '2': '최종 승인',
      '3': '반려',
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
  },
  mounted() {
    // fetchGoouts 호출 후 초기 필터링 상태로 설정
    this.filteredGoouts = this.goouts;
  }
};
</script>

<style>
.button-container {
  text-align: right;
  padding-right: 40px;
  background-color: #F7F8FA;
}
.button-container button {
  font-size: 18px;
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
</style>