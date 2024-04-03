<template>
  <!-- <HeaderComponent />
  <SideBar /> -->
  <div>
    <div class="approveUpdateBanner">
      <div class="approveUpdateBannerTxt">
        <h2>결재 수정하기</h2>
      </div>
    </div>
    <div class="ReqBox">
      <article class="ReqInputBox">
        <div class="ReqInput">
          <div class="form-group">
            <label for="content">내용:</label>
            <textarea
              id="content"
              class="form-control"
              rows="10"
              v-model="updateInfo.content"
              required
            ></textarea>
          </div>
          <p>결재자1</p>
          <select v-model="confirmer1Id">
            <option
              v-for="employee in employees"
              :key="employee.id"
              :value="employee.id"
            >
              {{ employee.name }}
            </option></select
          ><br />
          <p>결재자2</p>
          <select v-model="confirmer2Id">
            <option
              v-for="employee in employees"
              :key="employee.id"
              :value="employee.id"
            >
              {{ employee.name }}
            </option></select
          ><br />
        </div>
      </article>
      <button @click="updateApprove">수정</button><br /><br />
    </div>
  </div>
</template>

<script>
import axios from "axios";
// import HeaderComponent from "@/components/HeaderComponent.vue";
// import SideBar from "@/components/SideBar.vue";

export default {
  name: "ApproveUpdatePage",
  // components: {
  //   SideBar,
  //   HeaderComponent,
  // },
  data() {
    return {
      backend: "http://localhost:8080",
      updateInfo: {
        content: "",
      },
      confirmer1Id: "",
      confirmer2Id: "",
      employees: [],
    };
  },

  async created() {
    await this.loadUpdateInfo();
    await this.fetchEmployees();
  },

  methods: {
    loadUpdateInfo() {
      const storedInfo = localStorage.getItem("updateApproveInfo");
      if (storedInfo) {
        this.updateInfo = JSON.parse(storedInfo);
        // 불러온 정보를 바탕으로 각 필드의 초기값 설정
        this.content = this.updateInfo.content;
        this.confirmer1Id = this.updateInfo.confirmer1Id;
        this.confirmer2Id = this.updateInfo.confirmer2Id;
      } else {
        console.error("수정할 정보가 존재하지 않습니다.");
      }
    },

    async fetchEmployees() {
      try {
        const response = await axios.get(`${this.backend}/employee/list`);
        this.employees = response.data; // 백엔드 응답에 'result' 키가 없다면 이렇게 접근합니다.
      } catch (error) {
        console.error("직원 목록 로딩 실패:", error);
      }
    },

    async updateApprove() {

      if (this.confirmer1Id === this.confirmer2Id) {
        alert(
          "결재라인 수정 실패: 결재자1의 ID와 결재자2의 ID는 같을 수 없습니다."
        );
        return; // 메소드 실행을 중단
      }

      try {
        // 데이터 객체 동적 생성
        let updateData = { id: this.updateInfo.id };
        if (this.updateInfo.content) updateData.content = this.updateInfo.content; // content 추가

        console.log(updateData);
        // Approve 정보 업데이트 실행
        await axios.patch(`${this.backend}/approve/update`, updateData, {
          headers: {
            "Content-Type": "application/json",
          },
        });
        console.log("Approve 정보 수정 성공");

        // ApproveLine 업데이트를 위한 동적 데이터 객체 생성
        let approveLineUpdateReq = { approveId: this.updateInfo.id };
        if (this.confirmer1Id)
          approveLineUpdateReq.confirmer1Id = this.confirmer1Id;
        if (this.confirmer2Id)
          approveLineUpdateReq.confirmer2Id = this.confirmer2Id;
        console.log(approveLineUpdateReq);
        // ApproveLine 정보 업데이트 실행
        await axios.patch(
          `${this.backend}/approve/line/update`,
          approveLineUpdateReq,
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        console.log("ApproveLine 정보 수정 성공");

        alert("휴가 정보 및 결재라인 정보가 성공적으로 수정되었습니다.");
        this.$router.push("/approve/list"); // 수정 완료 후, 리스트 페이지로 리다이렉션
      } catch (error) {
        console.error("결재 정보 또는 결재라인 정보 수정 실패:", error);
        alert("결재 정보 또는 결재라인 정보 수정에 실패하였습니다.");
      }
    },
  },
};
</script>

<style scoped></style>
