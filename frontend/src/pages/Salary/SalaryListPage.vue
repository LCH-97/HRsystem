<template>
    <HeaderComponent />

    <div class="sb-nav-fixed">
        <div id="layoutSidenav">
            <SideBar />
            <div id="layoutSidenav_content">
                <main>
                    <div id="container-fluid px-6">
                        <!-- <h1 class="mt-4">MAIN PAGE</h1> -->
                        <div class="row" style="width: 100%">


                            <div class="col-xl-12">
                                <div class="card mb-3">
                                    <!-- <div class="card-header">
                                        <a href="/board"> 월급 </a>
                                    </div> -->

                                    <div class="card-body">
                                        <div class="chartjs-size-monitor">
                                            <div class="chartjs-size-monitor-expand">
                                                <div class="container">
                                                    <div class="form-group">
                                                        <label for="inputJobTitle">원하시는 년도를 선택하세요</label>
                                                        <select class="form-control" id="inputJobTitle"
                                                            v-model="selectedYear">
                                                            <option v-for="(year,index) in salaryYears" :key="index" :value="year">{{year}}</option>
                                                            
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="inputJobTitle">원하시는 월을 선택하세요</label>
                                                        <select class="form-control" id="inputJobTitle"
                                                            v-model="selectedMonth" v-on:change="getSalaryList(selectedYear,selectedMonth)">
                                                            <option value="01">01</option>
                                                            <option value="02">02</option>
                                                            <option value="03">03</option>
                                                            <option value="04">04</option>
                                                            <option value="05">05</option>
                                                            <option value="06">06</option>
                                                            <option value="07">07</option>
                                                            <option value="08">08</option>
                                                            <option value="09">09</option>
                                                            <option value="10">10</option>
                                                            <option value="11">11</option>
                                                            <option value="12">12</option>
                                                        </select>
                                                        
                                                    </div>
                                                    
                                                    

                                                    <table class="table">
                                                        <thead>
                                                            <tr>
                                                                <th>사원번호</th>
                                                                <th>부서</th>
                                                                <th>직급</th>
                                                                <th>이름</th>
                                                                <th>입사일</th>
                                                                <th>월급</th>
                                                                <th>계산일</th>
                                                                <th>근무 시간</th>
                                                                <th>휴가 일수</th>
                                                                <th>추가 근무</th>
                                                                <th>실 급여액</th>
                                                            </tr>

                                                            <tr v-for="salary in this.getSalaryResult[0].salaryDtoList"
                                                                v-bind:key="salary.id">
                                                                <td>{{ salary.employeeId }}</td>
                                                                <td>{{ salary.department }}</td>
                                                                <td>{{ salary.position }}</td>
                                                                <td>{{ salary.employeeName }}</td>
                                                                <td>{{ salary.employmentDate }}</td>
                                                                <td>{{ salary.employeeSalary }}</td>
                                                                <td>{{ salary.batchDate }}</td>
                                                                <td>{{ salary.commuteCount }}</td>
                                                                <td>{{ salary.paidVacationCount }}</td>
                                                                <td>{{ salary.overTime }}</td>
                                                                <td>{{ salary.totalSalary }}</td>
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

                            </div>

                        </div>
                    </div>
                </main>
            </div>
        </div>
    </div>
    <LoadingPage v-if="isLoading" @close-event="close" :-title="loadingTitle" :-text="loadingText"></LoadingPage>
</template>

<script>
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import LoadingPage from "@/components/SideBar.vue"
import axios from "axios";



export default {
    name: "SalaryListPage",
    components: {
        SideBar,
        HeaderComponent,
        LoadingPage,
    },
    data() {
        return {
            salaryMonthsResult: {
                "firstDate": "2024-01-10",
                "lastDate": "2024-02-25"
            },
            salaryMonths: [],
            selectedYears: [],
            selectedMonth: "",
            selectedYear: "",
            getSalaryResult: [{
                "month": 13,
                salaryDtoList: [
                    {
                        "employeeId": "",
                        "employmentDate": "",
                        "department": "",
                        "position": "",
                        "employeeName": "",
                        "employeeSalary": "데이터가",
                        "batchDate": "없습니다.",
                        "commuteCount": "",
                        "paidVacationCount": "",
                        "overTime": "",
                        "totalSalary": ""
                    },
                ],
            }

            ],
            // 공지사항



        };
    },
    methods: {
        getSalaryList(year,month) {
            console.log("getSalaryList");
            // const api = process.env.VUE_APP_BACKEND_URL;
            const api = "http://192.168.0.51/api";
            // let formData = new FormData();
            // formData.append('username', this.username);
            // formData.append('password', this.password);
            const token = sessionStorage.getItem("token");
            axios
                .get(api + "/manager/salary/list/"+year+"/"+month, {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                })
                .then((response) => {
                    console.log("Response:", response.data);
                    // this.responseData = response.data;
                    this.getSalaryResult = response.data.result

                })
                .catch((error) => {
                    console.error("Error updating data:", error);
                });
        },
        getYearsBetweenDates(startDate, endDate) {
            // 시작 날짜와 종료 날짜를 Date 객체로 변환합니다.
            const start = new Date(startDate).getFullYear();
            const end = new Date(endDate).getFullYear();

            // 두 날짜 사이의 월 수를 계산합니다.
            const yearsDiff = Math.floor(end - start);

            // 모든 월을 저장할 배열을 생성합니다.
            const years = [];

            // 시작 날짜부터 종료 날짜까지 반복하며 각 월을 배열에 추가합니다.
            let year = end;
            for (let i = 0; i <= yearsDiff; i++) {
                
                // months.push(month.toLocaleString('ko-KR', { year: 'numeric', month: 'long' }));
                years.push(year-i);
                

                // months.push(month.toLocaleString('ko-KR', { year: 'numeric', month: 'long' }));
                
            }

            // 배열을 반환합니다.
            return years;
        },
        fetchSalaryData() {
            console.log("fetchSalaryData method start");
            const api = "http://192.168.0.51/api";
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
                    const lastDate = this.salaryMonthsResult.lastDate;
                    console.log(lastDate);
                    this.selectedMonth = lastDate.split("-")[1];
                    this.selectedYear = lastDate.split("-")[0];
                    this.salaryYears = this.getYearsBetweenDates(this.salaryMonthsResult.firstDate, lastDate);
                
                    //   totalItems = response.data.total;
                })
                .catch((error) => {
                    console.error("Error fetchSalaryData :", error);
                    // 토큰  만료 예외처리
                });
            console.log("fetchSalaryData method end");
        },
    },
    mounted() {
        // const today = new Date();
        
        this.fetchSalaryData();

        this.getSalaryList(2024,3);
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

    margin: auto;
    padding: 8px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    overflow-x: auto;
    font-size: 20px;
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

.card-body{
    border: none;
}
</style>