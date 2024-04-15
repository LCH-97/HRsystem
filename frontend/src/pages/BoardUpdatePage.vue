<template>
  <HeaderComponent />
  <SideBar />
  <div class="container">
    <h2>공지사항 수정</h2>
    <form @submit.prevent="updateBoard">
      <div class="form-group">
        <label for="title">제목</label>
        <input
          type="text"
          id="title"
          v-model="board.title"
          class="form-control"
          required
        />
      </div>
      <div class="form-group">
        <label for="text">내용</label>
        <textarea
          id="text"
          v-model="board.text"
          class="form-control"
          required
        ></textarea>
      </div>
      <div class="form-group">
        <label>첨부 파일</label>
        <!-- 이미 업로드된 파일 목록 -->
        <ul>
          <li v-for="file in files" :key="file.id">
            {{ file.originalFilename }}
            <!-- 파일 삭제 버튼. deleteFile 메소드에 파일 ID 전달 -->
            <button @click.prevent="deleteFile(file.id, $event)">삭제</button>
          </li>
        </ul>

        <!-- 사용자가 새로 선택한 파일 목록 -->
        <div v-if="board.newFiles && board.newFiles.length > 0">
          <ul>
            <li v-for="(newFile, index) in board.newFiles" :key="index">
              {{ newFile.name }}
              <!-- 선택한 새 파일 삭제 버튼. removeNewFile 메소드에 인덱스 전달 -->
              <button @click="removeNewFile(index)">삭제</button>
            </li>
          </ul>
        </div>

        <!-- 파일 선택 입력 필드 -->
        <input type="file" @change="handleFileUpload" multiple />
      </div>
      <button type="submit" class="btn btn-primary">수정 완료</button>
    </form>
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
      axios
        .get(`http://192.168.0.51/api/board/files/${this.board.id}`)
        .then((response) => {
          this.files = response.data; // 파일 목록으로 응답 데이터 설정
        })
        .catch((error) => {
          console.error("파일 목록을 불러오는 중 오류가 발생했습니다:", error);
        });
    },

    // 새로 선택한 파일 삭제 메서드
    removeNewFile(index) {
      this.board.newFiles.splice(index, 1);
    },
    // 파일 삭제 요청
    deleteFile(fileId) {
      axios
        .delete(`http://192.168.0.51/api/board/files/delete/${fileId}`)
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
  max-width: 600px;
  margin: auto;
  margin-top: 50px;
}

.form-group {
  margin-bottom: 1rem;
}

.form-control {
  width: 100%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
}

.btn-primary {
  color: #fff;
  background-color: #007bff;
  border-color: #007bff;
  padding: 0.375rem 0.75rem;
  border-radius: 0.25rem;
  cursor: pointer;
}
</style>
