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
      <div class="confirm1-button" v-if="approveLine?.confirmer1Id === loggedInUserId && approve.status == 0">
        <!-- Show these buttons if the logged-in user is confirmer1 -->
          <button @click="confirm1">결재자1 결재</button>
          <button @click="reject1">결재자1 반려</button>
        </div>
        <!-- Show these buttons if the logged-in user is confirmer2 -->
        <div class="confirm1-button" v-else-if="approveLine?.confirmer2Id === loggedInUserId && approveLine?.status == 1">
          <button @click="confirm2">결재자2 결재</button>
          <button @click="reject2">결재자2 반려</button>
        </div>
        <!-- Show these buttons if the logged-in user is the one who requested the leave -->
        <div class="confirm1-button" v-else-if="approveLine?.employeeId === loggedInUserId">
          <button @click="updateApprove">수정</button>
          <button @click="deleteApprove">삭제</button>
        </div>
      <!-- <button @click="createApproveLine">상신</button> -->
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      approve: "",
      approveLine: "",
      id: this.$route.params.id,
      backend: "http://localhost:8080",
    };
  },
  methods: {
    setLoggedInUser() {
  const token = sessionStorage.getItem('token');
  if (token) {
    const decoded = jwtDecode(token);
    console.log('Decoded:', decoded); // 디코드된 토큰 출력
    this.loggedInUserId = decoded.ID;
    console.log('Logged In User ID:', this.loggedInUserId); // 사용자 ID 출력
  }
},
    async confirm1() {
      if (confirm("결재하시겠습니까?")) {
        console.log("approveLine:", this.approveLine);
        console.log("confirmer1Id:", this.approve.confirmer1Id);
        try {
           await axios.patch(`${this.backend}/approve/line/confirm1`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer1Id: this.approveLine.confirmer1Id, // 결재자1 ID
            comment: "결재자1 승인", // 코멘트
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");

          await this.returnApproveStatus(1);
          this.$router.push(`/approve/read/` + this.$route.params.id).then(() => {
  this.$router.go(0);
});
        } catch (error) {
          console.error("결재자1 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      }
    },
    async confirm2() {
      if (confirm("결재하시겠습니까?")) {
        try {
          await axios.patch(`${this.backend}/approve/line/confirm2`, {
            approveId: this.$route.params.id, // 휴가 ID
            confirmer2Id: this.approveLine.confirmer2Id, // 결재자1 ID
            comment: "결재자2 승인", // 코멘트
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");
          
          await this.returnApproveStatus(2);
          this.$router.push(`/approve/read/` + this.$route.params.id).then(() => {
  this.$router.go(0);
});
        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      }
    },

    async reject1() {
      const reason = prompt("반려 사유를 입력해주세요.");
      if (reason !== null && reason.trim() !== "") {
        try {
          await axios.patch(`${this.backend}/approve/line/reject1`, {
        approveId: this.id,
        confirmer1Id: this.approveLine.confirmer1Id,
        comment: reason, 
          });
          console.log("결재라인이 반려되었습니다.");
          await this.returnApproveStatus(3);
      this.$router.push(`/approve/read/` + this.$route.params.id).then(() => {
  this.$router.go(0);
});
        } catch (error) {
          console.error("결재자1 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      } else {
    alert("반려 사유를 입력해주세요.");
    }},

    async reject2() {
      const reason = prompt("반려 사유를 입력해주세요.");
      if (reason !== null && reason.trim() !== "") {
        try {
          await axios.patch(`${this.backend}/approve/line/reject2`, {
        approveId: this.id,
        confirmer2Id: this.approveLine.confirmer2Id,
        comment: reason, 
          });
          console.log("결재라인이 반려되었습니다.");
          await this.returnApproveStatus(3);
      this.$router.push(`/approve/read/` + this.$route.params.id).then(() => {
  this.$router.go(0);
});
        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자2 결재 처리에 실패했습니다.");
        }
      } else {
    alert("반려 사유를 입력해주세요.");
    }},

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

        await axios.patch(`${this.backend}/approve/return`, payload);
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
          await this.fetchApproveLine(this.id); // 결재라인 정보 조회
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
  mounted() {
  this.setLoggedInUser();
},
};
</script>

<style></style>
