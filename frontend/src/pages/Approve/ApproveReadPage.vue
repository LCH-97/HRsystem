<template>
  <h2>결재 상세보기</h2>
  <br /><br />
  <div v-if="approve">
    <h3>{{ approve.title }}</h3><br>
    <p>기안 시간</p>
    <p>{{ approve.createAt }}</p>
    <p>결재자1</p><br>
    <p>{{ approve.confirmer1 }}</p><br>
    <p>결재자2</p><br>
    <p>{{ approve.confirmer2 }}</p><br>
    <p>내용</p><br>
    <p>{{ approve.content }}</p><br>
  </div>
  <div class="approve-button">
    <button @click="updateApprove">수정</button>
    <button @click="deleteApprove">삭제</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      approve: null,
      id: this.$route.params.id,
    };
  },
  methods: {
    async fetchApprove() {
        console.log("1");
      try {
        const approveResponse = await axios.get(
          `http://localhost:8080/approve/read/${this.id}`
        );
        console.log("2");

        
        if (approveResponse.data.isSuccess) {
          this.approve = approveResponse.data.result;
          console.log("3");

          console.log(this.approve);
        } else {
          alert("결재 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error("결재 정보를 불러오는 중 오류가 발생했습니다.", error);
      }
    },
    

    updateApprove() {
      localStorage.setItem("updateapproveInfo", JSON.stringify(this.approve));
      this.$router.push("/approve/update");
    },
    deleteApprove() {
      if (confirm("정말로 이 결재를 삭제하시겠습니까?")) {
        axios
          .delete(`http://localhost:8080/approve/delete/${this.id}`)
          .then(() => {
            alert("결재가 성공적으로 삭제되었습니다.");
            this.$router.push("/approve/list"); // 휴가타입 목록 페이지로 리디렉션
          })
          .catch((error) => {
            console.error("결재 삭제 중 오류 발생:", error);
            alert("결재 삭제 중 오류가 발생했습니다.");
          });
      }
    },
  },
  mounted() {
    this.fetchApprove();
  },
};
</script>

<style></style>
