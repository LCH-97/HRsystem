<template>
  <div class="container">
    <table class="approve">
      <tr>
        <td>
          <div class="input-group">
            <label class="input-label">작성자 : </label>
            {{ board?.writerName }}
          </div>
        </td>
      </tr>
    </table>
    <div class="header">
      <div v-if="board">
        <h1 class="title">제목</h1>
        {{ board?.title }}
        <br />
        <table class="table">
          <tr>
            <th>첨부 파일</th>
            <td colspan="2">
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
            <h1 class="title">내용</h1>
            {{
              board?.text
            }}
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

export default {
  data() {
    return {
      board: null,
      id: this.$route.params.id,
      backend: "http://localhost:8080", // 백엔드 서버 주소
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
        this.loggedInUserId = decoded.userId; // 토큰 디코딩 방식에 따라 적절하게 조정
      }
    },
    // 공지사항 상세 정보 조회
    async fetchBoard() {
      try {
        const response = await axios.get(
          `${this.backend}/board/check/${this.id}`
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
      axios
        .get(`${this.backend}/board/files/${this.id}`)
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
  },
};
</script>

<style scoped>
.container {
  width: 600px;
  margin: 0 auto;
}
.header {
  text-align: center;
}
.title {
  font-size: 24px;
  font-weight: bold;
}
.table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  border: 1px solid #ddd;
  padding: 8px;
}
th {
  text-align: center;
}
.input-label {
  display: inline-block;
  width: 100px;
  text-align: right;
}
.input-field {
  width: 200px;
}
.button {
  margin-top: 10px;
  padding: 5px 10px;
  font-size: 16px;
  font-weight: bold;
  border: 1px solid #ddd;
  background-color: #fff;
  cursor: pointer;
}
.input-group {
  margin-bottom: 10px;
}
.input-label {
  display: inline-block;
  width: 100px;
  text-align: right;
}
.input-field {
  width: 200px;
}
.input-group.approval {
  margin-top: 20px;
}
.goout-button {
  text-align: right; /* 오른쪽 정렬 추가 */
}
.goout-button {
  margin-top: 20px; /* 결재 버튼 위쪽에 공백 추가 */
}
.approve {
  display: flex;
  justify-content: flex-end; /* 부모 요소를 오른쪽으로 정렬합니다. */
}
.approve th,
.approve td {
  text-align: right; /* 텍스트를 오른쪽 정렬합니다. */
}
.approve .input-label {
  width: auto; /* 결재자 라벨의 너비를 자동으로 설정합니다. */
}
.approve .input-field {
  width: 200px;
}
.file-list {
  list-style-type: none; /* Remove bullets from file list */
  padding-left: 0; /* Remove indentation */
  margin-bottom: 0; /* Reduce margin for file list */
}
.dates,
.vac {
  padding-top: 5px; /* Reduced padding for specific rows */
  padding-bottom: 5px; /* Reduced padding for specific rows */
}
</style>