<template>
  <div class="modal" v-if="isVisible">
    <div class="modal-content">
      <div class="selection-container">
        <div class="department-selection">
          <h3>부서 선택</h3>
          <ul>
            <li v-for="department in departments" :key="department.id" @click="selectDepartment(department.id)">
              {{ department.departmentName }}
            </li>
          </ul>
        </div>
        <div class="employee-selection" v-if="selectedDepartment">
          <h3>직원 선택</h3>
          <ul>
            <li v-for="employee in selectedEmployees" :key="employee.id" @click="confirmSelection(employee.id, employee.name)">
              {{ employee.name }} ({{ employee.username }})
            </li>
          </ul>
        </div>
      </div>
      <button @click="$emit('close')">닫기</button>
    </div>
  </div>
</template>

  
<script>
import axios from "axios";
export default {
  props: {
    departments: Array,
    isVisible: Boolean,
    backend: String,
  },
  data() {
    return {
      selectedDepartment: null,
      selectedEmployeeId: "",
      selectedEmployees: [],
    };
  },
  methods: {
    selectDepartment(departmentId) {
      this.selectedDepartment = departmentId;
      this.fetchEmployeesByDepartment(departmentId);
    },
    fetchEmployeesByDepartment(departmentId) {
      const token = sessionStorage.getItem("token");
      axios.get(`${this.backend}/employee/department/${departmentId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      }).then((response) => {
        this.selectedEmployees = response.data;
      }).catch(error => {
        console.error("직원 목록 조회 실패:", error);
      });
    },
    confirmSelection(id, name) {
      this.$emit('confirm', { id, name });
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
      background-color: rgba(0, 0, 0, 0.65); /* 배경을 살짝 어둡게 처리 */
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
      max-height: 500px; /* 모달 컨텐츠의 최대 높이 */
      overflow-y: auto; /* 컨텐츠가 높이를 초과할 경우 스크롤 생성 */
    }
    .selection-container {
  display: flex;
}

.department-selection, .employee-selection {
  flex: 1;
  margin-right: 10px;
  min-height: 400px;
  max-height: 400px; /* 선택 리스트의 최대 높이 */
  overflow-y: auto; /* 내용이 최대 높이를 초과할 경우 스크롤바 생성 */
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  cursor: pointer;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

li:hover {
  background-color: #eee;
}
</style>