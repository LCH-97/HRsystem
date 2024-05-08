<template>
  <HeaderComponent />

  <div class="sb-nav-fixed">
    <div id="layoutSidenav">
      <SideBar />
      <div id="layoutSidenav_content">
        <main>
          <div id="container-fluid px-4">
            <!-- <h1 class="mt-4">MAIN PAGE</h1> -->
            <div class="row" style="width: 100%">
              <div class="col-xl-6">
                <div class="card mb-4">
                  <div class="card-header">{{ new Date().toLocaleDateString() }}</div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <FullCalendar :options="calendarOptions" />
                      </div>
                      <div class="chartjs-size-monitor-shrink">
                        <div class=""></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xl-4">
                <div class="card mb-3">
                  <div class="card-header">
                    로그인 승인
                  </div>

                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="container">
                          <table class="table">
                            <thead>
                              <tr>
                                <th>사원번호</th>
                                <th>직원 이름</th>
                                <th>입사일</th>
                                <th>부서</th>
                                <th>직급</th>
                                <th>승인</th>
                              </tr>

                              <tr
                                v-for="employee in this.authorizeResult.result.slice(currentPage * pageSize, (currentPage + 1) * pageSize)"
                                v-bind:key="employee.id">
                                <td>{{ employee.id }}</td>
                                <td>{{ employee.name }}</td>
                                <td>{{ employee.employmentDate }}</td>
                                <td>{{ employee.department }}</td>
                                <td>{{ employee.position }}</td>
                                <td><button @click="authorize(employee.id)">
                                    승인
                                  </button></td>
                              </tr>
                            </thead>
                            <tbody id="noticeTableBody">
                              <!-- 서버에서 받아온 데이터로 테이블이 채워질 자리 -->
                            </tbody>
                          </table>

                          <div class="pagination">
                            <a href="#" class="prev" v-on:click="prev">&laquo; 이전</a>
                            <!-- 페이지 버튼은 자동으로 생성됩니다. -->
                            <a href="#" class="next" v-on:click="next">다음 &raquo;</a>
                          </div>
                        </div>
                      </div>
                      <div class="chartjs-size-monitor-shrink">
                        <div class=""></div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="card mb-3">
                  <div class="card-header"><a href="/salary/list">직원 월급 보러가기</a></div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                          <div class="datatable-container">
                            마지막 계산일 : {{ this.lastDate }}

                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xl-2">
                <div class="card mb-4">
                  <div class="card-header"><router-link to="/gooutType/list" class="nav-link">휴가 타입 관리</router-link></div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="card-header"><router-link to="/gooutType/list" class="nav-link">바로 가기</router-link></div>
                        
                      </div>
                      <div class="chartjs-size-monitor-shrink">
                        <div class=""></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="col-xl-2">
                <div class="card mb-4">
                  <div class="card-header"><router-link to="/overtimeapprovea" class="nav-link">초과근무 승인</router-link></div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="card-header"><router-link to="/overtimeapprovea" class="nav-link">초과근무 승인</router-link></div>
                        
                      </div>
                      <div class="chartjs-size-monitor-shrink">
                        <div class=""></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  </div>
</template>

<script>
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";
import axios from "axios";
// 달력
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";

