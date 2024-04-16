<template>
  <HeaderComponent />
  <SideBar />
  <div class="container">
    <div class="header">
      <h1 class="bold-text">공지사항 수정</h1>
    </div>
    <div class="content">
      <form @submit.prevent="updateBoard">
        <div class="form-group">
          <label for="title" class="bold-text">제목</label>
          <textarea id="title" v-model="board.title" class="form-control" rows="1" required></textarea>
        </div>
        <div class="form-group">
          <label for="text" class="bold-text">내용</label>
          <textarea id="text" v-model="board.text" class="form-control" rows="7" required></textarea>
        </div>
        <div class="form-group">
          <label class="bold-text">첨부 파일</label>
          <ul>
            <li v-for="file in files" :key="file.id">
              {{ file.originalFilename }}
              <button @click.prevent="deleteFile(file.id, $event)">삭제</button>
            </li>
          </ul>
          <div v-if="board.newFiles && board.newFiles.length > 0">
            <ul>
              <li v-for="(newFile, index) in board.newFiles" :key="index">
                {{ newFile.name }}
                <button @click="removeNewFile(index)">삭제</button>
              </li>
            </ul>
          </div>
          <input type="file" @change="handleFileUpload" multiple />
        </div>
        <div class="button">
          <button class="btn-submit bold-text">수정 완료</button>
        </div>
      </form>
    </div>
  </div>
</template>


<script>
import axios from "axios";
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";

export default {
  name: "BoardUpdatePage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      board: {
        id: "",
        title: "",
        text: "",
        files: [],
        newFiles: [], // 사용자가 새로 선택한 파일을 저장할 배열
      },
      files: [],
    };
  },
  created() {
    this.loadBoardData();
    this.fetchFiles(); // 파일 목록 불러오기 추가
  },
  methods: {
    loadBoardData() {
      const boardUpdateInfo = JSON.parse(
        localStorage.getItem("boardUpdateInfo")
      );
      if (boardUpdateInfo) {
        this.board.id = boardUpdateInfo.id;
        this.board.title = boardUpdateInfo.title;
        this.board.text = boardUpdateInfo.text;
      }
    },
    handleFileUpload(event) {
      this.board.newFiles = Array.from(event.target.files);
    },
    updateBoard() {
      console.log("Updating board");
      const formData = new FormData();

      // Prepare the JSON part of the request
      const boardUpdateReq = {
        id: this.board.id,
        title: this.board.title,
        text: this.board.text,
        // Assuming you have an array of existing file IDs to send back for reference
        existingFilesIds: this.files.map((file) => file.id),
      };
      formData.append(
        "boardUpdateReq",
        new Blob([JSON.stringify(boardUpdateReq)], { type: "application/json" })
      );

      // Append new files to the formData
      if (this.board.newFiles && this.board.newFiles.length > 0) {
        this.board.newFiles.forEach((file) => {
          formData.append("uploadFiles", file);
        });
      }
      const token = sessionStorage.getItem("token");
      axios
        .patch(`http://192.168.0.51/api/board/update`, formData, {headers: {
            Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
          },})
        .then(() => {
          alert("공지사항이 성공적으로 수정되었습니다.");
          this.$router.push("/board/list");
        })
        .catch((error) => {
          console.error("공지사항 수정 중 오류가 발생했습니다:", error);
          alert("공지사항 수정에 실패했습니다.");
        });
    },

    fetchFiles() {
      const token = sessionStorage.getItem("token");
      axios
        .get(`http://192.168.0.51/api/board/files/${this.board.id}`, {headers: {
            Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
          },})
        .then((response) => {
          this.files = response.data; // 파일 목록으로 응답 데이터 설정
        })
        .catch((error) => {
          console.error("파일 목록을 불러오는 중 오류가 발생했습니다:", error);
        });
    },

    // 새로 선택한 파일 삭제 메서드
    removeNewFile(index) {
  console.log("Removing file at index", index);
  this.board.newFiles.splice(index, 1);
},

    // 파일 삭제 요청
    deleteFile(fileId) {
      const token = sessionStorage.getItem("token");
      axios
        .delete(`http://192.168.0.51/api/board/files/delete/${fileId}`, {headers: {
            Authorization: "Bearer " + token, // 요청 헤더에 토큰을 포함시킵니다.
          },})
        .then(() => {
          alert("파일이 성공적으로 삭제되었습니다.");
          this.fetchFiles(); // 파일 목록 새로고침
        })
        .catch((error) => {
          // 오류 처리 개선
          let errorMessage = "파일 삭제에 실패했습니다.";
          if (error.response && error.response.data) {
            errorMessage += ` 서버 메시지: ${
              error.response.data.message || "상세 메시지 없음"
            }`;
          } else {
            errorMessage += ` 오류 메시지: ${error.message}`;
          }
          console.error(errorMessage);
          alert(errorMessage);
        });
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1240px; /* 너비 조정 */
  margin: 50px auto; /* 중앙 정렬 및 위쪽 여백 추가 */
  padding: 15px 20px; /* 내부 패딩은 변경하지 않음 */
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
  position: relative;
  left: 110px;
  top: 20px;
}

.header h1 {
  font-size: 22px; /* 폰트 크기 */
  color: black; /* 폰트 색상 */
  margin-bottom: 15px; /* 아래쪽 여백 */
}

.content {
  padding: 10px;
}

.form-group {
  margin-bottom: 20px; /* 하단 여백 */
}

.form-control {
  width: 100%; /* 너비 */
  padding: 8px; /* 패딩 */
  font-size: 16px; /* 폰트 크기 */
  border: 1px solid #ced4da; /* 테두리 */
  border-radius: 4px; /* 테두리 둥글게 */
}

.button {
  display: flex; /* 플렉스 박스 */
  justify-content: flex-end; /* 오른쪽 정렬 */
}

.btn-submit {
  background-color: black; /* 배경 색상 */
  color: white; /* 글자 색상 */
  border: none; /* 테두리 없음 */
  border-radius: 4px; /* 둥근 모서리 */
  padding: 10px 20px; /* 패딩 */
  font-size: 16px; /* 폰트 크기 */
  cursor: pointer; /* 커서 포인터 */
  transition: background-color 0.3s; /* 배경 색상 변경 애니메이션 */
}

.btn-submit:hover {
  background-color: #F7941E; /* 호버 시 배경 색상 변경 */
}
.bold-text {
  font-weight: bold; /* 볼드체 적용 */
}
</style>