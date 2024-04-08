<template>
  <HeaderComponent />
  <SideBar />
  <div class="container">
    <table class="approve">
      <tr>
        <td>
          <div class="input-group">
            <label class="input-label">작성자: </label>
            {{ board?.writerName }}
          </div>
        </td>
      </tr>
    </table>
    <table class="date">
      <tr>
        <td>
          <div class="input-group">
            <label class="input-label">작성: </label>
            {{ formatDate(board?.createAt) }}
          </div>
          <div class="input-group">
            <label class="input-label">수정: </label>
            {{ formatDate(board?.updateAt) }}
          </div>
        </td>
      </tr>
    </table>
    <div class="header">
      <div v-if="board">
        <h1 class="title">제목</h1>
        {{ board?.title }}
        <table class="table">
          <!-- 첨부 파일을 오른쪽에 정렬하고 싶다면 아래와 같이 td에 class를 추가합니다 -->
          <tr>
            <th>첨부 파일</th>
            <td class="file-list-container" colspan="2">
              <div v-if="files.length > 0">
                <ul class="file-list">
                  <li v-for="file in files" :key="file.originalFilename">
                    <a :href="file.downloadUrl" target="_blank">{{
                      file.originalFilename
                    }}</a>
                  </li>
                </ul>
              </div>
              <div v-else>첨부 파일 없음</div>
            </td>
          </tr>
          <!-- 내용 부분을 새로운 행으로 분리합니다 -->
          <tr>
            <th>내용</th>
            <td class="board-text" colspan="2">{{ board?.text }}</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
  <div class="board-button">
    <!-- Show these buttons if the logged-in user is the one who requested the leave -->
    <div v-if="board?.writerId === loggedInUserId">
      <button @click="updateBoard">수정</button>
      <button @click="deleteBoard">삭제</button>
    </div>
    <!-- If logged-in user's ID does not match any, do not show any buttons -->
  </div>
</template>

<script>
import axios from "axios";
import jwtDecode from "jwt-decode";
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";

export default {
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      board: null,
      id: this.$route.params.id,
      backend: "http://192.168.0.51/api", // 백엔드 서버 주소
      files: [], // 파일 목록을 저장할 배열
      loggedInUserId: null, // 현재 로그인한 사용자의 ID
    };
  },
  async created() {
    this.setLoggedInUser();
    await this.fetchBoard();
    await this.fetchFiles();
  },
  methods: {
    // 현재 로그인한 사용자의 정보 설정
    setLoggedInUser() {
      const token = sessionStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        this.loggedInUserId = decoded.ID; // 토큰 디코딩 방식에 따라 적절하게 조정
      }
    },
    // 공지사항 상세 정보 조회
    async fetchBoard() {
      const token = sessionStorage.getItem("token");
      try {
        const response = await axios.get(
          `${this.backend}/board/check/${this.id}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
            },
          }
        );
        this.board = response.data.result;
      } catch (error) {
        console.error(
          "공지사항 상세 정보를 불러오는 중 오류가 발생했습니다.",
          error
        );
        alert("공지사항 정보를 불러오는데 실패했습니다.");
      }
    },
    // 첨부 파일 목록 조회
    fetchFiles() {
      // 특정 휴가 결재 ID에 대한 파일 목록을 가져오도록 URL 수정
      const token = sessionStorage.getItem("token");
      axios
        .get(`${this.backend}/board/files/${this.id}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
            },
          })
        .then((response) => {
          this.files = response.data; // 파일 목록 업데이트
        })
        .catch((error) => {
          console.error("파일 목록을 가져오는 중 오류가 발생했습니다.", error);
        });
    },
    // 공지사항 수정 페이지로 이동
    updateBoard() {
      localStorage.setItem(
        "boardUpdateInfo",
        JSON.stringify({
          id: this.id,
          title: this.board.title,
          text: this.board.text,
        })
      );
      this.$router.push(`/board/update`);
    },
    // 공지사항 삭제
    async deleteBoard() {
      if (confirm("정말로 이 공지사항을 삭제하시겠습니까?")) {
        try {
          await axios.delete(`${this.backend}/board/delete/${this.id}`);
          alert("공지사항이 삭제되었습니다.");
          this.$router.push(`/board/list`);
        } catch (error) {
          console.error("공지사항 삭제 중 오류가 발생했습니다:", error);
          alert("공지사항 삭제 중 오류가 발생했습니다.");
        }
      }
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
  max-width: 1150px;
  margin-top: 80px;
  margin-left: 300px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.board-button {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.header {
  display: flex;
  /* justify-content: center; 이 부분을 제거합니다. */
  align-items: center;
  margin-bottom: 20px;
  margin-top: 20px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
  align-self: flex-start; /* 추가: 제목을 왼쪽에 정렬합니다. */
}

.text {
  font-size: 18px;
  margin-bottom: 10px;
  align-self: flex-start; /* 추가: 제목을 왼쪽에 정렬합니다. */
}

.table,
.approve {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.input-group {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.input-label {
  margin-right: 0.5rem;
  font-weight: bold;
}

.board-text {
  font-size: 0.875rem; /* 내용의 글씨 크기를 조절합니다 */
}

.file-list-container {
  justify-content: flex-end; /* 첨부 파일 목록을 오른쪽으로 정렬합니다 */
}

.file-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
  /* text-align: center; 이 부분을 제거합니다. */
}

.file-list li {
  display: inline;
  margin-right: 10px;
}

.file-list li a {
  color: #007bff;
  text-decoration: underline;
}

.board-button button {
  padding: 5px 15px;
  margin: 0 5px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

.board-button button:hover {
  background-color: #0056b3;
}
</style>
