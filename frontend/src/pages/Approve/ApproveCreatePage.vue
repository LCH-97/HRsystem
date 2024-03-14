<template>
  <div id="layoutSidenav_content">
    <main>
      <div class="container-fluid px-4">
        <h1 class="mt-4">결재만들기</h1>
        <div class="row">
          <div class="col-xl-3 col-md-6">
            <form @submit.prevent="handleFormSubmission">
              <div class="input-group mb-3">
                <span>제목</span>
                <input
                  class="form-control"
                  type="text"
                  placeholder="제목 입력"
                  v-model="title"
                />
              </div>
              <div class="input-group mb-3">
                <span>결재자1</span>
                <select class="form-control" v-model="approver1">
                  <option
                    v-for="approver in approvers"
                    :key="approver.id"
                    :value="approver.id"
                  >
                    {{ approver.name }}
                  </option>
                </select>
              </div>
              <div class="input-group mb-3">
                <span>결재자2</span>
                <select class="form-control" v-model="approver2">
                  <option
                    v-for="approver in approvers"
                    :key="approver.id"
                    :value="approver.id"
                  >
                    {{ approver.name }}
                  </option>
                </select>
              </div>
              <div class="input-group mb-3">
                <span>첨부파일</span>
                <input type="file" ref="upload" class="upload" multiple />
              </div>
              <div class="form-group">
                <label for="content">내용:</label>
                <textarea
                  id="content"
                  class="form-control"
                  rows="10"
                  v-model="content"
                  required
                ></textarea>
              </div>
              <button type="submit">제출</button>
            </form>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ApproveCreatePage",
  data() {
    return {
      backend: "http://localhost:8080",
      title: "",
      content: "",
      approver1: null,
      approver2: null,
      approvers: [], // 결재자 목록
    };
  },
  mounted() {
    this.fetchApprovers(); // 컴포넌트 마운트 시 결재자 목록을 가져옴
  },
  methods: {
    async fetchApprovers() {
      const api = `${this.backend}/approvers`; // 서버의 결재자 목록 API
      try {
        const response = await axios.get(api);
        this.approvers = response.data; // API 응답에서 결재자 목록을 가져와 저장
      } catch (error) {
        console.error("Error fetching approvers:", error);
      }
    },
    async getApproveCreate(title, approver1, approver2, content, files) {
      const token = sessionStorage.getItem('token');
      const formData = new FormData();
      const jsonData = {
        title,
        approver1,
        approver2,
        content,
      };
      formData.append(
        "approveCreateReq",
        new Blob([JSON.stringify(jsonData)], { type: "application/json" })
      );

      if (files) {
        for (let i = 0; i < files.length; i++) {
          formData.append(`uploads[]`, files[i], files[i].name);
        }
      }

      try {
        let response = await axios.post(
          `${this.backend}/approve/create`,
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
              'Authorization': 'Bearer ' + token,
            },
          }
        );
        return response.data.result;
      } catch (error) {
        console.error("Error creating approval:", error);
      }
    },
    async handleFormSubmission() {
      const files = this.$refs.upload.files;
      await this.getApproveCreate(
        this.title,
        this.approver1,
        this.approver2,
        this.content,
        files
      );
      alert("등록 완료");
      console.log("getApproveCreate");
      this.$router.push("/approve/list");
    },
  },
};
</script>


<style scoped>
/* 여기에 스타일을 추가할 수 있습니다. */
</style>
