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
                  <div class="card-header">Area Chart Example</div>
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
                    <a href="/board"> 공지사항 </a>
                  </div>

                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="container">
                          <table class="table">
                            <thead>
                              <tr>
                                <th>제목</th>

                                <th>작성자</th>
                                <th>날짜</th>
                              </tr>

                              <tr v-for="notice in this.notices" v-bind:key="notice.id">
                                <td>{{ notice.title }}</td>
                                <td>{{ notice.name }}</td>
                                <td>{{ notice.date }}</td>
                              </tr>
                            </thead>
                            <tbody id="noticeTableBody">
                              <!-- 서버에서 받아온 데이터로 테이블이 채워질 자리 -->
                            </tbody>
                          </table>

                          <div class="pagination">
                            <a href="#" class="prev">&laquo; 이전</a>
                            <!-- 페이지 버튼은 자동으로 생성됩니다. -->
                            <a href="#" class="next">다음 &raquo;</a>
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
                  <div class="card-header">Bar Chart Example</div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                          <div class="datatable-container">
                            <table id="datatablesSimple" class="datatable-table">
                              <thead>
                                <tr>
                                  <th data-sortable="true" style="width: 19.287833827893174%">
                                    <a href="#" class="datatable-sorter">Name</a>
                                  </th>
                                  <th data-sortable="true" style="width: 30.56379821958457%">
                                    <a href="#" class="datatable-sorter">Position</a>
                                  </th>
                                  <th data-sortable="true" style="width: 14.93570722057369%">
                                    <a href="#" class="datatable-sorter">Office</a>
                                  </th>
                                  <th data-sortable="true" style="width: 8.605341246290802%">
                                    <a href="#" class="datatable-sorter">Age</a>
                                  </th>
                                  <th data-sortable="true" style="width: 14.342235410484669%">
                                    <a href="#" class="datatable-sorter">Start date</a>
                                  </th>
                                  <th data-sortable="true" style="width: 12.265084075173096%">
                                    <a href="#" class="datatable-sorter">Salary</a>
                                  </th>
                                </tr>
                              </thead>
                              <tbody>
                                <tr data-index="0">
                                  <td>Tiger Nixon</td>
                                  <td>System Architect</td>
                                  <td>Edinburgh</td>
                                  <td>61</td>
                                  <td>2011/04/25</td>
                                  <td>$320,800</td>
                                </tr>
                                <tr data-index="1">
                                  <td>Garrett Winters</td>
                                  <td>Accountant</td>
                                  <td>Tokyo</td>
                                  <td>63</td>
                                  <td>2011/07/25</td>
                                  <td>$170,750</td>
                                </tr>
                                <tr data-index="2">
                                  <td>Ashton Cox</td>
                                  <td>Junior Technical Author</td>
                                  <td>San Francisco</td>
                                  <td>66</td>
                                  <td>2009/01/12</td>
                                  <td>$86,000</td>
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
              <div class="col-xl-2">
                <div class="card mb-4">
                  <div class="card-header">Bar Chart Example</div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div id="commute-info">
                          <h2>안녕하세요</h2>
                          <h2>{{ this.$route.query.name }}</h2>
                          <img class="profile-pic"
                            src="https://png.pngtree.com/png-clipart/20191121/original/pngtree-user-vector-icon-png-image_5152508.jpg"
                            alt="Profile Picture" />

                          <!-- 나중에는 여기 직원 이름이 오도록 -->

                          <div class="main-button-container" v-show="!isCommute">
                            <button id="commuteButton" v-show="!isLoading" @click="commute">
                              출근
                            </button>
                          </div>

                          <div class="main-button-container" v-show="isCommute && !isLeave">
                            <button id="leaveButton" v-show="!isLoading" @click="leave">
                              퇴근 {{ this.commuteId }}
                            </button>
                          </div>

                          <div class="main-button-container" v-show="isCommute && isLeave">
                            <button id="leaveButton">안전하게 퇴근하세요</button>
                          </div>

                          <div id="startTime" class="time">근무 시작</div>
                          <div>{{ this.startTime }}</div>
                          <div id="endTime" class="time">근무 종료</div>
                          <div>{{ this.endTime }}</div>
                          <div id="sumTime" class="time">총 업무시간</div>
                          <div>{{ this.sumTime }}</div>
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
          </div>
        </main>
      </div>
    </div>
  </div>
  <LoadingPage v-if="isLoading" />
