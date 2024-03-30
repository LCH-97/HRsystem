<template>
  <HeaderComponent />
  <SideBar />
  <div class="all">

    <br><br><br><br><br><br><br><br><br><br>
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
            <td>{{ board.createAt }}</td>
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
      try {
        const response = await axios.get(`http://localhost:8080/board/check`, {
          params: { page: this.currentPage, size: this.pageSize },
        });
        this.boards = response.data.result.boards;
        this.totalPages = Math.ceil(
          response.data.result.totalElements / this.pageSize
        );
      } catch (error) {
        console.error("Failed to fetch boards:", error);
      }
    },
    goToBoardReadPage(id) {
      this.$router.push(`/board/read/${id}`);
    },
  },
};
</script>
<style scoped>
.active {
  font-weight: bold;
  color: red;
}
.button-container {
  text-align: left;
  padding-right: 40px;
  margin-bottom: 20px;
}
.button-container2 {
  text-align: right;
  padding-right: 40px;
  margin-bottom: 20px;
}
.button-container button,
.button-container2 button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white; /* 글씨 색상을 흰색으로 설정 */
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111; /* 기본 배경색을 검정색으로 설정 */
  margin-left: 10px; /* 버튼 간격 조정 */
  transition: background-color 0.3s; /* 부드러운 색상 전환 효과 */
}
.button-container button:hover,
.button-container2 button:hover {
  background-color: #f75c29; /* 마우스를 올렸을 때 배경색을 주황색으로 변경 */
  color: white;
}
.boardList ul {
  list-style: none;
  padding: 0;
  margin-left: 100px;
}
.boardItem {
  cursor: pointer;
  margin: 10px 0;
  transition: color 0.3s ease;
}
.all {
  margin-left: 100px;
}

.boardList table {
  width: 100%;
  border-collapse: collapse;
}
.boardList th,
.boardList td {
  border: 1px solid #ddd;
  padding: 8px;
}
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px; /* 페이지네이션과 위의 내용 사이에 공간 추가 */
}

</style>
