<template>
  <div class="container with-shadow">
    <h2>휴가 목록</h2>
      <div class="filter">
        <button @click="filterGoouts(null)">전체</button>
        <button @click="filterGoouts(0)">기안중</button>
        <button @click="filterGoouts(1)">진행중</button>
        <button @click="filterGoouts(3)">반려</button>
        <button @click="filterGoouts(2)">결재 완료</button>
        <button @click="filterGoouts(4)">등록 취소</button>
      </div>
      <div class="make-goout">
        <a @click="goToGooutCreate">휴가 등록</a>
      </div>
      <div class="gooutList">
        <table>
          <thead>
            <tr>
              <th>게시글 번호</th>
              <th>이름</th>
              <th>작성자 이름</th>
              <th>휴가 유형</th>
              <th>상태</th>
              <th>시작 날짜</th>
              <th>종료 날짜</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="goout in filteredGoouts"
              :key="goout.id"
              @click="goToGooutReadPage(goout.id)"
              class="gooutItem"
            >
              <td>{{ goout.id }}</td>
              <td>{{ goout.name }}</td>
              <td>{{ goout.writerName }}</td>
              <td>{{ goout.gooutTypeName }}</td>
              <td>
                {{ getStatusText(goout.status) }}
              </td>
              <td>{{ goout.first }}</td>
              <td>{{ goout.last }}</td>
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
export default {
  name: "VacationPage",
  data() {
    return {
      goouts: [],
      filteredGoouts: [],
      currentPage: 1,
      pageSize: 10,
      pagesToShow: 5,
      pageGroupStart: 1, // 페이지 그룹의 시작 페이지 번호
      totalPages: 0, // 전체 페이지 수
    };
  },
  created() {
    this.fetchGoouts();
  },
  computed: {
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
  },
  methods: {
    changePage(page) {
      // 페이지를 변경하고, 새로운 페이지의 데이터를 불러옵니다.
      this.currentPage = page;
      this.fetchGoouts();
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

    getStatusText(status) {
      const statusMap = {
        0: "기안중",
        1: "진행중",
        2: "최종 승인",
        3: "반려",
        4: "등록 취소",
        // 필요한 다른 상태들...
      };
      return statusMap[status] || "알 수 없음";
    },
    goToGooutCreate() {
      this.$router.push("/goout/create");
    },
    async fetchGoouts() {
  console.log(`휴가리스트 읽기 시작`);
  try {
    const token = sessionStorage.getItem("token");
    const payload = {
      page: this.currentPage,
      size: this.pageSize
    };
    const response = await axios.post(`http://192.168.0.51/api/goout/check`, payload, {
      headers: {
        Authorization: "Bearer " + token,
        'Content-Type': 'application/json'
      }
    });
    if (response.data.code === 1200 && response.data.isSuccess) {
      const data = response.data.result;
      this.goouts = data.goouts;
      this.filteredGoouts = this.goouts;
      this.totalPages = Math.ceil(data.totalElements / this.pageSize);
    } else {
      console.error("API call successful, but unexpected response structure:", response.data);
    }
    console.log(`휴가리스트 읽기 끝`);
  } catch (error) {
    console.error("Failed to fetch goouts:", error.response ? error.response.data : error);
  }
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
        this.filteredGoouts = this.goouts.filter(
          (goout) => goout.status === status
        );
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
  left: 230px;
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

.gooutItem {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}

.gooutList table {
  width: 98%;
  border-collapse: collapse;
  margin-left: 10px;
  margin-top: 30px;
}
.gooutList th,
.gooutList td {
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
.make-goout{
  position: absolute;
  right: 40px;
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
.make-goout:hover {
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