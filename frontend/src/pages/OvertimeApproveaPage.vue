<template>
  <div>
    <HeaderComponent />
    <SideBar />

    <h1>초과 근무 목록</h1>
    <table>
      <thead>
        <tr>
          <th class="table-header">글 번호</th>
          <th class="table-header">날짜</th>
          <th class="table-header">근무 시간</th>
          <th class="table-header">시작 시간</th>
          <th class="table-header">종료 시간</th>
          <th class="table-header">사유</th>
          <th class="table-header">상태</th>
          <th class="table-header">승인</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="overtime in paginatedData" :key="overtime.id" @click="viewOvertimeDetail(overtime.id)">
          <td>{{ overtime.id }}</td>
          <td>{{ overtime.date }}</td>
          <td>{{ overtime.shift }}</td>
          <td>{{ overtime.startTime }}</td>
          <td>{{ overtime.endTime }}</td>
          <td>{{ overtime.reason }}</td>
          <td>{{ overtime.status }}</td>
          <td>
            <button v-if="overtime.status === '대기 중'" class="approval-button" @click.stop="approveOvertime(overtime.id)">
              승인
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <div class="pagination">
      <button @click="prevPage">이전</button>
      <button @click="nextPage">다음</button>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";

export default {
  name: "OvertimeApproveaPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      overtimeList: [],
      currentPage: 0,
      pageSize: 10,
      totalItems: 0,
    };
  },
  computed: {
    paginatedData() {
      const start = this.currentPage * this.pageSize;
      const end = start + this.pageSize;
      return this.overtimeList.filter(overtime => overtime.status !== '승인됨').slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.totalItems / this.pageSize);
    },
  },
  mounted() {
    this.fetchOvertimeList();
    console.log("Total pages:", this.totalPages); // 전체 페이지 수 출력
  },

  methods: {
    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages - 1) {
        this.currentPage++;
      }
    },
    async fetchOvertimeList() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(
          "http://192.168.0.51/api/employee/overtime/list/real",
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        );
        this.overtimeList = response.data.result || []; // API 응답이 비어 있을 경우 빈 배열 할당
        this.totalItems = this.overtimeList.length; // 총 아이템 수 업데이트
        console.log("Fetched data:", this.overtimeList); // 데이터 로드 확인
        console.log("Total items loaded:", this.totalItems); // 총 아이템 수 로그 출력
        console.log("Total pages:", this.totalPages); // 총 페이지 수 로그 출력
      } catch (error) {
        console.error("Error loading overtime list:", error);
      }
    },

    viewOvertimeDetail(id) {
      window.location.href = `http://192.168.0.51/overtime/read/${id}`;
    },
    async approveOvertime(id, event) {
      event.stopPropagation(); // 이벤트 버블링 방지
      try {
        console.log("1");
        const token = sessionStorage.getItem("token");
        const payload = {
          id: id,
        };
        console.log("2");
        await axios.patch(
          `http://192.168.0.51/api/employee/overtime/approve`,
          payload,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          }
        );
        console.log("2");
        alert("초과 근무를 승인 하였습니다.");
        this.fetchOvertimeList(); // 목록 다시 불러오기
      } catch (error) {
        console.error("Error:", error);
        alert("초과 근무 승인에 실패 하였습니다.");
      }
    },
  },
};
</script>

<style scoped>
h1 {
  font-size: 34px;
  margin-left: 267px;
  margin-top: 51px;
  font-family: "Roboto", sans-serif;
}

body {
  font-family: "Roboto", sans-serif;
  background-color: #fff;
  color: #000;
  margin: 20px;
}

table {
  width: 80%;
  border-collapse: collapse;
  margin-top: 47px;
  margin-left: 261px;
}

th,
td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
  background-color: #fff;
}

th {
  background-color: #fff;
  color: #333;
  border-bottom: 2px solid #333;
}

tr:last-child td {
  border-bottom: none;
}

.approval-button {
  padding: 5px 10px;
  background-color: #ef9d39;
  border: none;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
  margin: 2px 2px;
  cursor: pointer;
  border-radius: 4px;
}
.pagination button {
  background-color: #111111;
  color: white;
  padding: 10px;
  border: none;
  visibility: visible; /* 강제로 보이게 설정 */
  position: relative; /* 다른 요소에 의해 가려지는지 확인 */
  z-index: 1000; /* 다른 요소 위에 표시 */
  margin: 0 3px; /* 버튼 사이의 간격 */
}

.pagination {
  display: flex;
  justify-content: center; /* 가로축 가운데 정렬 */
  align-items: center; /* 세로축 가운데 정렬 */
  margin-top: 20px; /* 상단 여백 추가 */
  margin: 0 auto;
}
</style>
