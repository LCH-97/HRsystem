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


                            <div class="col-xl-11">
                                <div class="card mb-3">
                                    <div class="card-header">
                                        <a href="/board"> 월급 </a>
                                    </div>

                                    <div class="card-body">
                                        <div class="chartjs-size-monitor">
                                            <div class="chartjs-size-monitor-expand">
                                                <div class="container">
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
</template>

<script>
import SideBar from "@/components/SideBar.vue";
import HeaderComponent from "@/components/HeaderComponent.vue";
import axios from "axios";



export default {
    name: "SalaryListPage",
    components: {
        SideBar,
        HeaderComponent,
    },
    data() {
        return {

            getSalaryResult:[{
                "month": 1,
                salaryDtoList: [
                {
                    "employeeId": 1,
                    "employmentDate": "2024-03-15",
                    "department": "사장실",
                    "position": "사장",
                    "employeeName": "asd",
                    "employeeSalary": 5000000,
                    "batchDate": "2024-02-10",
                    "commuteCount": 0,
                    "paidVacationCount": 0,
                    "overTime": 0,
                    "totalSalary": 38300723
                },
            ],
            }
            
            ],
            // 공지사항
            


        };
    },
    methods: {
        getSalaryList() {
            console.log("getSalaryList");
            // const api = process.env.VUE_APP_BACKEND_URL;
            const api = "http://localhost:8080";
            console.log(api);
            // let formData = new FormData();
            // formData.append('username', this.username);
            // formData.append('password', this.password);
            const token = sessionStorage.getItem("token");
            axios
                .get(api + "/manager/salary/list", {
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
        
    },
    mounted() {
        // 출근한 상태인지 확인해야함.
        this.getSalaryList();
        
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
</style>