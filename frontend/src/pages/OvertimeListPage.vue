<template>
  <div>
    <HeaderComponent />
    <SideBar />
    
    <div class="col-xl-9">
    <div class="card mb-3">
      <div class="card-header">
        초과근무페이지
      </div>
      <div class="card-body">
        <div class="chartjs-size-monitor">
          <div class="chartjs-size-monitor-expand">


            <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">

              <div class="datatable-container">
                <table id="datatablesSimple" class="datatable-table">
                  <thead>
                    <tr>
                      <th data-sortable="true" style="width: 7.287833827893174%;"><a href="#"
                          class="datatable-sorter">No.</a></th>
                      <th data-sortable="true" style="width: 8.56379821958457%;"><a href="#"
                          class="datatable-sorter">일자</a></th>
                      <th data-sortable="true" style="width: 8.93570722057369%;"><a href="#"
                          class="datatable-sorter">오전/오후</a></th>
                      <th data-sortable="true" style="width: 8.605341246290802%;"><a href="#"
                          class="datatable-sorter">시작 시간</a></th>
                      <th data-sortable="true" style="width: 8.605341246290802%;"><a href="#"
                          class="datatable-sorter">종료 시간</a></th>
                      <th data-sortable="true" style="width: 12.265084075173096%;"><a href="#"
                          class="datatable-sorter">사유</a></th>
                      <th data-sortable="true" style="width: 8.265084075173096%;"><a href="#"
                          class="datatable-sorter">승인여부</a></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="overtime in overtimeList" :key="overtime.id" @click="viewOvertimeDetail(overtime.id)">
                      <td>{{ overtime.id }}</td>
                      <td>{{ overtime.date }}</td>
                      <td>{{ overtime.shift }}</td>
                      <td>{{ overtime.startTime }}</td>
                      <td>{{ overtime.endTime }}</td>
                      <td>{{ overtime.reason }}</td>
                      <td>{{ overtime.status }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>

            </div>
          </div>
          <div class="chartjs-size-monitor-shrink">
            <div class=""></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>



    <!-- <div class="page-content">
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
  </div> -->
  
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
          const token = sessionStorage.getItem("token");
          const response = await axios.get("http://192.168.0.51/api/employee/overtime/list",{
          headers: { "Content-Type": "multipart/form-data", Authorization: "Bearer " + token, },

        });
          this.overtimeList = response.data.result;
        } catch (error) {
          console.error("Error:", error);
        }
      },
      viewOvertimeDetail(id) {
        window.location.href = `/overtime/read/${id}`;
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

th,
td {
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


.col-xl-9{
  margin-left: 320px;
  margin-top: 50px;
}



</style>