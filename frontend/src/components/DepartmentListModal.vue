<template>
      <div class="modal" v-if="isVisible">
      <div class="modal-content">
        <h3>부서 선택</h3>
        <select v-model="selectedDepartment">
          <option v-for="department in departments" :value="department.id" :key="department.id">
            {{ department.departmentName }}
          </option>
        </select>
        <h3>직원 선택</h3>
        <select v-if="selectedEmployees.length > 0" v-model="selectedEmployeeId">
          <option v-for="employee in selectedEmployees" :value="employee.id" :key="employee.id">
            {{ employee.name }}
          </option>
        </select>
        <button @click="confirmSelection">선택 완료</button>
        <button @click="closeModal">닫기</button>
      </div>
    </div>
  </template>
  
  <script>
import axios from "axios";
  export default {
    props: {
      departments: Array,
      isVisible: Boolean,
      backend: String, // backend prop 추가
    },
      data() {
      return {
        selectedDepartment: null,
        selectedEmployeeId: "",
        selectedEmployees: [],
      };
    },
    watch: {
      selectedDepartment() {
        this.fetchEmployeesByDepartment();
      },
    },
    methods: {
      fetchEmployeesByDepartment() {
        // 인자로 departmentId를 받도록 수정
        if (!this.selectedDepartment) return; // selectedDepartment가 null이면 요청을 보내지 않음
        axios.get(`${this.backend}/employee/department/${this.selectedDepartment}`).then((response) => {
          this.selectedEmployees = response.data;
        }).catch(error => {
          console.error("직원 목록 조회 실패:", error);
        });
      },
      confirmSelection() {
        this.$emit('confirm', this.selectedEmployeeId);
        this.closeModal();
      },
      closeModal() {
        this.$emit('update:isVisible', false);
      },
    },
  };
  </script>
  <style scoped>
.modal {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 배경을 살짝 어둡게 처리 */
  z-index: 2; /* 필요에 따라 조정 */
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  width: 50%; /* 모달 창의 너비, 필요에 따라 조정 */
  max-width: 600px; /* 최대 너비, 필요에 따라 조정 */
  z-index: 3; /* 모달 컨텐츠가 배경 위에 올라오도록 설정 */
}
</style>