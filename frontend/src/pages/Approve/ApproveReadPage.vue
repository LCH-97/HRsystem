<template>
  <HeaderComponent />
  <SideBar />
  <div class="approve-read-all">
    <div class="container">
      <table class="approve">
        <tr>
          <th>결재</th>
          <td>
            <div class="input-group">
              <label class="input-label">결재자1 : </label>
              {{ confirmer1?.confirmerName }}
            </div>
            <div class="input-group">
              <label class="input-label"
                >상태 : {{ getStatusText(confirmer1?.status) }}</label
              >
            </div>
          </td>
          <td>
            <div class="input-group">
              <label class="input-label">결재자2 : </label>
              {{ confirmer2?.confirmerName }}
            </div>
            <div class="input-group">
              <label class="input-label"
                >상태 : {{ getStatusText(confirmer2?.status) }}</label
              >
            </div>
          </td>
        </tr>
      </table>
      <div class="header">
        <h1 class="title">{{ this.approve.title }}</h1>
        <br />
        <table class="table">
          <tr v-if="files.length > 0">
            <th>첨부파일</th>
            <td v-for="file in files" :key = "file.name">
            <a :href="file.downloadUrl" target = "_blank">{{ file.name }}</a>
            </td>
          </tr>
          <tr>
            <th>기안자</th>
            <td>{{ approve.employeeName }}</td>
          </tr>
          <tr>
            <th>상 태</th>
            <td>{{ getStatusText1(this.approve.status) }}</td>
          </tr>
          <tr v-if="confirmer1.status === 3">
            <th>결재자1 반려 사유</th>
            <td>{{ confirmer1.comment }}</td>
          </tr>
          <tr v-if="confirmer2.status === 3">
            <th>결재자2 반려 사유</th>
            <td>{{ confirmer2.comment }}</td>
          </tr>
          <tr>
            <th>기안일자</th>
            <td>{{ approve.createAt }}</td>
          </tr>
          <tr>
            <th>내 용</th>
            <td>
            <div class="contable">{{ this.approve.content }}</div>
            </td>
          </tr>
        </table>
      </div>
      <div class="approve-button">
        <div
          class="confirm1-button"
          v-if="
            confirmer1?.confirmerId === loggedInUserId && approve.status == 0
          "
        >
          <button @click="confirm1">결 재</button>
          <button @click="reject1">반 려</button>
        </div>
        <div
          class="confirm1-button"
          v-else-if="
            confirmer2?.confirmerId === loggedInUserId && approve?.status == 1
          "
        >
          <button @click="confirm2">결 재</button>
          <button @click="reject2">반 려</button>
        </div>
        <div
          class="confirm1-button"
          v-else-if="approve?.employeeId === loggedInUserId && approve?.status != 4"
        >
          <button @click="updateApprove">수 정</button>
          <button @click="deleteApprove">회 수</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import jwtDecode from "jwt-decode";
import HeaderComponent from "@/components/HeaderComponent.vue";
import SideBar from "@/components/SideBar.vue";

