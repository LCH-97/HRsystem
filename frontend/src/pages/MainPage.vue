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
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand1" style="margin-top: 28px; direction: ltr; text-align: left; display: flex; flex-direction: column; justify-content: center; gap: 33px;">
                        <FullCalendar :options="calendarOptions" style="margin-top: 30px; direction: ltr; text-align: left; display: flex; flex-direction: column; justify-content: center; gap: 33px;" />
                      </div>
                      
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-xl-4">
                <div class="card mb-5">
                  <div class="card-header2">결재 현황</div>
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div class="approval-status-container">
                          <div class="status-item">
                            <div class="status-number">{{ pendingApprovals }}</div>
                            <div class="status-label">대기중</div>
                          </div>
                          <div class="status-item">
                            <div class="status-number">{{ inProgressApprovals }}</div>
                            <div class="status-label">결재중</div>
                          </div>
                          <div class="status-item">
                            <div class="status-number">{{ returnedApprovals }}</div>
                            <div class="status-label">반려</div>
                          </div>
                          <div class="status-item">
                            <div class="status-number">{{ withdrawnApprovals }}</div>
                            <div class="status-label">회수</div>
                          </div>
                          <div class="status-item">
                            <div class="status-number">{{ completedApprovals }}</div>
                            <div class="status-label">결재 완료</div>
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
                  <a1 href="" style="position: absolute; top: 8px; left: 20px;"> 공지사항 </a1>
                  <a href="/board/list" style="position: relative; left: 309px; top: 30px; "> + 더보기 </a>
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
                            </thead>
                            <tbody id="boardTableBody">
                              <tr v-for="board in boards" :key="board.id">
                                <td>{{ board.title }}</td>
                                <td>{{ board.writerid }}</td>
                                <td>{{ board.createAt }}</td>
                              </tr>
                              
                            </tbody>
                          </table>

                          <div class="pagination">
                            
                          </div>
                          <div id="boardsIsNull" v-if="boards.length == 0"> 공지사항이 존재하지 않습니다.</div>
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
                  <div class="card-body">
                    <div class="chartjs-size-monitor">
                      <div class="chartjs-size-monitor-expand">
                        <div id="commute-info">
                          <h2 style="font-size: 27px; position: relative; top: 85px;">안녕하세요</h2>
                          <h2 style="font-size: 26px; position: relative; top: 102px;">{{ this.name }}님</h2>
                          <img class="profile-pic" src="https://png.pngtree.com/png-clipart/20191121/original/pngtree-user-vector-icon-png-image_5152508.jpg" alt="Profile Picture" />

                          <!-- 나중에는 여기 직원 이름이 오도록 -->

                          <div class="main-button-container" v-show="!isCommute">
                            <button id="commuteButton" @click="commute">
                              출근
                            </button>
                          </div>

                          <div class="main-button-container" v-show="isCommute && !isLeave">
                            <button id="leaveButton" @click="leave">
                              퇴근
                            </button>
                          </div>

                          <div class="main-button-container" v-show="isCommute && isLeave">
                            <button id="leaveButton">퇴근하세요.</button>
                          </div>

                          <div id="startTime" class="time">근무 시작</div>
                          <div>{{ startTime }}</div>
                          <div id="endTime" class="time">근무 종료</div>
                          <div>{{ endTime }}</div>
                          <div id="sumTime" class="time">총 업무시간</div>
                          <div>{{ sumTime }}</div>
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
  <LoadingPage v-if="isLoading" @close-event="close" :-title="loadingTitle" :-text="loadingText"/>
</template>

