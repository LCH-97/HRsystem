<template>
  <div class="all">
    <div class="approve-read-page">
      <h2>결재 상세보기</h2>
      <br /><br />
      <div>
        <h3>{{ this.approve.title }}</h3>
        <br />
        <br />
        <p>상태: {{ getStatusText(this.approve.status) }}</p>
        <br />
        <p>결재자1: {{ this.approve.confirmer1 }}</p>
        <p v-if="this.approve.status==1 ||this.approve.status==2 ">결재1 완료</p>
        <br />
        <p>결재자2: {{ this.approve.confirmer2 }}</p>
        <p v-if="this.approve.status==2 ">결재2 완료</p>

        <br />
        <br />
        <p>내용: {{ this.approve.content }}</p>
        <br />
      </div>
    </div>
    <div class="approve-button">
      <button v-if="this.approve.status==0" @click="confirm1">결재자1 결재</button>
      <button v-if="this.approve.status==1" @click="confirm2">결재자2 결재</button>
      <button v-if="this.approve.status==0" @click="reject1">결재자1 반려</button>
      <button v-if="this.approve.status==1" @click="reject2">결재자2 반려</button>
      <button v-if="this.approve.status==3" @click="updateApprove">수정</button>
      <button @click="deleteApprove">삭제</button>
      <!-- <button @click="createApproveLine">상신</button> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      approve: 1,
      approveLine: 1,
      id: this.$route.params.id,
      backend: "http://localhost:8080",
    };
  },
  methods: {
    async confirm1() {
      if (confirm("결재하시겠습니까?")) {
        console.log("approveLine:", this.approveLine);
        console.log("confirmer1Id:", this.approve.confirmer1Id);
        try {
          const confirmeResponse =await axios.patch(`${this.backend}/approve/line/confirm1`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer1Id: this.approve.confirmer1Id, // 결재자1 ID
            comment: "결재자1 승인", // 코멘트
          });
          console.log(confirmeResponse);
          console.log("결재라인이 성공적으로 승인되었습니다.");

          // await this.returnApproveStatus(1);
        } catch (error) {
          console.error("결재자1 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      }
    },
    async confirm2() {
      if (confirm("결재하시겠습니까?")) {
        try {
          const confirmeResponse =await axios.patch(`${this.backend}/approve/line/confirm2`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer2Id: this.approve.confirmer2Id, // 결재자1 ID
            comment: "결재자2 승인", // 코멘트
          });
          console.log(confirmeResponse);
          console.log("결재라인이 성공적으로 승인되었습니다.");

        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      }
    },

    async reject1() {
      if (confirm("반려하시겠습니까?")) {
        try {
          const confirmeResponse =await axios.patch(`${this.backend}/approve/line/reject1`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer1Id: this.approve.confirmer1Id, // 결재자1 ID
            comment: "결재자1 반려", // 코멘트
          });
          console.log(confirmeResponse);
          console.log("결재라인이 반려되었습니다.");

        } catch (error) {
          console.error("결재자1 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      }
    },

    async reject2() {
      if (confirm("반려하시겠습니까?")) {
        try {
          const confirmeResponse =await axios.patch(`${this.backend}/approve/line/reject2`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer2Id: this.approve.confirmer2Id, // 결재자1 ID
            comment: "결재자2 반려", // 코멘트
          });
          console.log(confirmeResponse);
          console.log("결재라인이 반려되었습니다.");

        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자2 결재 처리에 실패했습니다.");
        }
      }
    },

    async createApproveLine() {
      // if (!approveId) {
      //   alert("결재 ID가 제공되지 않았습니다.");
      //   return;
      // }

      const approveLineReq = {
        confirmer1Id: this.confirmer1Id,
        confirmer2Id: this.confirmer2Id,
        employeeId: this.employeeId,
        approveId: this.id,
      };
      try {
        const response = await axios.post(
          `${this.backend}/approve/line/create`,
          approveLineReq,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        console.log("ApproveLine 생성 성공:", response);
        alert("결재 등록 및 결재라인 생성 완료");
        this.$router.push("/approve/list");
      } catch (error) {
        console.error("결재라인 생성 실패", error);
        alert("결재라인 생성 실패: " + error.response.data.message);
      }
    },

    async returnApproveStatus(status) {
      try {
        const payload = {
          id: this.id,
          status: status,
        };

        // 수정된 객체를 사용하여 백엔드에 요청
        const returnApproveStatusRes= await axios.patch(`${this.backend}/approve/return`, payload);
        this.approveLine = returnApproveStatusRes.data.result;
        alert("결재 정보의 상태 업데이트가 성공적으로 처리되었습니다.");
      } catch (error) {
        console.error("결재 정보의 상태 업데이트에 실패했습니다:", error);
      }
    },
    async fetchApprove() {
      try {
        const approveResponse = await axios.get(
          `http://localhost:8080/approve/read/${this.id}`
        );

        if (approveResponse.data.isSuccess) {
          this.approve = approveResponse.data.result;
          console.log(approveResponse);
        } else {
          alert("결재 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error("결재 정보를 불러오는 중 오류가 발생했습니다.", error);
      }
    },
    async fetchApproveLine(approveId) {
      try {
        const response = await axios.get(
          `http://localhost:8080/approve/line/2/${approveId}`
        );
        if (response.data.isSuccess) {
          this.approveLine = response.data.result;
        } else {
          console.error("결재라인 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error(
          "결재 라인 정보를 불러오는 중 오류가 발생했습니다.",
          error
        );
      }
    },

    getStatusText(status) {
      const statusMap = {
        0: "대기중",
        1: "결재자1 승인",
        2: "최종 승인",
        3: "반려",
      };
      return statusMap[status] || "알 수 없음";
    },
    updateApprove() {
      if (this.approve.status !== 3) {
        alert("반려상태가 아니면 수정을 할 수 없습니다. 없습니다.");
        return; // 메소드 실행을 중단
      }
      const approveId = this.$route.params.id;
      localStorage.setItem(
        "updateApproveInfo",
        JSON.stringify({ ...this.approve, id: approveId })
      );
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
    // created() {
    //   this.fetchApprove();
    //   this.fetchApproveLine();
    // },
  },
  created() {
    this.fetchApprove();
    // this.returnApproveStatus();
    // this.fetchApproveLine()
  },
};
</script>

<style></style>