export default {
  name: "ApproveReadPage",
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      approve: "",
      approveLine: "",
      id: this.$route.params.id,

      confirmer1: "",
      confirmer2: "",
      backend: "http://localhost:8080",
      files: [],
    };
  },
  methods: {
    fetchFiles() {
      // 특정 결재 ID에 대한 파일 목록을 가져오도록 URL 수정
      const token = sessionStorage.getItem("token");
      axios.get(`${this.backend}/approve/files/${this.id}`,{
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
          .then(response => {
            this.files = response.data; // 파일 목록 업데이트
          })
          .catch(error => {
            console.error("파일 목록을 가져오는 중 오류가 발생했습니다.", error);
          });
    },
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
        try {        
          const token = sessionStorage.getItem("token");
          await axios.patch(`${this.backend}/approve/line/confirm1`, {
            approveId: this.id,
            confirmerId: this.confirmer1.confirmerId, // 결재자1 ID
            comment: "결재자1 승인", // 코멘트
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
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
          const token = sessionStorage.getItem("token");
          await axios.patch(`${this.backend}/approve/line/confirm2`, {
            approveId: this.id,
            confirmerId: this.confirmer2.confirmerId, // 결재자1 ID
            comment: "결재자2 승인",
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
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
          alert("결재자2 결재 처리에 실패했습니다.");
        }
      }
    },

    async reject1() {
      const reason = prompt("반려 사유를 입력해주세요.");
      if (reason !== null && reason.trim() !== "") {
        try {
          const token = sessionStorage.getItem("token");
          await axios.patch(`${this.backend}/approve/line/reject1`, {
            approveId: this.id,
            confirmerId: this.confirmer1.confirmerId,
            comment: reason,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
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
          const token = sessionStorage.getItem("token");
          await axios.patch(`${this.backend}/approve/line/reject2`, {
            approveId: this.id,
            confirmerId: this.confirmer2.confirmerId,
            comment: reason,
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
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

    async returnApproveStatus(status) {
      try {
        const payload = {
          id: this.id,
          status: status,
        };
        const token = sessionStorage.getItem("token");
        await axios.patch(`${this.backend}/approve/return`, payload,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + token,
            },
          });
        alert("결재 정보의 상태 업데이트가 성공적으로 처리되었습니다.");
      } catch (error) {
        console.error("결재 정보의 상태 업데이트에 실패했습니다:", error);
      }
    },
    async fetchApprove() {
      try {
        const token = sessionStorage.getItem("token");
        const approveResponse = await axios.get(
          `http://localhost:8080/approve/read/${this.id}`
        );

        if (approveResponse.data.isSuccess) {
          this.approve = approveResponse.data.result;
          await this.fetchApproveLine(this.id); // 결재라인 정보 조회
          this.fetchFiles();
        } else {
          alert("결재 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error("결재 정보를 불러오는 중 오류가 발생했습니다.", error);
      }
    },
    async fetchApproveLine(approveId) {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get(
          `http://localhost:8080/approve/line/2/${approveId}`
        );
        if (response.data.isSuccess && response.data.result.length >= 2) {
          this.confirmer1 = response.data.result[0];
          this.confirmer2 = response.data.result[1];
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
        1: "승인",
        2: "승인",
        3: "반려",
        4: "회수",
      };
      return statusMap[status] || "알 수 없음";
    },
    getStatusText1(status) {
      const statusMap = {
        0: "대기중",
        1: "결재자1 승인",
        2: "최종 승인",
        3: "반려",
        4: "회수",

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
        JSON.stringify({
          id: approveId,
          title: this.approve.title,
          content: this.approve.content,
        })
      );
      localStorage.setItem(
        "updateAPproveLineInfo",
        JSON.stringify({
          confirmer1Id: this.confirmer1?.confirmerId,
          confirmer2Id: this.confirmer2?.confirmerId,
        })
      );
      this.$router.push("/approve/update");
    },

    async deleteApprove() {
      if (confirm("정말로 이 결재를 회수  하시겠습니까?")) {
        try {
          const token = sessionStorage.getItem("token");
          await axios.delete(`http://192.168.0.51/api/approve/cancel/${this.id}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      });

          alert("결재가 성공적으로 회수되었습니다.");
          this.$router.push("/approve/list");
        } catch (error) {
          console.error("결재 삭제 중 오류 발생:", error);
          alert("결재 회수 중 오류가 발생했습니다.");
        }
      }
    },
  },

  created() {
    this.fetchApprove();
    this.fetchFiles();

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
.container {
  width: 800px;
  margin-top: 60px;
  display: 100%;

}
.title {
  font-size: 24px;
  font-weight: bold;
  margin-left: 70px;
}
.table {
  width: 100%;
  border-collapse: collapse;
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
.input-label {
  display: inline-block;
  width: 100px;
  text-align: center;
}
/* .contenttable {
  min-height: 400px;

} */
.contable {
  text-align: left; /* 텍스트를 왼쪽으로 정렬 */
  vertical-align: top; /* 콘텐츠를 셀의 상단으로 정렬 */
  min-height: 400px;
  width: 665px
  
}
.input-field {
  width: 200px;
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
.input-group {
  margin-bottom: 10px;
}

.input-group.approval {
  margin-top: 20px;
}
.approve-button {
  text-align: right;
  margin-top: 50px;
}
.approve {
  margin-bottom: 100px; /* 결재칸과 휴가신청서 사이에 공백 추가 */
}
.header {
  margin-top: 20px; /* 헤더 위쪽에 공백 추가 */
  background-color: white;
  text-align: center;
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
.vac {
  height: 500px;
}
</style>