<script>
import SideBar from "../components/SideBar.vue";
import HeaderComponent from "../components/HeaderComponent.vue";
import axios from "axios";
import LoadingPage from "@/components/LoadingPage.vue";
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
      name:"",
      startTime: "0",
      endTime: "0",
      sumTime: "0",
      isCommute: false,
      isLeave: true,
      isLoading: false,
      loadingTitle: "",
      loadingText: "",
      commuteId: "",
      newTodo: '',
      todos: [],
      // 공지사항
      boards: [],
      // 결재 현황 데이터
      pendingApprovals: 0,
      inProgressApprovals: 0,
      returnedApprovals: 0,
      withdrawnApprovals: 0,
      completedApprovals: 0,
      //달력 옵션
      calendarOptions: {
        plugins: [dayGridPlugin, interactionPlugin],
        initialView: "dayGridMonth",
      },
    };
  },
  methods: {
    splitDaltaTime(daltaTime){
      return daltaTime.split('T')[1];
    },
    commute() {
      console.log("click commute button");
      if(this.isLoading) return;
      this.isLoading = true;
      this.loadingTitle = "출근 중"
      this.loadingText = "잠시만 기다려주세요."
      const api = "http://192.168.0.51/api";
      const token = sessionStorage.getItem("token");
      axios
        .post(api + "/employee/commute", null, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          console.log("Response:", response.data);
          this.startTime = this.splitDaltaTime(response.data.result ? response.data.result.startTime : '0T0') ;
          this.commuteId = response.data.result ? response.data.result.id : '';
          this.isCommute = true;
          this.isLeave = false;
        })
        .catch((error) => {
          console.error("Error updating data:", error);
        }).finally(()=>{this.isLoading = false;});
    },
    leave() {
      console.log(" leave click");
      if(this.isLoading) return;
      this.isLoading = true;
      const api = "http://192.168.0.51/api";
      const token = sessionStorage.getItem("token");
      axios
        .patch(api + "/employee/leave/" + this.commuteId, null, {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          console.log("Response:", response.data);
          this.endTime = this.splitDaltaTime(response.data.result ? response.data.result.endTime : '0');
          this.sumTime = response.data.result ? response.data.result.sumTime : '0';
          this.isLeave = true;
        })
        .catch((error) => {
          console.error("Error leave:", error);
          alert("퇴근 실패");
        }).finally(()=>{this.isLoading = false;});
    },
    
    check() {
      console.log("check");
      if(this.isLoading) return;
      this.isLoading = true;
      this.loadingTitle = "출근 확인 중"
      this.loadingText = "잠시만 기다려주세요."
      const api = "http://192.168.0.51/api";
      const token = sessionStorage.getItem("token");
      axios
        .get(api + "/employee/commute/check", {
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
          },
        })
        .then((response) => {
          console.log("Chcek Response:", response.data);
          this.isCommute = response.data.result ? response.data.result.isCommute : false;
          this.isLeave = response.data.result ? response.data.result.isLeave : true;
          this.commuteId =  response.data.result.commuteId;
          if (this.isCommute) {
            this.commuteId = response.data.result.id;
            this.startTime = this.splitDaltaTime(response?.data?.result?.startTime || '0T0') ;
          }
          if (this.isLeave) {
            this.endTime = this.splitDaltaTime(response.data.result.endTime) ;
            this.sumTime = response.data.result.sumTime;
          }
        })
        .catch((error) => {
          console.error("Error updating data:", error);
        }).finally(()=>{this.isLoading = false;});
    },
    
    fetchBoardData(page) {
      console.log("qweqwe");
      const itemsPerPage = 6;
      axios
        .get(`http://192.168.0.51/api/board/check?page=${page}&perPage=${itemsPerPage}`)
        .then((response) => {
          // 백엔드에서 전달된 데이터 중 게시글 목록만 추출하여 할당
          this.boards = response.data.result.boards;
          console.log(this.boards); // 게시글 데이터가 올바르게 할당되었는지 콘솔에 출력
        })
        .catch((error) => {
          console.error("Error fetching board data:", error);
        });
    },
    
    fetchApprovalData() {
      // 실제로는 서버에서 데이터를 가져와야 합니다.
      // 예시 데이터로 임시로 값을 할당합니다.
      this.pendingApprovals = 5;
      this.inProgressApprovals = 3;
      this.returnedApprovals = 1;
      this.withdrawnApprovals = 2;
      this.completedApprovals = 10;
    },
  },
  mounted() {
    // 출근한 상태인지 확인
    this.name= sessionStorage.getItem("name");
    this.check();
    this.fetchBoardData(1);
    // 결재 현황 데이터 가져오기
    this.fetchApprovalData();
  },
};
</script>


<style scoped>
/* 나머지 스타일은 여기에 추가 */
#boardsIsNull{
  text-align: center;
  font-size: larger;
  margin-top: -130px;
}

.approval-status-container {
  display: flex;
  margin-top: 45px;
  justify-content: space-between;
}

.status-item {
  text-align: center;
}

.status-number {
  font-size: 24px;
  font-weight: bold;
}

.status-label {
  font-size: 12px;
  color: gray;
}

.fc .fc-toolbar.fc-header-toolbar {
  margin-bottom: 2.5em;
}

#commuteButton {
  border: none;
  outline: none;
  margin: 15px;
  font-size: 17px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 81%;
  height: 27px;
}

#commuteButton:hover {
  background-color: #f7a129;
  
}

#leaveButton {
  border: none;
  outline: none;
  margin: 10px;
  font-size: 12px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 80%;
  height: 35px;
}

#leaveButton:hover {
  background-color: #f7a129;

}

button:hover {
  background-color: #f7a129;
}

button:active {
  background-color: #ff8c00;
}

#commute-info {
  height: 591px;
  font-size: 12px;
  text-align: center;
  border: 2px solid #fdfbfb;
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
  height: 99px;
  border-radius: 50%;
  border: 2px solid #fff;
  position: absolute;
  top: 234px;
  left: 50%;
  transform: translateX(-50%);
}

.main-button-container {
  position: relative;
  padding: 0px;
  margin-top: 286px;
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
  padding: 6px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  overflow-x: auto;
  font-size: 10px;
  margin-top: 24px;
  height: 354px;
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
  margin-top: 300px;
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

.chartjs-size-monitor-expand1 {
  margin-top: 1px;
  direction: ltr;
  text-align: left;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 33px;
  margin-top: 4px;
}

.card-header2 {
  position: relative;
  left: 18px;
  top: 14px;
}
.mb-5{
  margin-bottom: 2rem !important;
}

</style>

