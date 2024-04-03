<template>
  <div>
    <HeaderComponent />
    <SideBar />

    <!-- <h1>초과근무 신청</h1>

    <form @submit.prevent="submitForm" id="overtimeForm">
      <div class="section-divider"></div> -->
      <!-- 초과근무 신청서 -->
      <!-- <div class="flex-container">
        <label for="date">Date</label>
        <input type="date" id="date" name="date" required v-model="form.date">
      </div>
      <div class="flex-container">
        <label for="shift">Shift</label>
        <select id="shift" name="shift" required v-model="form.shift" @change="handleShiftChange">
          <option value="morning">Morning</option>
          <option value="afternoon">Afternoon</option>
        </select>
      </div>
      <div class="flex-container">
        <label for="startTime">Start Time</label>
        <select id="startTime" name="startTime" required v-model="form.startTime">
          <option value="" disabled selected>Select Start Time</option>
          <option v-for="time in startTimes" :value="time.value" :key="time.value">{{ time.label }}</option>
        </select>
      </div>
      <div class="flex-container">
        <label for="endTime">End Time</label>
        <select id="endTime" name="endTime" required v-model="form.endTime">
          <option value="" disabled selected>Select End Time</option>
          <option v-for="time in endTimes" :value="time.value" :key="time.value">{{ time.label }}</option>
        </select>
      </div>
      <div class="section-divider">Reason</div>
      <textarea v-model="form.reason" id="reason" name="reason" rows="4" cols="50" required></textarea>
      <input type="submit" value="Submit">
    </form>
    <br>
    <br>

    <p>
      <br>
      추가적인 문의사항은 아래로 연락바랍니다.<br>
      <br>
      Phone: 010.0000.0000 <br>
      <br>
      Email: 000001234@gmail.com <br>
    </p>
  </div> -->
  
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
      <input type="date" id="overtimeDate" name="date" required v-model="form.date" class="form-control">
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label for="shift">새벽/야간</label>
      <select id="shift" name="shift" required v-model="form.shift" @change="handleShiftChange" class="form-control">
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
      <select id="startTime" name="startTime" required v-model="form.startTime" class="form-control">
        <option value="" disabled selected>시간 선택</option>
        <option v-for="time in startTimes" :value="time.value" :key="time.value">{{ time.label }}</option>
      </select>
    </div>
  </div>
  <div class="col-md-6">
    <div class="form-group">
      <label for="endTime">초과근무 종료시간</label>
      <select id="endTime" name="endTime" required v-model="form.endTime" class="form-control">
        <option value="" disabled selected>시간 선택</option>
        <option v-for="time in endTimes" :value="time.value" :key="time.value">{{ time.label }}</option>
      </select>
    </div>
  </div>
</div>
          <div class="form-group">
            <label for="reason">초과근무 사유</label>
            <textarea v-model="form.reason" id="reason" name="reason" rows="4" cols="50" required class="form-control"></textarea>
          </div>
          <input type="submit" value="제출" class="btn-submit">
        </form>
       
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import HeaderComponent from '../components/HeaderComponent.vue';

export default {
  name: 'OvertimeCreatePage',
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
      endTimes: []
    };
  },
  methods: {
    async sendData(formData) {
      try {
        const token = sessionStorage.getItem("token");
        let response = await axios.post("http://192.168.0.51/api/employee/overtime/create", formData, {
          headers: { "Content-Type": "multipart/form-data", Authorization: "Bearer " + token, },

        });

        console.log("ID:", response.data.result.id);
        // Handle response as needed
        alert("초과 근무 신청이 완료되었습니다."); // 신청 완료 메시지 출력
      } catch (error) {
        console.error("Error:", error);
        alert("초과 근무 신청이 실패했습니다."); // 신청 실패 메시지 출력
      }
    },
    async submitForm() {
      try {
        const formData = new FormData();

        formData.append("date", this.date);
        formData.append("shift", this.shift);
        formData.append("startTime", this.startTime);
        formData.append("endTime", this.endTime);
        formData.append("reason", this.reason);
        console.log("create overtime data:", this.form);

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
    }
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
</style>