<template>
  <HeaderComponent />
  <SideBar />
  <div class="container with-shadow">
    <h2>결재 목록</h2>
    <div class="filter">
      <button @click="filterApprovalsByStatus(null)">전체</button>
      <button @click="filterApprovalsByStatus(0)">기안중 {{ statusCounts.대기중 }} </button>
      <button @click="filterApprovalsByStatus(1)">진행중 {{ statusCounts.결재자1승인 }} </button>
      <button @click="filterApprovalsByStatus(3)">반려 {{ statusCounts.반려 }} </button>
      <button @click="filterApprovalsByStatus(2)">결재 완료</button>
      <button @click="filterApprovalsByStatus(4)">회수</button>
    </div>
    <div>
      <a class="make-approve" href="/approve/">결재 생성 </a>
    </div>
    <div class="approveList">
      <table>
        <thead>
          <tr>
            <th style="text-align: center">순번</th>
            <th style="text-align: center">기안일자</th>
            <th style="text-align: center">제목</th>
            <th style="text-align: center">기안자</th>
            <th style="text-align: center">결재자1</th>
            <th style="text-align: center">결재자2</th>
            <th style="text-align: center">진행상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="approve in filteredApprovals" :key="approve.id" @click="goToApproveReadPage(approve.id)"
            class="approveItem">
            <td>{{ approve.id }}</td>
            <td>{{ approve.createAt }}</td>
            <td>{{ approve.title }}</td>
            <td>{{ approve.employeeName }}</td>
            <td>{{ approve.confirmer1Name }}</td>
            <td>{{ approve.confirmer2Name }}</td>
            <td>{{ getStatusText(approve.status) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="pagination">
        <button @click="prevGroup">이전</button>
        <button
          v-for="page in pageGroup"
          :key="page"
          :class="{ active: page === currentPage }"
          @click="changePage(page)"
        >
          {{ page }}
        </button>
        <button @click="nextGroup">다음</button>
      </div>
  </div>
</template>

<script>
import axios from "axios";
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";

export default {
  name: "ApproveListPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      approvals: [],
      approveLine: [],
      filteredApprovals: [],
      currentFilterStatus: null,
      confirmer1: "",
      confirmer2: "",
      currentPage: 1,
      pageSize: 10,
      pagesToShow: 5,
      pageGroupStart: 1,
      totalPages: 0,

    };
  },
  created() {
    this.fetchApprovals();
  },
  computed: {

    pageGroup() {
      let startPage =
        Math.floor((this.currentPage - 1) / this.pagesToShow) *
          this.pagesToShow +
        1;
      let pages = [];
      for (let i = 0; i < this.pagesToShow; i++) {
        let page = startPage + i;
        if (page > this.totalPages) break; 

        pages.push(page);
      }
      return pages;
    },


    statusCounts() {
      const counts = { total: 0, 대기중: 0, 결재자1승인: 0, 최종승인: 0, 반려: 0 };

      this.approvals.forEach((approve) => {
        counts.total += 1;
        const statusText = this.getStatusText(approve.status);
        if (counts[statusText] !== undefined) {
          counts[statusText] += 1;
        }
      });

      return counts;
    },
  },

  methods: {
    changePage(page) {
      this.currentPage = page;
      this.fetchApprovals();
    },

    prevGroup() {
      this.pageGroupStart = Math.max(1, this.pageGroupStart - this.pagesToShow);
      this.changePage(this.pageGroupStart);
    },
    nextGroup() {
      if (this.pageGroupStart + this.pagesToShow <= this.totalPages) {
        this.pageGroupStart += this.pagesToShow;
        this.changePage(this.pageGroupStart);
      }
    },
    filterApprovalsByStatus(status) {
      this.currentFilterStatus = status;
      if (status === null) {
        this.filteredApprovals = this.approvals;
      } else {
        this.filteredApprovals = this.approvals.filter(
          (approve) => approve.status === status
        );
      }
    },

    async fetchApprovals() {

      const api = `http://www.lch-hr-api.kro.kr/approve/list`;

      try {
        const token = sessionStorage.getItem("token");
        const payload = {
          page: this.currentPage - 1,
          size: this.pageSize
        };
        const response = await axios.post(api, payload, {
            headers: {
              Authorization: "Bearer " + token,
              "Content-Type": "application/json",
            }
        });
        if (response.data && response.data.result) {
          this.approvals = response.data.result.content;
          this.filteredApprovals = this.approvals;
          this.totalPages = response.data.result.totalPages;
          this.currentPage = response.data.result.number + 1;

          this.pageGroupStart = Math.floor((this.currentPage - 1) / this.pagesToShow) * this.pagesToShow + 1;
        }
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    },



    goToApproveReadPage(id) {
      if (id) {
        this.$router.push(`/approve/read/${id}`);
      } else {
        console.error("ID is undefined");
      }
    },
    getStatusText(status) {
      const statusMap = {
        0: "대기중",
        1: "결재자1승인",
        2: "최종승인",
        3: "반려",
        4: "회수",
      };
      return statusMap[status] || "알 수 없음";
    },
  },
};
</script>

<style scoped>
.container {
  margin-top: 30px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  left: 463px;
  top: 50px;
  margin-left: -50px;
  width: 60%;
  height: auto;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
.active {
  font-weight: bold;
  color: red;
}

.approveItem {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}

.approveList table {
  width: 98%;
  border-collapse: collapse;
  margin-left: 10px;
  margin-top: 30px;
}
.approveList th,
.approveList td {
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
.make-approve{
  position: absolute;
  right: 50px;
  text-decoration: none;
  font-size: 13px;
  font-weight: 600;
  padding: 7px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin-top: -20px;
}
.make-approve:hover {
  background-color: #f75c29;
}
.filter {
  margin-top: 20px;
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
