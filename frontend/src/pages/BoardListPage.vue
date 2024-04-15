<template>
  <HeaderComponent />
  <SideBar />
  <div class="all">
    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
    <div class="boardList">
      <table>
        <thead>
          <tr>
            <th>게시글 번호</th>
            <th>작성자 이름</th>
            <th>제목</th>
            <th>시간</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="board in boards"
            :key="board.id"
            @click="goToBoardReadPage(board.id)"
            class="boardItem"
          >
            <td style="text-align: center">{{ board.id }}</td>
            <td>{{ board.writerName }}</td>
            <td>{{ board.title }}</td>
            <td>{{ formatDate(board.createAt) }}</td>
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
      <button @click="nextGroup">이후</button>
    </div>
    <div class="button-container2">
      <button @click="goToBoardCreate">글쓰기</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";
export default {
  name: "BoardListPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      boards: [],
      currentPage: 1,
      pageSize: 10,
      pagesToShow: 5,
      pageGroupStart: 1,
      totalPages: 0,
    };
  },
  created() {
    this.fetchBoards();
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
  },
  methods: {
    changePage(page) {
      this.currentPage = page;
      this.fetchBoards();
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
    goToBoardCreate() {
      this.$router.push("/board/create");
    },
    async fetchBoards() {
  const token = sessionStorage.getItem("token");
  const payload = {
    page: this.currentPage,
    size: this.pageSize
  };
  try {
    const response = await axios.post(`http://192.168.0.51/api/board/check`, payload, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    this.boards = response.data.result.boards;
    this.totalPages = Math.ceil(response.data.result.totalElements / this.pageSize);
  } catch (error) {
    console.error("Failed to fetch boards:", error);
  }
},
    goToBoardReadPage(id) {
      this.$router.push(`/board/read/${id}`);
    },
    formatDate(isoString) {
      const date = new Date(isoString);
      const year = date.getFullYear();
      const month = date.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
      const day = date.getDate();
      const hour = date.getHours();
      const minute = date.getMinutes();

      const formattedDate = `${year}-${month.toString().padStart(2, "0")}-${day
        .toString()
        .padStart(2, "0")}`;
      const formattedTime = `${hour.toString().padStart(2, "0")}:${minute
        .toString()
        .padStart(2, "0")}`;

      return `${formattedDate} ${formattedTime}`;
    },
  },
};
</script>

<style scoped>
.all {
  margin-top: -125px; /* 위쪽 마진을 줄임 */
  margin-left: 300px;
  margin-right: auto;
  max-width: 1150px; /* 최대 너비를 줄여 좁은 폭으로 설정 */
}

.boardList table {
  width: 100%;
  border-collapse: collapse;
  text-align: center; /* 테이블 내용 중앙 정렬 */
}

.boardList th {
  background-color: #f8f9fa; /* 헤더 배경색 */
  color: #495057; /* 헤더 글자색 */
  border-bottom: 2px solid #e3e6f0; /* 하단 테두리 스타일 */
  padding: 0.75rem;
}

.boardList td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #e3e6f0;
}

.pagination button,
.button-container2 button {
  font-size: 18px; /* 폰트 크기 조정 */
  font-weight: 600; /* 폰트 굵기 조정 */
  padding: 5px 10px; /* 패딩 조정 */
  color: white; /* 글자 색상을 흰색으로 설정 */
  letter-spacing: 0.2px; /* 글자 간격 조정 */
  border: none; /* 테두리 제거 */
  border-radius: 10px; /* 버튼 모서리 둥글게 조정 */
  background-color: #111111; /* 배경색을 검정색으로 설정 */
  margin: 5px; /* 버튼 간 마진 조정 */
  transition: background-color 0.3s; /* 배경색 변경 시 효과 조정 */
}

.button-container2 {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  align-items: center;
  position: fixed; /* 고정 위치 */
  bottom: 20px; /* 아래쪽 여백 */
  right: 20px; /* 오른쪽 여백 */
}

.pagination button:hover,
.button-container2 button:hover {
  background-color: #f75c29; /* 마우스 호버 시 배경색을 주황색으로 변경 */
}

.active {
  font-weight: bold; /* 활성 페이지 번호를 굵게 표시 */
  color: #495057; /* 활성 페이지 번호의 글자색 변경 */
  background-color: #e9ecef; /* 활성 페이지 번호의 배경색 변경 */
}

.boardItem:hover {
  background-color: #f8f9fa; /* 항목에 마우스를 올렸을 때 배경색 변경 */
  cursor: pointer; /* 마우스 호버 시 커서 변경 */
}

.pagination {
  display: flex;
  justify-content: center; /* 중앙 정렬을 위한 스타일 */
  align-items: center;
  width: 100%; /* 컨테이너 너비 전체 사용 */
  margin-top: 20px; /* 상단 여백 추가 */
}
</style>
