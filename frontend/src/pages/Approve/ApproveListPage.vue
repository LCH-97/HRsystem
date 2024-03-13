<template>
  <!-- <HeaderComponent />
    <SideBar /> -->
  <div id="layoutSidenav">
    <div id="layoutSidenav_content">
      <main>
        <div class="container-fluid px-4">
          <h1 class="mt-4">모든 결재</h1>
          <ol class="breadcrumb mb-4">
            <li class="breadcrumb-item active">모든 결재</li>
          </ol>
          <div class="row">
            <div class="col-xl-3 col-md-6"></div>
            <div class="col-xl-3 col-md-6"></div>
            <div class="col-xl-3 col-md-6"></div>
            <div class="col-xl-3 col-md-6"></div>
          </div>
          <div class="row">
            <div class="col-xl-6"></div>
            <div class="col-xl-6"></div>
          </div>
          <div class="card mb-4">
            <div class="card-header">
              <i class="fas fa-table me-1"></i>
              내 결재들
            </div>
            <div class="card-body">
              <button>전체</button>
              <button>기안 중</button>
              <button>진행 중</button>
              <button>반려</button>
              <button>결제 완료</button>
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
                  <tr v-for="(approve, index) in approvals" :key="index">
                    <td>{{ approve.id }}</td>
                    <td>{{ approve.createAt }}</td>
                    <td>{{ approve.title }}</td>
                    <td>{{ approve.employee }}</td>
                    <td>{{ approve.confirmer1 }}</td>
                    <td>{{ approve.confirmer2 }}</td>
                    <td>{{ approve.status }}</td>
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

export default {
  name: "ApproveListPage",
  data() {
    return {
      approvals: [], // 데이터 배열 이름 수정
    };
  },
  mounted() {
    this.fetchApprovals();
  },
  methods: {
    fetchApprovals() {
      const api = "http://localhost:8080/approve/list";
      axios.get(api)
      .then((response) => {
        this.approvals = response.data.result; // 데이터 배열 이름 수정
        console.log(response.data.result);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
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
  padding: 0.75rem 1rem;
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
  background-color: #0d6efd;
  color: #fff;
  border: none;
  padding: 0.375rem 0.75rem;
  margin-right: 0.5rem;
  border-radius: 0.375rem;
  cursor: pointer;
}

button:hover {
  background-color: #0b5ed7;
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

/* 푸터 스타일 */
tfoot tr th {
  color: #495057;
  background-color: #f8f9fa;
  border-top: 2px solid #e3e6f0;
}
.btn-outline-primary {
  --bs-btn-color: #0d6efd;
  --bs-btn-border-color: #0d6efd;
  --bs-btn-hover-color: #fff;
  --bs-btn-hover-bg: #0d6efd;
  --bs-btn-hover-border-color: #0d6efd;
  --bs-btn-focus-shadow-rgb: 13, 110, 253;
  --bs-btn-active-color: #fff;
  --bs-btn-active-bg: #0d6efd;
  --bs-btn-active-border-color: #0d6efd;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #0d6efd;
  --bs-btn-disabled-bg: transparent;
  --bs-btn-disabled-border-color: #0d6efd;
  --bs-gradient: none;
}

.collapse:not(.show) {
  display: none;
}

.collapsing {
  height: 0;
  overflow: hidden;
  transition: height 0.35s ease;
}
.overflow-hidden {
  overflow: hidden !important;
}

.overflow-visible {
  overflow: visible !important;
}

.overflow-scroll {
  overflow: scroll !important;
}

.d-inline {
  display: inline !important;
}

.d-inline-block {
  display: inline-block !important;
}

.d-block {
  display: block !important;
}

.d-grid {
  display: grid !important;
}

.d-table {
  display: table !important;
}

.d-table-row {
  display: table-row !important;
}

.d-table-cell {
  display: table-cell !important;
}

.d-flex {
  display: flex !important;
}

.d-inline-flex {
  display: inline-flex !important;
}

.d-none {
  display: none !important;
}

.shadow {
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
}

.shadow-sm {
  box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075) !important;
}

.shadow-lg {
  box-shadow: 0 1rem 3rem rgba(0, 0, 0, 0.175) !important;
}

.shadow-none {
  box-shadow: none !important;
}

.position-static {
  position: static !important;
}

.position-relative {
  position: relative !important;
}

.position-absolute {
  position: absolute !important;
}

.position-fixed {
  position: fixed !important;
}

.position-sticky {
  position: sticky !important;
}

.top-0 {
  top: 0 !important;
}

.top-50 {
  top: 50% !important;
}

.top-100 {
  top: 100% !important;
}

.bottom-0 {
  bottom: 0 !important;
}

.bottom-50 {
  bottom: 50% !important;
}

.bottom-100 {
  bottom: 100% !important;
}

.start-0 {
  left: 0 !important;
}

.start-50 {
  left: 50% !important;
}

.start-100 {
  left: 100% !important;
}

.end-0 {
  right: 0 !important;
}

.end-50 {
  right: 50% !important;
}

.end-100 {
  right: 100% !important;
}

.translate-middle {
  transform: translate(-50%, -50%) !important;
}

.translate-middle-x {
  transform: translateX(-50%) !important;
}

.translate-middle-y {
  transform: translateY(-50%) !important;
}
</style>
