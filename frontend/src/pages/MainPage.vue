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
                                    <div class="card-header">

                                        Area Chart Example
                                    </div>
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
                                        Bar Chart Example
                                    </div>
                                    <div class="card-body">
                                        <div class="chartjs-size-monitor">
                                            <div class="chartjs-size-monitor-expand">


                                                <div
                                                    class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                                                    
                                                    <div class="datatable-container">
                                                        <table id="datatablesSimple" class="datatable-table">
                                                            <thead>
                                                                <tr>
                                                                    <th data-sortable="true"
                                                                        style="width: 19.287833827893174%;"><a href="#"
                                                                            class="datatable-sorter">Name</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 30.56379821958457%;"><a href="#"
                                                                            class="datatable-sorter">Position</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 14.93570722057369%;"><a href="#"
                                                                            class="datatable-sorter">Office</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 8.605341246290802%;"><a href="#"
                                                                            class="datatable-sorter">Age</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 14.342235410484669%;"><a href="#"
                                                                            class="datatable-sorter">Start date</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 12.265084075173096%;"><a href="#"
                                                                            class="datatable-sorter">Salary</a></th>
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
                                <div class="card mb-3">
                                    <div class="card-header">
                                        Bar Chart Example
                                    </div>
                                    <div class="card-body">
                                        <div class="chartjs-size-monitor">
                                            <div class="chartjs-size-monitor-expand">


                                                <div
                                                    class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                                                    
                                                    <div class="datatable-container">
                                                        <table id="datatablesSimple" class="datatable-table">
                                                            <thead>
                                                                <tr>
                                                                    <th data-sortable="true"
                                                                        style="width: 19.287833827893174%;"><a href="#"
                                                                            class="datatable-sorter">Name</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 30.56379821958457%;"><a href="#"
                                                                            class="datatable-sorter">Position</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 14.93570722057369%;"><a href="#"
                                                                            class="datatable-sorter">Office</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 8.605341246290802%;"><a href="#"
                                                                            class="datatable-sorter">Age</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 14.342235410484669%;"><a href="#"
                                                                            class="datatable-sorter">Start date</a></th>
                                                                    <th data-sortable="true"
                                                                        style="width: 12.265084075173096%;"><a href="#"
                                                                            class="datatable-sorter">Salary</a></th>
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
                                    <div class="card-header">
                                        Bar Chart Example
                                    </div>
                                    <div class="card-body">
                                        <div class="chartjs-size-monitor">
                                            <div class="chartjs-size-monitor-expand">


                                                <div id="commute-info">
                                                    <h2>안녕하세요 </h2>
                                                    <h2>{{ this.$route.query.name }}</h2>
                                                    <img class="profile-pic"
                                                        src="https://png.pngtree.com/png-clipart/20191121/original/pngtree-user-vector-icon-png-image_5152508.jpg"
                                                        alt="Profile Picture">

                                                    <!-- 나중에는 여기 직원 이름이 오도록 -->

                                                    <div class="main-button-container" v-show="!isCommute">
                                                        <button id="commuteButton" @click="commute">출근</button>
                                                    </div>

                                                    <div class="main-button-container" v-show="isCommute && !isLeave">
                                                        <button id="leaveButton" @click="leave">퇴근 {{ this.commuteId
                                                            }}</button>
                                                    </div>

                                                    <div class="main-button-container" v-show="isCommute && isLeave">
                                                        <button id="leaveButton">빨리
                                                            나가라</button>
                                                    </div>


                                                    <div id="startTime" class="time">근무 시작 </div>
                                                    <div>{{ this.startTime }} </div>
                                                    <div id="endTime" class="time">근무 종료 </div>
                                                    <div>{{ this.endTime }} </div>
                                                    <div id="sumTime" class="time">총 업무시간 </div>
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






</template>


<script>
import SideBar from '../components/SideBar.vue'
import HeaderComponent from '../components/HeaderComponent.vue'
import axios from 'axios';
// 달력
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
    name: 'MainPage',
    components: {
        SideBar,
        HeaderComponent,
        FullCalendar,

    },
    data() {
        return {
            startTime: '0',
            endTime: '0',
            sumTime: '0',
            isCommute: false,
            isLeave: true,
            commuteId: '',
            //달력 옵션
            calendarOptions: {
                plugins: [dayGridPlugin, interactionPlugin],
                initialView: 'dayGridMonth'
            }


        };
    },
    methods: {
        commute() {
            console.log("click");
            // const api = process.env.VUE_APP_BACKEND_URL;
            const api = 'http://localhost:8080';
            console.log(api);
            // let formData = new FormData();
            // formData.append('username', this.username);
            // formData.append('password', this.password);
            const token = sessionStorage.getItem('token');
            axios.post(api + '/employee/commute', null, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                }
            })
                .then(response => {
                    console.log('Response:', response.data);
                    // this.responseData = response.data;
                    this.startTime = response.data.result.startTime;
                    this.commuteId = response.data.result.id;
                    this.isCommute = true;
                    this.isLeave = false;
                })
                .catch(error => {
                    console.error('Error updating data:', error);
                });
        },
        leave() {
            console.log(" leave click");
            // const api = process.env.VUE_APP_BACKEND_URL;
            const api = 'http://localhost:8080';
            console.log(api);
            // let formData = new FormData();
            // formData.append('username', this.username);
            // formData.append('password', this.password);
            const token = sessionStorage.getItem('token');
            axios.patch(api + '/employee/leave/' + this.commuteId, null, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                }
            })
                .then(response => {
                    console.log('Response:', response.data);
                    // this.responseData = response.data;
                    this.endTime = response.data.result.endTime;
                    this.sumTime = response.data.result.sumTime;
                    this.isLeave = true;
                })
                .catch(error => {
                    console.error('Error updating data:', error);
                });
        },
        check() {
            console.log("check");
            // const api = process.env.VUE_APP_BACKEND_URL;
            const api = 'http://localhost:8080';
            console.log(api);
            // let formData = new FormData();
            // formData.append('username', this.username);
            // formData.append('password', this.password);
            const token = sessionStorage.getItem('token');
            axios.get(api + '/employee/commute/check', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token,
                }
            })
                .then(response => {
                    console.log('Chcek Response:', response.data);
                    // this.responseData = response.data;
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




                    //페이지 구성에 필요한 걸 다 가져와야한다.
                })
                .catch(error => {
                    console.error('Error updating data:', error);
                });
        },
    },
    mounted() {
        // 출근한 상태인지 확인해야함.
        this.check()

    },
}
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
    background-color: #FF8C00;
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
    border: 2px solid #FFF;
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
</style>