</template>

<script>
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";
import LoadingPage from "@/components/LoadingPage.vue";

import axios from "axios";
// 달력
import FullCalendar from "@fullcalendar/vue3";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";

export default {
  name: "MainPage",
  components: {
    SideBar,
    HeaderComponent,
    FullCalendar,
    LoadingPage,
  },
  data() {
    return {
      startTime: "0",
      endTime: "0",
      sumTime: "0",
      isCommute: false,
      isLeave: true,
      commuteId: "",
      isLoading: false,
      // 공지사항
      notices: "",

      //달력 옵션
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: "dayGridMonth",
      },
    };
  },
  methods: {
    async commute() {
      console.log("click");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://192.168.0.51/api";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      let response = await axios.post(api + "/employee/commute", null, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      }).then(() => {
        console.log("대기 중");
        this.isLoading = true;
      })
        .catch(error => {
          console.error('Error post commute data:', error);
          alert("출근 실패");
          // if (error.response.data.code === "USER-004") {
          //   this.popTitle = "로그인에 실패하였습니다.";
          //   this.popText = "다시 시도해주세요.";
          // } else if (error.response.data.code === "EMPLOYEE-001") {
          //   this.popTitle = "미승인 계정입니다.";
          //   this.popText = "인사담당자에게 연락하세요.";
          // } else {
          //   this.popTitle = "예상하지 못한 에러입니다. ";
          //   this.popText = "서버 관리자에게 연락하세요.";
          // }
          // this.popUpStatus = true;
        });

      console.log("Response:", response.data);

      // this.responseData = response.data;
      this.startTime = response.data.result.startTime;
      this.commuteId = response.data.result.id;
      this.isCommute = true;
      this.isLeave = false;

      this.isLoading = false;
    },
    async leave() {
      console.log(" leave click");
      // const api = process.env.VUE_APP_BACKEND_URL;
      const api = "http://192.168.0.51/api";
      console.log(api);
      // let formData = new FormData();
      // formData.append('username', this.username);
      // formData.append('password', this.password);
      const token = sessionStorage.getItem("token");
      let response = await axios.patch(api + "/employee/leave/" + this.commuteId, null, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
        .then(() => {
          console.log("대기 중");
          this.isLoading = true;
          // this.responseData = response.data;

        })
        .catch((error) => {
          console.error("Error updating data:", error);
          alert("퇴근 실패");
        });
      console.log("Resposne:"+response);
      this.endTime = response.data.result.endTime;
      this.sumTime = response.data.result.sumTime;
      this.isLeave = true;

      this.isLoading = false;
    },
    async check() {
      console.log("check START");
      const api = "http://192.168.0.51/api";
      console.log(api);
      const token = sessionStorage.getItem("token");
      let response = await axios
        .get(api + "/employee/commute/check", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then(() => {
          console.log("Chcek Loaing...");
          // this.responseData = response.data;
          this.isLoading = true;

          //페이지 구성에 필요한 걸 다 가져와야한다.
        })
        .catch((error) => {
          console.error("Error check data:", error);
          alert("출근 정보 가져오기 실패");
        });
      this.isCommute = response.data.result.isCommute;
      this.isLeave = response.data.result.isLeave;
      if (this.isCommute) {
        this.commuteId = response.data.result.id;
        this.startTime = response.data.result.startTime;
      }
      if (this.isLeave) {
        this.endTime = response.data.result.endTime;
        this.sumTime = response.data.result.sumTime;
      }
      this.isLoading = false;
    },

    async fetchNoticeData(page) {
      console.log("fetchNoticeData START");
      const itemsPerPage = 6;
      let response = await axios.get(`http://192.168.0.51/api/board/check?page=${page}&perPage=${itemsPerPage}`)
        .then(() => {
          console.log("Loading fetchNoticeData");
          this.isLoading = true;
          //   totalItems = response.data.total;
        })
        .catch((error) => {
          console.error("Error fetching notice data:", error);
          alert("공지사항 불러오기 실패");
        });
      this.notices = response.data.result;
      console.log("fetchNoticeData END");

      this.isLoading = false;
    },
  },
  mounted() {
    // 출근한 상태인지 확인해야함.
    try {
      this.check();
      this.fetchNoticeData(1);
    } catch (error) {
      console.log("init fail:"+error);
    }finally{
      this.isLoading = false;
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
  font-size: 10px;
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
