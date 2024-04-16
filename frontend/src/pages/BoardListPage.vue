<template>
  <HeaderComponent />
  <SideBar />
  <div class="container with-shadow">
    <h2>공지사항</h2>
    <div class="make-board">
      <button @click="goToBoardCreate">글쓰기</button>
    </div>
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
      <button @click="nextGroup">다음</button>
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
  width: 100%;
  top: 50px;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
.boardList table {
  width: 100%;
  border-collapse: collapse;
  margin-left: 10px;
  margin-top: 70px;
}
.boardList th,
.boardList td {
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
.make-board{
  position: absolute;
  right: 50px;
  top: 80px;
}

</style>
