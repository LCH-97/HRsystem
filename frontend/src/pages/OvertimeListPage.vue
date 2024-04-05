<template>
  <div>
    <HeaderComponent />
    <SideBar />
<br>
<br>
<br>
<div class="container with-shadow">
      <h1>초과근무 목록</h1>
      <div class="datatable-container">
        <table id="datatablesSimple" class="datatable-table">
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
          <button @click="prevPage">&lt;</button>
          <span>{{ currentPage }}</span>
          <button @click="nextPage">&gt;</button>
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
        const response = await axios.get("http://192.168.0.51/api/employee/overtime/list", {
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
  max-width: 1254px;
  margin: 0 auto;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  left: 113px;
  height: 436px;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
h1 {
  font-size: 24px;
  margin-bottom: 32px;
  margin-left: 13px;
}
.pagination {
  display: flex;
  justify-content: center; /* center buttons horizontally */
  align-items: center; /* center buttons vertically */
  margin-top: 20px;
  text-align: center;
}
.pagination button {
  cursor: pointer;
  padding: 8px 12px;
  border: none;
  background-color: #F0F0F0;
  border-radius: 5px;
  margin: 0 5px;
}
.pagination button:hover {
  background-color: #E0E0E0;
}
.pagination button:disabled {
  cursor: not-allowed;
  background-color: #ddd;
}
</style>