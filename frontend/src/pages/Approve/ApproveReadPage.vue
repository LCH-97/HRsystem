<template>
  <!-- <HeaderComponent />
    <SideBar /> -->
  <div class="approve-read-all">
    <div class="container">
      <table class="approve">
        <tr>
          <th>결재</th>
          <td>
            <div class="input-group">
              <label class="input-label">결재자1 : </label>
              {{ this.approve.confirmer1 }}
            </div>
            <div class="input-group">
              <label class="input-label">상태 : </label>
              {{ getStatusText(this.approve.status) }}
            </div>
          </td>
          <td>
            <div class="input-group">
              <label class="input-label">결재자2 : </label>
              {{ this.approve.confirmer2 }}
            </div>
            <div class="input-group">
              <label class="input-label">상태 : </label>
              {{ getStatusText(this.approve.status) }}
            </div>
          </td>
        </tr>
      </table>
      <div class="header">
        <h1 class="title">{{ this.approve.title }}</h1>
        <br />
        <table class="table">
          <tr>
            <th>기안자</th>
            <td>{{ approve.name }}</td>
          </tr>
          <tr>
            <th>상태</th>
            <td>{{ getStatusText(this.approve.status) }}</td>
          </tr>
          <tr>
            <th>기안일자</th>
            <td>{{ approve.createAt }}</td>
          </tr>
          <tr>
            <th>내용</th>
            <td>{{ this.approve.content }}</td>
          </tr>
        </table>
      </div>
      <div class="approve-button">
        <div
          class="confirm1-button"
          v-if="
            approveLine?.confirmer1Id === loggedInUserId && approve.status == 0
          "
        >
          <!-- Show these buttons if the logged-in user is confirmer1 -->
          <button @click="confirm1">결 재</button>
          <button @click="reject1">반 려</button>
        </div>
        <!-- Show these buttons if the logged-in user is confirmer2 -->
        <div
          class="confirm1-button"
          v-else-if="
            approveLine?.confirmer2Id === loggedInUserId &&
            approveLine?.status == 1
          "
        >
          <button @click="confirm2">결 재</button>
          <button @click="reject2">반 려</button>
        </div>
        <!-- Show these buttons if the logged-in user is the one who requested the leave -->
        <div
          class="confirm1-button"
          v-else-if="approveLine?.employeeId === loggedInUserId"
        >
          <button @click="updateApprove">수 정</button>
          <button @click="deleteApprove">삭 제</button>
        </div>
        <!-- <button @click="createApproveLine">상신</button> -->
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { jwtDecode } from "jwt-decode";

export default {
  name: "ApproveReadPage",

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
      const token = sessionStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        console.log("Decoded:", decoded); // 디코드된 토큰 출력
        this.loggedInUserId = decoded.ID;
        console.log("Logged In User ID:", this.loggedInUserId); // 사용자 ID 출력
      }
    },
    async confirm1() {
      if (confirm("결재하시겠습니까?")) {
        console.log("approveLine:", this.approveLine);
        console.log("confirmer1Id:", this.approve.confirmer1Id);
        try {
          await axios.patch(`${this.backend}/approve/line/confirm1`, {
            approveId: this.id,
            confirmer1Id: this.approveLine.confirmer1Id, // 결재자1 ID
            comment: "결재자1 승인", // 코멘트
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");

          await this.returnApproveStatus(1);
          this.$router
            .push(`/approve/read/` + this.$route.params.id)
            .then(() => {
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
            approveId: this.$route.params.id,
            confirmer2Id: this.approveLine.confirmer2Id,
            comment: "결재자2 승인",
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");

          await this.returnApproveStatus(2);
          this.$router
            .push(`/approve/read/` + this.$route.params.id)
            .then(() => {
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
          this.$router
            .push(`/approve/read/` + this.$route.params.id)
            .then(() => {
              this.$router.go(0);
            });
        } catch (error) {
          console.error("결재자1 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 결재 처리에 실패했습니다.");
        }
      } else {
        alert("반려 사유를 입력해주세요.");
      }
    },

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
          this.$router
            .push(`/approve/read/` + this.$route.params.id)
            .then(() => {
              this.$router.go(0);
            });
        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자2 결재 처리에 실패했습니다.");
        }
      } else {
        alert("반려 사유를 입력해주세요.");
      }
    },

    async createApproveLine() {
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
        return;
      }
      const approveId = this.$route.params.id;
      localStorage.setItem(
        "updateApproveInfo",
        JSON.stringify({ ...this.approve, id: approveId })
      );
      this.$router.push("/approve/update");
    },

    async deleteApprove() {
      if (confirm("정말로 이 결재를 삭제하시겠습니까?")) {
        try {
          await axios.delete(
            `http://localhost:8080/approve/line/delete/${this.id}`
          );
          await axios.delete(`http://localhost:8080/approve/delete/${this.id}`);

          alert("결재가 성공적으로 삭제되었습니다.");
          this.$router.push("/approve/list");
        } catch (error) {
          console.error("결재 삭제 중 오류 발생:", error);
          alert("결재 삭제 중 오류가 발생했습니다.");
        }
      }
    },
  },

  created() {
    this.fetchApprove();
  },
  mounted() {
    this.setLoggedInUser();
  },
};
</script>

<style scoped>
.approve-read-all {
  margin-top: 30px;
  margin-left: 320px;
  width: 80%;
}
/* button {
  font-size: 18px;
  font-weight: 600;
  padding: 5px 10px;
  color: white;
  letter-spacing: 0.2px;
  border: none;
  border-radius: 10px;
  background-color: #111111;
  margin: 15px 0px 15px 10px;
  width: 100px;
}
button:hover {
  background-color: #f75c29; */
/* } */
.container {
  width: 800px;
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
.approve-button {
  text-align: right; /* 오른쪽 정렬 추가 */
}
.approve {
  margin-bottom: 100px; /* 결재칸과 휴가신청서 사이에 공백 추가 */
}
.header {
  margin-top: 20px; /* 헤더 위쪽에 공백 추가 */
  background-color: white;
}
.approve-button {
  margin-top: 50px; /* 결재 버튼 위쪽에 공백 추가 */
}
.approve {
  display: flex;
  justify-content: flex-end; /* 부모 요소를 오른쪽으로 정렬합니다. */
}
.approve th,
.approve td {
  padding: 8px;
  text-align: right; /* 텍스트를 오른쪽 정렬합니다. */
}
.approve .input-group {
  margin-bottom: 10px;
}
.approve .input-label {
  width: auto; /* 결재자 라벨의 너비를 자동으로 설정합니다. */
}
.approve .input-field {
  width: 200px;
}
.approve .table td:nth-last-child(2) {
  line-height: 2; /* 휴가 유형 칸의 높이를 두 배로 조절 */
}
.vac {
  height: 500px;
}
</style>
