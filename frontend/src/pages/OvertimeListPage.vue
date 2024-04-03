<template>
  <div>
    <HeaderComponent />
    <SideBar />

    <!-- <div class="col-xl-9">
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
</div> -->




<br>
<br>
<br>


<div class="container with-shadow">
      <h1>초과근무 목록</h1>
      <div class="datatable-container">
        <table id="datatablesSimple" class="datatable-table">
          <thead>
            <tr>
              <th>No.</th>
              <th>일자</th>
              <th>오전/오후</th>
              <th>시작 시간</th>
              <th>종료 시간</th>
              <th>사유</th>
              <th>승인여부</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="overtime in paginatedOvertimeList" :key="overtime.id" @click="viewOvertimeDetail(overtime.id)">
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
        <div class="pagination">
          <button @click="prevPage">&lt;</button>
          <span>{{ currentPage }}</span>
          <button @click="nextPage">&gt;</button>
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
      overtimeList: [],
      itemsPerPage: 6,
      currentPage: 1,
    };
  },
  computed: {
    paginatedOvertimeList() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.overtimeList.slice(startIndex, endIndex);
    },
    totalPages() {
      return Math.ceil(this.overtimeList.length / this.itemsPerPage);
    },
  },
  mounted() {
    this.fetchOvertimeList();
  },
  methods: {
    async fetchOvertimeList() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/employee/overtime/list", {
          headers: { Authorization: "Bearer " + token, },
        });
        this.overtimeList = response.data.result;
      } catch (error) {
        console.error("Error:", error);
      }
    },
    viewOvertimeDetail(id) {
      window.location.href = `/overtime/read/${id}`;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1254px;
  margin: 0 auto;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  position: relative;
  left: 113px;
}

.with-shadow {
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

h1 {
  font-size: 24px;
  margin-bottom: 32px;
  margin-left: 13px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.pagination button {
  cursor: pointer;
  padding: 8px 12px;
  border: none;
  background-color: #f0f0f0;
  border-radius: 5px;
  margin: 0 5px;
}

.pagination button:hover {
  background-color: #e0e0e0;
}

.pagination button:disabled {
  cursor: not-allowed;
  background-color: #ddd;
}
</style>