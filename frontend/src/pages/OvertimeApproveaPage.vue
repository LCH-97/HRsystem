<template>
    <div>

        <HeaderComponent />
      <SideBar />


      <h1>초과 근무 목록</h1>
      <table>
        <thead>
          <tr>
            <th class="table-header">ID</th>
            <th class="table-header">날짜</th>
            <th class="table-header">근무 시간</th>
            <th class="table-header">시작 시간</th>
            <th class="table-header">종료 시간</th>
            <th class="table-header">사유</th>
            <th class="table-header">상태</th>
            <th class="table-header">승인</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="overtime in overtimeList" :key="overtime.id">
            <td class="table-data" @click="viewOvertimeDetail(overtime.id)">{{ overtime.id }}</td>
            <td class="table-data">{{ overtime.date }}</td>
            <td class="table-data">{{ overtime.shift }}</td>
            <td class="table-data">{{ overtime.startTime }}</td>
            <td class="table-data">{{ overtime.endTime }}</td>
            <td class="table-data">{{ overtime.reason }}</td>
            <td class="table-data">{{ overtime.status }}</td>
            <td class="table-data">
              <button v-if="overtime.status === '대기 중'" class="approval-button" @click="approveOvertime(overtime.id)">승인</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  import SideBar from '../components/SideBar.vue';
  import HeaderComponent from '../components/HeaderComponent.vue';
  
  export default {
    name: 'OvertimeApproveaPage',
    components: {
        SideBar,
      HeaderComponent,
    },
    data() {
      return {
        overtimeList: []
      };
    },
    mounted() {
      this.fetchOvertimeList();
    },
    methods: {
      async fetchOvertimeList() {
        try {
          const response = await axios.get("http://localhost:8080/employee/overtime/list");
          this.overtimeList = response.data.result;
        } catch (error) {
          console.error("Error:", error);
        }
      },
      viewOvertimeDetail(id) {
        window.location.href = `http://localhost:8080/employee/overtime/read/${id}`;
      },
      async approveOvertime(id) {
        try {
          await axios.patch(`http://localhost:8080/employee/overtime/approve/${id}`);
          this.fetchOvertimeList(); // 목록 다시 불러오기
        } catch (error) {
          console.error("Error:", error);
        }
      }
    }
  };
  </script>
  
  <style scoped>
  h1 {
    font-size: 34px;
    margin-left: 267px;
    margin-top: 51px;
    font-family: 'Roboto', sans-serif;
  }
  body {
    font-family: 'Roboto', sans-serif;
    background-color: #fff;
    color: #000;
    margin: 20px;
  }
  table {
    width: 80%;
    border-collapse: collapse;
    margin-top: 47px;
    margin-left: 261px;
  }
  th, td {
    padding: 10px;
    text-align: left;
    border-bottom: 1px solid #ddd;
    background-color: #fff;
  }
  th {
    background-color: #fff;
    color: #333;
    border-bottom: 2px solid #333;
  }
  tr:last-child td {
    border-bottom: none;
  }
  .approval-button {
    padding: 5px 10px;
    background-color: #ef9d39;
    border: none;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    margin: 2px 2px;
    cursor: pointer;
    border-radius: 4px;
  }
  </style>
  