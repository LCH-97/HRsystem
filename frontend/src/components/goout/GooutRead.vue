<template>
  <div class="all">
    <div class="goout-read-page">
      <h1>휴가 세부 정보</h1>
      <br><br><br>
      <div v-if="goout && gooutLine">
        <h2>{{ goout.gooutTypeName }}</h2>
        <p>휴가결재 올린사람: {{ gooutLine?.employeeName }}</p>
        <p>휴가가는 직원: {{ goout.employeeName }}</p>
        <p>대리인: {{ goout.agentName }}</p>
        <p>시작 날짜: {{ goout.first }}</p>
        <p>종료 날짜: {{ goout.last }}</p>
        <p>휴가 사용일 수: {{ daysUsed }}일</p>
        <br>
        <p>결재자1 : {{ gooutLine?.confirmer1Name }}</p>
        <p>결재자2 : {{ gooutLine?.confirmer2Name }}</p>
        <p>상태: {{ getStatusText(goout.status) }}</p>
      </div>
      <div v-else>
        <p>정보를 불러오는 중...</p>
      </div>
    </div>
    <br><br>
      <div class="goout-button">
        <div class="confirm1-button" v-if="gooutLine?.confirmer1Id === loggedInUserId && goout?.status == 0">
        <!-- Show these buttons if the logged-in user is confirmer1 -->
          <button @click="confirm1">결재자1 결재</button>
          <button @click="reject1">결재자1 반려</button>
        </div>
        <!-- Show these buttons if the logged-in user is confirmer2 -->
        <div class="confirm1-button" v-else-if="gooutLine?.confirmer2Id === loggedInUserId && goout?.status == 1">
          <button @click="confirm2">결재자2 결재</button>
          <button @click="reject2">결재자2 반려</button>
        </div>
        <!-- Show these buttons if the logged-in user is the one who requested the leave -->
        <div class="confirm1-button" v-else-if="gooutLine?.employeeId === loggedInUserId">
          <button @click="updateGoout">수정</button>
          <button @click="deleteGoout">삭제</button>
        </div>
        <!-- If logged-in user's ID does not match any, do not show any buttons -->
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

export default {
  data() {
    return {
      goout: null,
      gooutLine: null,
      id: this.$route.params.id,
      backend: "http://localhost:8080", // 백엔드 서버 주소
    };
  },
  methods: {
    setLoggedInUser() {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decoded = jwtDecode(token); // Use the correct decoding method
      this.loggedInUserId = decoded.ID; // Adjust according to your token structure
    }
  },


    async confirm1() {
  if (confirm("결재하시겠습니까?")) {
    try {
      await axios.patch(`${this.backend}/gooutLine/confirm1`, {
        gooutId: this.id, // 휴가 ID
        confirmer1Id: this.gooutLine.confirmer1Id, // 결재자1 ID
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
        gooutId: this.id, // 휴가 ID
        confirmer2Id: this.gooutLine.confirmer2Id, // 결재자1 ID
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
        gooutId: this.id,
        confirmer1Id: this.gooutLine.confirmer1Id,
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
        gooutId: this.id,
        confirmer2Id: this.gooutLine.confirmer2Id,
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
      const gooutResponse = await axios.get(`http://localhost:8080/goout/check/${this.id}`);
      if (gooutResponse.data.isSuccess) {
        this.goout = gooutResponse.data.result;
        await this.fetchGooutLine(this.id); // 결재라인 정보 조회
      } else {
        alert("휴가 정보를 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error("휴가 정보를 불러오는 중 오류가 발생했습니다.", error);
    }
  },
    async fetchGooutLine(gooutId) {
    try {
      const response = await axios.get(`http://localhost:8080/gooutLine/2/${gooutId}`);
      if (response.data.isSuccess) {
        // Assuming response.data.result directly contains the GooutLineRead object
        this.gooutLine = response.data.result;
      } else {
        console.error("결재라인 정보를 불러오는데 실패했습니다.");
      }
    } catch (error) {
      console.error("결재라인 정보를 불러오는 중 오류가 발생했습니다.", error);
    }
  },
    getStatusText(status) {
      const statusMap = {
        '0': '대기중',
        '1': '결재자1 승인',
        '2': '최종 승인',
        '3': '반려',
      };
      return statusMap[status] || '알 수 없음';
    },
    updateGoout() {

      if (this.goout.status !== 3) {
    alert("반려상태가 아니면 수정을 할 수 없습니다. 없습니다.");
    return; // 메소드 실행을 중단
  }
  const gooutId = this.$route.params.id;
  localStorage.setItem('updateGooutInfo', JSON.stringify({ ...this.goout, id: gooutId }));
  this.$router.push('/goout/update');
},

      async deleteGoout() {
      if (confirm("정말로 이 휴가를 삭제하시겠습니까?")) {
        try {
          // First, delete the approval line associated with this vacation request
          await axios.delete(`http://localhost:8080/gooutLine/delete/${this.id}`);
          console.log("결재라인이 성공적으로 삭제되었습니다.");

          // After the approval line is successfully deleted, delete the vacation request
          await axios.delete(`http://localhost:8080/goout/delete/${this.id}`);
          alert("휴가가 성공적으로 삭제되었습니다.");
          this.$router.push("/goout/list"); // Redirect to the list of vacation requests
        } catch (error) {
          console.error("휴가 또는 결재라인 삭제 중 오류 발생:", error);
          alert("휴가 또는 결재라인 삭제 중 오류가 발생했습니다.");
        }
      }
    }
  },
  created() {
    this.fetchGoout();
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
    }
  }
};
</script>

<style scoped>
.all {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.goout-read-page {
  max-width: 600px;
  padding: 20px;
  text-align: center;
}

.goout-button {
  display: flex;
  margin-top: 20px;
  gap: 10px;
}

button {
  padding: 10px 20px;
  border: none;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
