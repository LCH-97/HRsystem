<template>
  <div>
    <HeaderComponent />
    <div class="main-content">
      <SideBar />
      <div class="container">
        <h2>공지사항 생성</h2>
        <form @submit.prevent="createBoard">
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
            <label for="files">파일 첨부</label>
            <input type="file" id="files" ref="files" multiple />
          </div>
          <button type="submit" class="btn btn-primary">공지사항 생성</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";

export default {
  name: "BoardCreatePage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      board: {
        title: "",
        text: "",
      },
    };
  },
  methods: {
    createBoard() {
      // 인증 토큰을 sessionStorage에서 가져옵니다.
      const token = sessionStorage.getItem("token");

      // FormData 인스턴스를 생성합니다.
      const formData = new FormData();
      formData.append(
        "boardCreateReq",
        new Blob([JSON.stringify(this.board)], { type: "application/json" })
      );

      // 선택된 파일들을 formData에 추가합니다.
      if (this.$refs.files && this.$refs.files.files.length) {
        for (let file of this.$refs.files.files) {
          formData.append("uploadFiles", file);
        }
      }

      // 요청 헤더에 인증 토큰을 추가하는 설정 객체를 생성합니다.
      const config = {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${token}`, // Authorization 헤더에 토큰 추가
        },
      };

      // 공지사항 생성 API를 호출합니다.
      axios
        .post("http://localhost:8080/board/create", formData, config)
        .then((response) => {
          console.log(response.data);
          alert("공지사항이 성공적으로 생성되었습니다.");
          this.$router.push("/board/list");
        })
        .catch((error) => {
          if (error.response && error.response.data) {
            // 서버로부터 받은 에러 메시지를 alert로 표시
            alert(`에러: ${error.response.data.message}`);
          } else {
            alert("휴가 등록 중 문제가 발생했습니다.");
          }
        });
    },
  },
};
</script>

<style scoped>
.main-content {
  display: flex;
  margin-left: 250px;
}

.container {
  flex-grow: 1;
  padding: 20px;
  margin-top: 50px;
}

.form-group {
  margin-bottom: 1rem;
}

.label,
.form-control {
  display: block;
  width: 100%;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  color: #495057;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
  transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

.btn-primary {
  color: #fff;
  background-color: #000000;
  border-color: #000000;
}

.btn-primary:hover {
  color: #fff;
  background-color: #000000;
  border-color: #000000;
}

.btn {
  display: inline-block;
  font-weight: 400;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  border: 1px solid transparent;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
  line-height: 1.5;
  border-radius: 0.25rem;
  transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out,
    border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
}

/* 버튼을 마우스를 올렸을 때의 스타일 */
.btn:hover {
  color: #fff;
  background-color: orange;
  border-color: orange;
}
</style>

