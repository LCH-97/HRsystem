<template>
  <HeaderComponent />
  <SideBar />
  <h1>결재 목록</h1>
  <div class="container with-shadow">
    <div class="filter">
      <button @click="filterApprovalsByStatus(null)">전체</button>
      <button @click="filterApprovalsByStatus(0)">기안중 {{ statusCounts.대기중 }} </button>
      <button @click="filterApprovalsByStatus(1)">진행중 {{ statusCounts.결재자1승인 }} </button>
      <button @click="filterApprovalsByStatus(3)">반려 {{ statusCounts.반려 }} </button>
      <button @click="filterApprovalsByStatus(2)">결재 완료</button>
      <button @click="filterApprovalsByStatus(4)">회수</button>
    </div>
    <div>
      <a class="make-approve" href="/approve/create">결재만들기 </a>
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
      <button v-for="page in pageGroup" :key="page" :class="{ active: page === currentPage }"
        @click="changePage(page)">
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
      filteredApprovals: [], // 필터링된 결재 목록
      currentFilterStatus: null, // 현재 선택된 필터 상태
      confirmer1: "",
      confirmer2: "",
      currentPage: 1,
      pageSize: "",
      pagesToShow: 5,
      pageGroupStart: 1,
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.approvals.length / this.pageSize);
    },
    pageGroup() {
      // 현재 페이지가 포함된 페이지 그룹의 시작 페이지를 계산합니다.
      let startPage =
        Math.floor((this.currentPage - 1) / this.pagesToShow) *
        this.pagesToShow +
        1;
      // 시작 페이지를 기준으로 pagesToShow만큼의 페이지 번호를 생성합니다.
      // 단, 전체 페이지 수를 초과하지 않도록 주의합니다.
      let pages = [];
      for (let i = 0; i < this.pagesToShow; i++) {
        let page = startPage + i;
        if (page > this.totalPages) break; // 전체 페이지 수를 초과하지 않도록 합니다.
        pages.push(page);
      }
      return pages;
    },

    // 상태별 개수를 계산하는 계산된 속성
    statusCounts() {
      const counts = { total: 0, 대기중: 0, 결재자1승인: 0, 최종승인: 0, 반려: 0 };

      // 모든 approvals를 순회하며 상태별로 개수를 계산합니다.
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
  async mounted() {
    await this.fetchApprovals();
    //   // await this.fetchApproveLine();
  },
  methods: {
    changePage(page) {
      // 페이지를 변경하고, 새로운 페이지의 데이터를 불러옵니다.
      this.currentPage = page;
      this.fetchApprovals();
    },

    prevGroup() {
      // 이전 그룹으로 이동 (페이지 번호 배열만 -5)
      this.pageGroupStart = Math.max(1, this.pageGroupStart - this.pagesToShow);
      // 현재 페이지도 페이지 그룹의 첫 페이지로 설정
      this.changePage(this.pageGroupStart);
    },
    nextGroup() {
      // 다음 그룹으로 이동 (페이지 번호 배열만 +5)
      if (this.pageGroupStart + this.pagesToShow <= this.totalPages) {
        this.pageGroupStart += this.pagesToShow;
        // 현재 페이지도 페이지 그룹의 첫 페이지로 설정
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

      const api = `http://192.168.0.51/api/approve/list?page=${this.currentPage - 1}&size=${this.pageSize}`;
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(api, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        });
        if (
          response.data &&
          response.data.result &&
          Array.isArray(response.data.result.content)
        ) {
          this.approvals = response.data.result.content;
          this.filteredApprovals = this.approvals;
          // this.approvals = Array.isArray(response.data.result) ? response.data.result : [];
          // this.filteredApprovals = this.approvals; // 초기 로딩 시 전체 결재 목록을 보여줍니다.

          // 모든 결재 항목에 대해 결재라인 정보를 불러옵니다.
          // for (const approve of this.approvals) {
          //   await this.fetchApproveLine(approve.id); // 각 결재 항목의 id를 사용하여 fetchApproveLine 호출
          // }
        } else {
          this.approvals = [];
          this.filteredApprovals = [];
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        this.approvals = [];
        this.filteredApprovals = [];
      }
      // const start = (this.currentPage - 1) * this.pageSize;
      //   const end = this.currentPage * this.pageSize;
      //   this.filteredApprovals = this.approvals.slice(start, end);
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
  margin-top: 50px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  left: 300px;
  height: auto;
  margin-left: -50px;
  width: 80%;
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
  font-size: 15px;
  font-weight: 600;
  padding: 7px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin-top: -45px;
}
.make-approve:hover {
  background-color: #f75c29;
}
.filter {
  margin-top: 20px;
}
button {
  font-size: 15px;
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
