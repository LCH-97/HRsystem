<template>
  <HeaderComponent />
  <SideBar />
  <div class="container with-shadow">
    <div class="contauner1">
      <div class="header">
        <h1 class="title">{{ board?.title }}</h1>
        <br/>
        <table class="table">
          <tr>
            <th>작성자</th>
            <td>{{ board?.writerName }}</td>
          </tr>
          <tr>
            <th>작성 일자</th>
            <td>{{ formatDate(board?.createAt) }}</td>
          </tr>
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
          <tr>
            <th>내용</th>
            <td class="board-text" colspan="2">{{ board?.text }}</td>
          </tr>
        </table>

      <div class="board-button">
        <!-- Show these buttons if the logged-in user is the one who requested the leave -->
        <div v-if="board?.writerId === loggedInUserId">
          <button @click="updateBoard">수정</button>
          <button @click="deleteBoard">삭제</button>
        </div>
        <!-- If logged-in user's ID does not match any, do not show any buttons -->
      </div>
    </div>
    </div>
  </div>
  <footer><br /><br /><br /><br /><br /></footer>
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
        .get(`${this.backend}/board/files/${this.id}`, {
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
          const token = sessionStorage.getItem("token");
          await axios.delete(`${this.backend}/board/delete/${this.id}`, {
          headers: {
            Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
          },
        });
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
  margin-top: 65px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  height: auto;
  width: 810px;
  margin-left: auto; /* 좌우 마진을 자동으로 설정 */
  margin-right: auto; /* 좌우 마진을 자동으로 설정 */
  left: 110px;
}
.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}
.title {
  font-size: 24px;
  font-weight: bold;
  margin-left: 70px;
}
.table {
  width: 770px;
  border-collapse: collapse;
  margin: 0 auto;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  word-wrap: break-word;
}
th {
  text-align: center;
}
.header {
  margin-top: 20px; /* 헤더 위쪽에 공백 추가 */
  background-color: white;
  text-align: center;
  margin-left: 0 auto;
  margin-right: 0 auto;
}
button {
  color: white;
  margin-top: 10px;
  padding: 5px 10px;
  font-size: 16px;
  font-weight: bold;
  border: 1px solid #ddd;
  background-color: #111111;
  cursor: pointer;
  border-radius: 10px;
}
button:hover {
  background-color: #f75c29;
}
.board-button {
  display: flex;
  justify-content: flex-end;
}
.board-text{
  text-align: left; /* 텍스트를 왼쪽으로 정렬 */
  vertical-align: top; /* 콘텐츠를 셀의 상단으로 정렬 */
  min-height: 400px;
  width: 665px;
}
</style>