export default {
  name: "ManagerPage",
  components: {
    SideBar,
    HeaderComponent,
    FullCalendar,
  },
  data() {
    return {
      // startTime: "0",
      // endTime: "0",
      // sumTime: "0",
      // isCommute: false,
      // isLeave: true,
      // commuteId: "",
      currentPage: 0,
      pageSize: 5,
      lastDate: "",
      authorizeResult: {
        result: [

          {
            "id": 0,
            "name": "",
            "employmentDate": "",
            "department": "",
            "position": "",
            "salary": ""
          },
        ]
      },

      // 공지사항
      salaryMonthsResult: {
        "firstDate": "2024-01-10",
        "lastDate": "2024-02-25"
      },
      salaryMonths: [],

      //달력 옵션
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: "dayGridMonth",
      },
    };
  },
  methods: {
    fetchSalaryData() {
      console.log("fetchSalaryData method start");
      const api = "http://www.lch-hr-api.kro.kr";
      // 요청하면 월급 처음 준 날하고, 마지막으로 준 날 반환됨.
      const token = sessionStorage.getItem("token");
      axios
        .get(
          api + "/manager/salary/months", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        }
        )
        .then((response) => {
          this.salaryMonthsResult = response.data.result;
          console.log(response.data.result);
          this.lastDate = this.salaryMonthsResult.lastDate;



          //   totalItems = response.data.total;
        })
        .catch((error) => {
          console.error("Error fetchSalaryData :", error);
          // 토큰  만료 예외처리
        });
      console.log("fetchSalaryData method end");
    },
    getAuthorizeList() {
      console.log("getAuthorizeList");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://www.lch-hr-api.kro.kr";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      axios
        .get(api + "/manager/authorize", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          console.log("Response:", response.data);
          this.authorizeResult = response.data;

        })
        .catch((error) => {
          console.error("Error getAuthorizeList:", error);
          throw new Error("Error getAuthorizeList");
        });
    },

    authorize(employeeId) {
      console.log("authorize");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://www.lch-hr-api.kro.kr";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      axios
        .patch(api + "/manager/authorize/" + employeeId, null, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          console.log("Response:", response.data);
          alert("승인 완료");

        })
        .catch((error) => {
          console.error("Error getAuthorizeList:", error);
          alert("승인 실패: 담당자에게 연락하세요");
        }).finally(() => {
          this.getAuthorizeList(); // 목록 초기화
        });
    },
    prev() {
      if (this.currentPage > 0)
        this.currentPage = this.currentPage - 1;
    },
    next() {
      if ((this.currentPage + 1) * this.pageSize < this.authorizeResult.result.length)
        this.currentPage = this.currentPage + 1;
    }

  },
  mounted() {
    // 출근한 상태인지 확인해야함.
    //   this.check();
    //   this.fetchNoticeData(1);
    try {
      this.getAuthorizeList();
      this.fetchSalaryData();
    } catch (error) {
      console.error("ManagerPage Init Fail", error);
    }




  },
};
</script>

<style scoped>
#commuteButton {
  border: none;
  outline: none;
  margin: 10px;
  font-size: 12px;
  background-color: #f7c231;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 80%;
  height: 35px;
}

#leaveButton {
  border: none;
  outline: none;
  margin: 10px;
  font-size: 12px;
  background-color: #f7c231;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 80%;
  height: 35px;
}

button:hover {
  background-color: #f7a129;
}

button:active {
  background-color: #ff8c00;
}

#commute-info {
  height: 100%;
  font-size: 12px;
  text-align: center;
  border: 2px solid #f0f0f0;
  max-width: 220px;
  margin: auto;
  border-radius: 10px;
  background-color: #f9f9f9;
  position: relative;
}

.time {
  font-size: 15px;
}

.profile-pic {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 2px solid #fff;
  position: absolute;
  top: 75px;
  left: 50%;
  transform: translateX(-50%);
}

.main-button-container {
  position: relative;
  padding: 0px;
  margin-top: 150px;
}

#sumTime {
  position: relative;
  color: gray;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
  margin: 0;
  padding: 0;
}

.container {
  max-width: 970px;
  margin: auto;
  padding: 8px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  overflow-x: auto;
  font-size: 15px;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 10px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.table th {
  background-color: #f2f2f2;
}

.table tr:last-child td {
  border-bottom: none;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.pagination a {
  display: inline-block;
  padding: 8px 16px;
  text-decoration: none;
  color: #333;
  border: 1px solid #ddd;
  margin-right: 5px;
}

.pagination a.active {
  background-color: #e29c41;
  color: white;
  border: 1px solid #e29c41;
}
</style>