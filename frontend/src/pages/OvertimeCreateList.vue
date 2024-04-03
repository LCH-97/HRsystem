<template>
    <div>
        <HeaderComponent />
        <SideBar />
        <br>
        <br>
        <br>
        <div class="container">
            <div class="header">
                <h1>초과근무 신청</h1>
            </div>
            <div class="content">
                <form @submit.prevent="submitForm" id="overtimeForm">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="overtimeDate">초과근무 날짜</label>
                                <input type="date" id="overtimeDate" name="date" required v-model="form.date"
                                    class="form-control">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="shift">새벽/야간</label>
                                <select id="shift" name="shift" required v-model="form.shift"
                                    @change="handleShiftChange" class="form-control">
                                    <option value="" disabled selected>시간대 선택</option>
                                    <option value="morning">새벽</option>
                                    <option value="afternoon">야간</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="startTime">초과근무 시작시간</label>
                                <select id="startTime" name="startTime" required v-model="form.startTime"
                                    class="form-control">
                                    <option value="" disabled selected>시간 선택</option>
                                    <option v-for="time in startTimes" :value="time.value" :key="time.value">{{
                    time.label }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="endTime">초과근무 종료시간</label>
                                <select id="endTime" name="endTime" required v-model="form.endTime"
                                    class="form-control">
                                    <option value="" disabled selected>시간 선택</option>
                                    <option v-for="time in endTimes" :value="time.value" :key="time.value">{{ time.label
                                        }}</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="reason">초과근무 사유</label>
                        <textarea v-model="form.reason" id="reason" name="reason" rows="4" cols="50" required
                            class="form-control"></textarea>
                    </div>
                    <input type="submit" value="제출" class="btn-submit">
                </form>

            </div>
        </div>
    </div>

    <br>
    <br>
    <br>

















    <div class="container">
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




</template>

<script>
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import HeaderComponent from '../components/HeaderComponent.vue';

export default {
  name: 'OvertimePage',
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      form: {
        date: '',
        shift: '',
        startTime: '',
        endTime: '',
        reason: ''
      },
      startTimes: [],
      endTimes: [],
      overtimeList: []
    };
  },
  methods: {
    async sendData(formData) {
      try {
        const token = sessionStorage.getItem("token");
        let response = await axios.post("http://192.168.0.51/api/employee/overtime/create", formData, {
          headers: { "Content-Type": "multipart/form-data", Authorization: "Bearer " + token }
        });

        console.log("ID:", response.data.result.id);
        alert("초과 근무 신청이 완료되었습니다.");
      } catch (error) {
        console.error("Error:", error);
        alert("초과 근무 신청이 실패했습니다.");
      }
    },
    async submitForm() {
      try {
        const formData = new FormData();

        formData.append("date", this.form.date);
        formData.append("shift", this.form.shift);
        formData.append("startTime", this.form.startTime);
        formData.append("endTime", this.form.endTime);
        formData.append("reason", this.form.reason);

        await this.sendData(formData);
      } catch (error) {
        console.error("Error:", error);
      }
    },
    handleShiftChange() {
      if (this.form.shift === 'morning') {
        this.startTimes = [
          { label: '06:00 AM', value: '06:00' },
          { label: '07:00 AM', value: '07:00' },
          { label: '08:00 AM', value: '08:00' }
        ];
        this.endTimes = [
          { label: '07:00 AM', value: '07:00' },
          { label: '08:00 AM', value: '08:00' },
          { label: '09:00 AM', value: '09:00' }
        ];
      } else if (this.form.shift === 'afternoon') {
        this.startTimes = [
          { label: '06:00 PM', value: '18:00' },
          { label: '07:00 PM', value: '19:00' },
          { label: '08:00 PM', value: '20:00' }
        ];
        this.endTimes = [
          { label: '07:00 PM', value: '19:00' },
          { label: '08:00 PM', value: '20:00' },
          { label: '09:00 PM', value: '21:00' }
        ];
      }
    },
    async fetchOvertimeList() {
      try {
        const token = sessionStorage.getItem("token");
        const response = await axios.get("http://192.168.0.51/api/employee/overtime/list", {
          headers: { "Content-Type": "multipart/form-data", Authorization: "Bearer " + token }
        });
        this.overtimeList = response.data.result;
      } catch (error) {
        console.error("Error:", error);
      }
    },
    viewOvertimeDetail(id) {
      window.location.href = `/overtime/read/${id}`;
    }
  },
  mounted() {
    this.fetchOvertimeList();
  }
};

</script>

<style scoped>
.container {
    max-width: 1254px;
    margin: 0 auto;
    padding: 30px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    position: relative;
    left: 113px;
}

.header h1 {
    font-size: 24px;
    margin-bottom: 20px;
    color: black;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    font-weight: bold;
    margin-bottom: 5px;
    display: block;
}

.btn-submit {
    background-color: black;
    color: white;
    border: none;
    border-radius: 8px;
    width: 100px;
    height: 50px;
    font-size: 16px;
    font-weight: bold;
    transition: 0.3s;
    position: relative;
    right: -1089px;
}

.btn-submit:hover {
    background-color: #F7941E;
}


body {
  font-family: 'Roboto', sans-serif;
  background-color: #fff;
  color: #000;
  margin: 20px;
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