<template>
    <div>
      <HeaderComponent />
      <SideBar />

      <div class="page-content">
        <h1>초과 근무 목록</h1>
        <table>
          <thead>
            <tr>
              <th class="table-header">ID</th>
              <th class="table-header">Date</th>
              <th class="table-header">Shift</th>
              <th class="table-header">Start Time</th>
              <th class="table-header">End Time</th>
              <th class="table-header">Reason</th>
              <th class="table-header">Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="overtime in overtimeList" :key="overtime.id" @click="viewOvertimeDetail(overtime.id)">
              <td class="table-data">{{ overtime.id }}</td>
              <td class="table-data">{{ overtime.date }}</td>
              <td class="table-data">{{ overtime.shift }}</td>
              <td class="table-data">{{ overtime.startTime }}</td>
              <td class="table-data">{{ overtime.endTime }}</td>
              <td class="table-data">{{ overtime.reason }}</td>
              <td class="table-data">{{ overtime.status }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>

  import axios from 'axios';
  import SideBar from '../components/SideBar.vue';
  import HeaderComponent from '../components/HeaderComponent.vue';

  
  export default {
    name: 'OvertimeListPage',
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
      }
    }
  };
  </script>
  
  <style scoped>
  body {
    font-family: 'Roboto', sans-serif;
    background-color: #fff;
    color: #000;
    margin: 20px;
  }
  h1 {
    font-size: 30px;
    margin-left: 260px;
  }
  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 13px;
    margin-left: 9px;
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
  .table-data {
    cursor: pointer;
  }
  </style>
  