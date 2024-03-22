<template>
    <HeaderComponent />
    <SideBar />

  <div id="layoutSidenav">
    <div id="layoutSidenav_content">
      <main>
        <div class="container-fluid px-4">
          <h1 class="mt-4">모든 결재</h1>
          <ol class="breadcrumb mb-4">
          <a class="make-approve" href="/approve/create">결재만들기 </a>
          </ol>
          <div class="row">
            <div class="col-xl-3 col-md-6"></div>
          </div>
          <div class="row">
            <div class="col-xl-6"></div>
          </div>
          <div class="card mb-4">
            <div class="card-header">
              <i class="fas fa-table me-1"></i>
              내 결재들
            </div>
            <div class="card-body">
              <button @click="filterApprovalsByStatus(null)">전체 {{ statusCounts.total }}</button>
              <button @click="filterApprovalsByStatus(0)">기안중 {{ statusCounts.대기중 }}</button>
              <button @click="filterApprovalsByStatus(1)">진행중 {{ statusCounts.결재자1승인 }}</button>
              <button @click="filterApprovalsByStatus(3)">반려 {{ statusCounts.반려 }}</button>
              <button @click="filterApprovalsByStatus(2)">결제 완료 {{ statusCounts.최종승인 }}</button>
              <table id="datatablesSimple">
                <thead>
                  <tr>
                    <th>순번</th>
                    <th>기안일자</th>
                    <th>제목</th>
                    <th>기안자</th>
                    <th>결재자1</th>
                    <th>결재자2</th>
                    <th>진행상태</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(approve, index) in filteredApprovals" :key="index" @click="goToApproveReadPage(approve.id)">
                    <td>{{ approve.id }}</td>
                    <td>{{ approve.createAt }}</td>
                    <td>{{ approve.title }}</td>
                    <td>{{ approve.name }}</td>
                    <td>{{ approve.confirmer1 }}</td>
                    <td>{{ approve.confirmer2 }}</td>
                    <td>{{ getStatusText(approve.status) }}</td>
                  </tr>
                </tbody>
                <tfoot>
                  <tr>
                    <th>순번</th>
                    <th>기안일자</th>
                    <th>제목</th>
                    <th>기안자</th>
                    <th>결재자1</th>
                    <th>결재자2</th>
                    <th>진행상태</th>
                  </tr>
                </tfoot>
              </table>
            </div>
          </div>
        </div>
      </main>
      <footer class="py-4 bg-light mt-auto">
        <div class="container-fluid px-4">
          <div class="d-flex align-items-center justify-content-between small">
            <div class="text-muted">Copyright &copy; Your Website 2023</div>
            <div>
              <a href="#">Privacy Policy</a>
              &middot;
              <a href="#">Terms &amp; Conditions</a>
            </div>
          </div>
        </div>
      </footer>
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
      filteredApprovals: [], // 필터링된 결재 목록
      currentFilterStatus: null, // 현재 선택된 필터 상태
    };
  },
  computed: {
  // 상태별 개수를 계산하는 계산된 속성
  statusCounts() {
    const counts = { total: 0, 대기중: 0, 결재자1승인: 0, 최종승인: 0, 반려: 0 };

    // 모든 approvals를 순회하며 상태별로 개수를 계산합니다.
    this.approvals.forEach(approve => {
      counts.total += 1;
      const statusText = this.getStatusText(approve.status);
      if (counts[statusText] !== undefined) {
        counts[statusText] += 1;
      }
    });

    return counts;
  }
},
  async mounted() {
    await this.fetchApprovals();
  },
  methods: {
     async fetchApprovals() {
  const api = "http://192.168.0.51/api/approve/list";
  try {
        const response = await axios.get(api);
        this.approvals = response.data.result;
        this.filteredApprovals = this.approvals; // 초기 로딩 시 전체 결재 목록을 보여줍니다.
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
      };
      return statusMap[status] || "알 수 없음";
  },
  filterApprovalsByStatus(status) {
    this.currentFilterStatus = status;
    if(status === null) {
      this.filteredApprovals = this.approvals;
    } else {
      this.filteredApprovals = this.approvals.filter(approve => approve.status === status);
    }
  },
},
  
};
</script>

<style scoped>
.container-fluid {
  padding: 2rem;
}

h1.mt-4 {
  margin-bottom: 1rem;
}

/* 브레드크럼 스타일 */
.breadcrumb.mb-4 {
  background-color: #f8f9fa;
  padding: 0.75rem 1rem 50px;
  border-radius: 0.375rem;
}

/* 카드 스타일 */
.card.mb-4 {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
}

.card-header {
  background-color: #fff;
  border-bottom: 1px solid #e3e6f0;
  padding: 0.75rem 1.25rem;
}

.card-body {
  padding: 1.25rem;
}

/* 버튼 스타일 */
button {
  font-size: 18px;
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
  background-color: #F75C29;
}

/* 테이블 스타일 */
#datatablesSimple {
  width: 100%;
  border-collapse: collapse;
  text-align: center;
}

#datatablesSimple th,
#datatablesSimple td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #e3e6f0;
}

#datatablesSimple th {
  color: #495057;
  background-color: #f8f9fa;
  border-bottom: 2px solid #e3e6f0;
}

.make-approve {
  position: absolute;
  right: 5%;
  text-decoration: none;
  font-size: 18px;
  font-weight: 600;
  padding: 7px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: -5px 0px 15px 10px;
}
.make-approve:hover{
  background-color: #F75C29;
}

</style>
