<template>
  <div class="container">
    <table class="approve">
      <tr>
        <th>결재</th>
        <td>
          <div class="input-group" v-if="confirmer1 && confirmer1.status !== 4">
            <label class="input-label">결재자1 : </label>
            {{ confirmer1?.confirmerName }}
          </div>
          <div v-if="goout">
            <div class="input-group">
              {{ getStatusText(confirmer1?.status) }}
            </div>
          </div>
        </td>
        <td>
          <div class="input-group" v-if="confirmer2 && confirmer2.status !== 4">
            <label class="input-label">결재자2 : </label>
            {{ confirmer2?.confirmerName }}
          </div>
          <div v-if="goout">
            <div class="input-group">
              {{ getStatusText(confirmer2?.status) }}
            </div>
          </div>
        </td>
      </tr>
    </table>
    <div class="header">
      <div v-if="goout">
        <h1 class="title">휴가신청서</h1>
        <br />
        <table class="table">
          <tr v-if="confirmer1?.status === 3 || confirmer2?.status === 3">
            <th>반려 사유</th>
            <td :colspan="confirmer1?.status === 3 ? '2' : ''">
              {{ confirmer1?.status === 3 ? confirmer1.comment : confirmer2.comment }}
            </td>
          </tr>
          <tr>
            <th>첨부 파일</th>
            <td colspan="2">
              <div v-if="files.length > 0">
                <ul class="file-list">
                  <li v-for="file in files" :key="file.name">
                    <a :href="file.downloadUrl" target="_blank">{{
                        file.name
                      }}</a>
                  </li>
                </ul>
              </div>
              <div v-else>첨부 파일 없음</div>
            </td>
          </tr>
          <tr>
            <th>휴가결재 올린사람</th>
            <td>{{ goout.writerName }}</td>
          </tr>
          <tr>
            <th>휴가가는 직원</th>
            <td>{{ goout.employeeName }}</td>
          </tr>
          <tr class="dates">
            <th>대리인</th>
            <td>{{ goout.agentName }}</td>
          </tr>
          <tr>
            <th>기한</th>
            <td>
              <label class="input-label">시작 날짜 : </label>
              {{ goout.first }}
              <br />

              <label class="input-label">종료 날짜 : </label>
              {{ goout.last }}
            </td>
          </tr>
          <tr>
            <th>휴가 사용일 수</th>
            <td>{{ daysUsed }}일</td>
          </tr>
          <tr class="vac">
            <th>휴가 유형</th>
            <td>{{ goout.gooutTypeName }}</td>
          </tr>
        </table>
      </div>
    </div>
  </div>
  <div class="goout-button">
    <div
        class="confirm1-button"
        v-if="confirmer1?.confirmerId === loggedInUserId && goout?.status == 0"
    >
      <!-- Show these buttons if the logged-in user is confirmer1 -->
      <button @click="confirm1">결재자1 결재</button>
      <button @click="reject1">결재자1 반려</button>
    </div>
    <!-- Show these buttons if the logged-in user is confirmer2 -->
    <div
        class="confirm1-button"
        v-else-if="
        confirmer2?.confirmerId === loggedInUserId && goout?.status == 1
      "
    >
      <button @click="confirm2">결재자2 결재</button>
      <button @click="reject2">결재자2 반려</button>
    </div>
    <!-- Show these buttons if the logged-in user is the one who requested the leave -->
    <div
        class="confirm1-button"
        v-else-if="
        goout?.writerId === loggedInUserId ||
        goout?.employeeId === loggedInUserId
      "
    >
      <button @click="updateGoout">수정</button>
      <button @click="deleteGoout">등록취소</button>
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
      goout: null,
      gooutLine: null,
      id: this.$route.params.id,
      backend: "http://localhost:8080", // 백엔드 서버 주소
      confirmer1: null,
      confirmer2: null,
      files: [], // 파일 목록을 저장할 배열
    };
  },
  methods: {
    fetchFiles() {
      // 특정 휴가 결재 ID에 대한 파일 목록을 가져오도록 URL 수정
      axios
          .get(`${this.backend}/goout/files/${this.id}`)
          .then((response) => {
            this.files = response.data; // 파일 목록 업데이트
          })
          .catch((error) => {
            console.error("파일 목록을 가져오는 중 오류가 발생했습니다.", error);
          });
    },
    setLoggedInUser() {
      const token = sessionStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token); // Use the correct decoding method
        this.loggedInUserId = decoded.ID; // Adjust according to your token structure
      }
    },
    async confirm1() {
      if (confirm("결재하시겠습니까?")) {
        try {
          await axios.patch(`${this.backend}/gooutLine/confirm1`, {
            id: this.confirmer1.id,
            gooutId: this.id, // 휴가 ID
            confirmerId: this.confirmer1.confirmerId, // 결재자1 ID
            comment: "결재자1 승인", // 코멘트
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");
          await this.returnGooutStatus(1);
          this.$router.push(`/goout/read/` + this.$route.params.id).then(() => {
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
          await axios.patch(`${this.backend}/gooutLine/confirm2`, {
            id: this.confirmer2.id,
            gooutId: this.id, // 휴가 ID
            confirmerId: this.confirmer2.confirmerId, // 결재자1 ID
            comment: "결재자2 승인", // 코멘트
          });
          console.log("결재라인이 성공적으로 승인되었습니다.");
          await this.returnGooutStatus(2);
          this.$router.push(`/goout/read/` + this.$route.params.id).then(() => {
            this.$router.go(0);
          });
        } catch (error) {
          console.error("결재자2 결재 처리 중 오류가 발생했습니다:", error);
          alert("결재자2 결재 처리에 실패했습니다.");
        }
      }
    },
    async reject1() {
      // 반려 사유 입력받기
      const reason = prompt("반려 사유를 입력해주세요.");
      if (reason !== null && reason.trim() !== "") {
        try {
          await axios.patch(`${this.backend}/gooutLine/reject1`, {
            id: this.confirmer1.id,
            gooutId: this.id,
            confirmerId: this.confirmer1.confirmerId,
            comment: reason, // 사용자 입력 반려 사유 사용
          });
          console.log("결재라인이 성공적으로 반려되었습니다.");
          await this.returnGooutStatus(3);
          this.$router.push(`/goout/read/` + this.$route.params.id).then(() => {
            this.$router.go(0);
          });
        } catch (error) {
          console.error("결재자1 반려 처리 중 오류가 발생했습니다:", error);
          alert("결재자1 반려 처리에 실패했습니다.");
        }
      } else {
        alert("반려 사유를 입력해주세요.");
      }
    },
    async reject2() {
      // 반려 사유 입력받기
      const reason = prompt("반려 사유를 입력해주세요.");
      if (reason !== null && reason.trim() !== "") {
        try {
          await axios.patch(`${this.backend}/gooutLine/reject2`, {
            id: this.confirmer2.id,
            gooutId: this.id,
            confirmerId: this.confirmer2.confirmerId,
            comment: reason, // 사용자 입력 반려 사유 사용
          });
          console.log("결재라인이 성공적으로 반려되었습니다.");
          await this.returnGooutStatus(3);
          this.$router.push(`/goout/read/` + this.$route.params.id).then(() => {
            this.$router.go(0);
          });
        } catch (error) {
          console.error("결재자2 반려 처리 중 오류가 발생했습니다:", error);
          alert("결재자2 반려 처리에 실패했습니다.");
        }
      } else {
        alert("반려 사유를 입력해주세요.");
      }
    },
    async returnGooutStatus(status) {
      try {
        const payload = {
          id: this.id, // 현재 휴가/외출의 ID
          status: status, // 변경할 상태
        };
        // 수정된 객체를 사용하여 백엔드에 요청
        await axios.patch(`${this.backend}/goout/return`, payload);
        alert("휴가/외출 정보의 상태 업데이트가 성공적으로 처리되었습니다.");
      } catch (error) {
        console.error("휴가/외출 정보의 상태 업데이트에 실패했습니다:", error);
      }
    },
    async fetchGoout() {
      try {
        const gooutResponse = await axios.get(
            `${this.backend}/goout/check/${this.id}`
        );
        if (gooutResponse.data.isSuccess) {
          this.goout = gooutResponse.data.result;
          await this.fetchGooutLine(this.id); // 결재라인 정보 조회
          this.fetchFiles(); // 휴가 정보를 성공적으로 가져온 후 파일 목록 요청
        } else {
          alert("휴가 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error("휴가 정보를 불러오는 중 오류가 발생했습니다.", error);
      }
    },
    async fetchGooutLine(gooutId) {
      try {
        const response = await axios.get(
            `http://localhost:8080/gooutLine/2/${gooutId}`
        );
        if (response.data.isSuccess && response.data.result.length >= 2) {
          // 첫 번째와 두 번째 결재자 정보 분리하여 저장
          this.confirmer1 = response.data.result[0];
          this.confirmer2 = response.data.result[1];
        } else {
          console.error("결재라인 정보를 불러오는데 실패했습니다.");
        }
      } catch (error) {
        console.error(
            "결재라인 정보를 불러오는 중 오류가 발생했습니다.",
            error
        );
      }
    },
    getStatusText(status) {
      const statusMap = {
        0: "상태 : 대기중",
        1: "상태 : 승인",
        2: "상태 : 승인",
        3: "상태 : 반려",
        4: "등록 취소",
      };
      return statusMap[status] || "알 수 없음";
    },
    updateGoout() {
      if (this.goout.status !== 3) {
        alert("반려상태가 아니면 수정할 수 없습니다.");
        return;
      }
      const gooutId = this.$route.params.id;
      localStorage.setItem(
          "updateGooutInfo",
          JSON.stringify({
            gooutTypeId: this.goout.gooutTypeId, // Presuming gooutTypeId is already there
            employeeId: this.goout.employeeId,
            agentId: this.goout.agentId,
            first: this.goout.first,
            last: this.goout.last,
            id: gooutId,
          })
      );
      localStorage.setItem(
          "updateGooutLineInfo",
          JSON.stringify({
            confirmer1Id: this.confirmer1?.confirmerId,
            confirmer2Id: this.confirmer2?.confirmerId,
          })
      );
      this.$router.push("/goout/update");
    },
    async deleteGoout() {
      if (confirm("휴가 등록을 취소하시겠습니까?")) {
        try {
          await axios.patch(`${this.backend}/goout/cancel/${this.id}`, {});
          console.log("휴가 등록을 취소하였습니다.");
          alert("휴가 등록을 취소하였습니다.");
          this.$router.push(`/goout/list`);
        } catch (error) {
          console.error("휴가 등록 취소 중 오류가 발생했습니다:", error);
          alert("휴가 등록 취소 중 오류가 발생했습니다.");
        }
      }
    },
  },
  created() {
    this.fetchGoout();
    this.fetchFiles();
  },
  mounted() {
    this.setLoggedInUser();
  },
  computed: {
    // 휴가 사용일 수를 계산하는 계산된 속성
    daysUsed() {
      if (this.goout && this.goout.first && this.goout.last) {
        const firstDate = new Date(this.goout.first);
        const lastDate = new Date(this.goout.last);
        const difference = lastDate - firstDate;
        const days = difference / (1000 * 3600 * 24);
        return days + 1; // 포함되는 첫날을 계산에 추가
      }
      return 0; // 또는 적절한 기본값
    },
  },
};
</script>

<style scoped>
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
