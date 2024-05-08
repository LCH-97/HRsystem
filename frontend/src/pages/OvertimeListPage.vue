<template>
  <div>
    <HeaderComponent />
    <SideBar />
<div class="container with-shadow">
      <h2>초과근무 목록</h2>
      <div class="overtimeList">
        <table>
          <thead>
            <tr>
              <th>No.</th>
              <th>일자</th>
              <th>새벽/야간</th>
              <th>시작 시간</th>
              <th>종료 시간</th>
              <th>사유</th>
              <th>승인여부</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="overtime in paginatedOvertimeList" :key="overtime.id" @click="viewOvertimeDetail(overtime.id)">
              <td>{{ overtime.id }}</td>
              <td>{{ overtime.date }}</td>
              <td>{{ overtime.shift }}</td>
              <td>{{ overtime.startTime }}</td>
              <td>{{ overtime.endTime }}</td>
              <td>{{ overtime.reason }}</td>
              <td>{{ overtime.status }}</td>
            </tr>
          </tbody>
        </table>
        <div class="pagination">
          <button @click="prevPage">이전</button>
          <button v-for="page in pageGroup"
          :key="page"
          :class="{ active: page === currentPage}"
          @click="changePage(page)"> {{ currentPage }}</button>
          <button @click="nextPage">다음</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import HeaderComponent from '../components/HeaderComponent.vue';
export default {
  name: 'OvertimeListPage',
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      overtimeList: [],
      itemsPerPage: 6,
      currentPage: 1,
    };
  },
  computed: {
    paginatedOvertimeList() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.overtimeList.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.overtimeList.length / this.itemsPerPage);
    },
  },
  mounted() {
    this.fetchOvertimeList();
  },
  methods: {
    async fetchOvertimeList() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get("http://www.lch-hr-api.kro.kr/employee/overtime/list", {
          headers: { Authorization: "Bearer " + token, },
        });
        this.overtimeList = response.data.result;
      } catch (error) {
        console.error("Error:", error);
      }
    },
    viewOvertimeDetail(id) {
      window.location.href = `/overtime/read/${id}`;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
  },
};
</script>
<style scoped>
.container {
  margin-top: 50px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  left: 463px;
  height: auto;
  margin-left: -50px;
  width: 60%;
  top: 50px;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
.overtimeList table{
  width: 100%;
  border-collapse: collapse;
  margin-left: 10px;
  margin-top: 70px;
}
.overtimeList th,
.overtimeList td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* 페이지네이션과 위의 내용 사이에 공간 추가 */
}
button {
  font-size: 13px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: -5px 0px 15px 10px;
}

button:hover {
  background-color: #f75c29;
}
</style>