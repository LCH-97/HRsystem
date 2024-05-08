<template>
  <HeaderComponent />
  <SideBar />
  <div class="container with-shadow">
    <div class="container1">
      <h1 class="title">초과 근무 상세 정보</h1>
      <table class="table">
        <tr>
          <th>신청자</th>
          <td>{{ overtime?.employeeName }}</td>
        </tr>
        <tr>
          <th>날짜</th>
          <td>{{ overtime?.date }}</td>
        </tr>
        <tr>
          <th>근무 교대</th>
          <td>{{ overtime?.shift }}</td>
        </tr>
        <tr>
          <th>시작 시간</th>
          <td>{{ overtime?.startTime }}</td>
        </tr>
        <tr>
          <th>종료 시간</th>
          <td>{{ overtime?.endTime }}</td>
        </tr>
        <tr>
          <th>사유</th>
          <td>{{ overtime?.reason }}</td>
        </tr>
        <tr>
          <th>상태</th>
          <td>{{ overtime?.status }}</td>
        </tr>
      </table>
    </div>
  </div>
  <footer><br/><br/><br/><br/><br/></footer>
</template>

<script>
import axios from 'axios';
import jwtDecode from "jwt-decode";
import HeaderComponent from '@/components/HeaderComponent.vue';
import SideBar from '@/components/SideBar.vue';

export default {
  name: "OvertimeDetailPage",
  components: {
    HeaderComponent,
    SideBar,
  },
  data() {
    return {
      overtime: null,
      id: this.$route.params.id,
      backend: "http://www.lch-hr-api.kro.kr", // 백엔드 서버 주소\
      loggedInUserId: null, // 현재 로그인한 사용자의 ID
    };
  },
  async created() {
    this.setLoggedInUser();
    await this.fetchOvertime();
  },
  methods: {
    setLoggedInUser() {
      const token = sessionStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        this.loggedInUserId = decoded.ID; // 토큰 디코딩 방식에 따라 적절하게 조정
      }
    },
    async fetchOvertime() {
      const token = sessionStorage.getItem("token");
      try {
        const response = await axios.get(
          `${this.backend}/employee/overtime/read/${this.id}`, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
          },
        });
        this.overtime = response.data;
      } catch (error) {
        console.error("초과 근무 정보를 불러오는 중 오류가 발생했습니다", error);
        alert("초과 근무 정보를 불러오는데 실패했습니다.");
      }
    },
  },
};
</script>

<style scoped>
.container {
  margin-top: 65px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  height: auto;
  width: 810px;
  margin-left: auto; /* 좌우 마진을 자동으로 설정 */
  margin-right: auto; /* 좌우 마진을 자동으로 설정 */
  left: 110px;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
.container1 {
  width: 770px;
  margin: 0 auto;
}
.title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
.table {
  width: 770px;
  border-collapse: collapse;
  margin: 0 auto;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  word-wrap: break-word;
  text-align: center;
}
th {
  text-align: center;
}
</style>
