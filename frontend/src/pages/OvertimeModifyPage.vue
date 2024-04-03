<template>
  <div>
    <HeaderComponent />
    <SideBar />
    <!-- <h1>초과 근무 수정</h1>

    <form @submit.prevent="submitForm" id="overtimeForm">
      <div class="section-divider"></div> -->
    <!-- 초과 근무 수정 -->
    <!-- <div class="flex-container">
        <label for="id">ID</label>
        <input type="text" id="id" name="id" v-model="formData.id" required>
      </div>
      <div class="flex-container">
        <label for="date">Date</label>
        <input type="date" id="date" name="date" v-model="formData.date">
      </div>
      <div class="flex-container">
        <label for="shift">Shift</label>
        <select id="shift" name="shift" v-model="formData.shift" @change="handleShiftChange">
          <option value="morning">Morning</option>
          <option value="afternoon">Afternoon</option>
        </select>
      </div>
      <div class="flex-container">
        <label for="startTime">Start Time</label>
        <select id="startTime" name="startTime" v-model="formData.startTime"> -->
    <!-- Options dynamically populated based on shift selection -->
    <!-- <option v-for="time in startTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
        </select> -->
    <!-- </div> -->
    <!-- <div class="flex-container">
        <label for="endTime">End Time</label>
        <select id="endTime" name="endTime" v-model="formData.endTime"> -->
    <!-- Options dynamically populated based on shift selection -->
    <!-- <option v-for="time in endTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
        </select> -->
    <!-- </div>
      <div class="section-divider">Reason</div>
      <textarea v-model="formData.reason" id="reason" name="reason" rows="4" cols="50"></textarea>
      <input type="submit" value="Update"> -->
    <!-- <div id="message" style="color: rgb(50, 216, 228);"></div> 수정 완료 메시지 출력 영역 -->
    <!-- </form>
  </div> -->





















    <!-- <br>
    <br>
    <br>
    <div class="col-xl-9">
      <div class="card mb-6" style="margin-left: 100px;   position: relative; left: 189px;">
        <div class="card-header">초과근무 페이지</div>
        <div class="card-body">
          <div class="container">
            <div class="header">
              <h1>초과근무 수정</h1>
            </div>
            <div class="content">
              <form @submit.prevent="submitForm" id="overtimeForm">
                <div class="row">
                  <div class="label">아이디</div>
                  <input type="text" id="id" name="id" v-model="formData.id" required>
                </div>
                <div class="row">
                  <div class="label">초과근무 날짜</div>
                  <input type="date" id="date" name="date" v-model="formData.date">
                </div>
                <div class="row">
                  <div class="label">새벽/야간</div>
                  <select id="shift" name="shift" v-model="formData.shift" @change="handleShiftChange">
                    <option value="morning">Morning</option>
                    <option value="afternoon">Afternoon</option>
                  </select>
                </div>
                <div class="row">
                  <div class="label">초과근무 시작시간</div>
                  <select id="startTime" name="startTime" v-model="formData.startTime"> -->
    <!-- Options dynamically populated based on shift selection -->
    <!-- <option v-for="time in startTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
                  </select> -->
    <!-- </div>
                <div class="row">
                  <div class="label">초과근무 종료시간</div>
                  <select id="endTime" name="endTime" v-model="formData.endTime"> -->
    <!-- Options dynamically populated based on shift selection -->
    <!-- <option v-for="time in endTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
                  </select>
                </div>
                <div class="row">
                  <div class="label">초과근무 사유</div>
                  <textarea v-model="formData.reason" id="reason" name="reason" rows="4" cols="50"></textarea>
                  <input type="submit" value="Update">
                  <div id="message" style="color: rgb(50, 216, 228);"></div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div> -->

















    <br>
    <br>
    <br>
    <div class="content">
      <div class="header">
        <h1>초과근무 수정</h1>
      </div>
      <form @submit.prevent="submitForm" id="overtimeForm">
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="ID">아이디</label>
              <input type="text" id="id" name="id" v-model="formData.id" required class="form-control">
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="overtimeDate">초과근무 날짜</label>
              <input type="date" id="date" name="date" v-model="formData.date">
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="shift">새벽/야간</label>
              <select id="shift" name="shift" v-model="formData.shift" @change="handleShiftChange">
                <option value="morning">Morning</option>
                <option value="afternoon">Afternoon</option>
              </select>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="startTime">초과근무 시작시간</label>
              <select id="startTime" name="startTime" v-model="formData.startTime">
                <!-- Options dynamically populated based on shift selection -->
                <option v-for="time in startTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
              </select>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="endTime">초과근무 종료시간</label>
              <select id="endTime" name="endTime" v-model="formData.endTime">
                <!-- Options dynamically populated based on shift selection -->
                <option v-for="time in endTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
              </select>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label for="reason">초과근무 사유</label>
                <textarea v-model="formData.reason" id="reason" name="reason" rows="4" cols="50" required
                  class="form-control"></textarea>
                <input type="submit" value="Update">
                <div id="message" style="color: rgb(50, 216, 228);"></div>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>











</template>

<script>
import axios from 'axios';
import SideBar from '../components/SideBar.vue';
import HeaderComponent from '../components/HeaderComponent.vue';

export default {
  name: 'OvertimeModifyPage',
  components: {
    SideBar,
    HeaderComponent,
  },
  data() {
    return {
      formData: {
        id: '',
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
    async updateData(formData) {
      try {
        let response = await axios.patch(`http://192.168.0.51/api/employee/overtime/update/${formData.get('id')}`, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        console.log("ID:", response.data.id);
        // Handle response as needed
        alert("초과 근무 수정이 완료되었습니다."); // 신청 완료 메시지 출력
      } catch (error) {
        console.error("Error:", error);
      }
    },
    async submitForm() {
      try {
        const formData = new FormData();
        for (let key in this.formData) {
          formData.append(key, this.formData[key]);
        }
        await this.updateData(formData);
      } catch (error) {
        console.error("Error:", error);
      }
    },
    handleShiftChange() {
      if (this.formData.shift === 'morning') {
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
      } else if (this.formData.shift === 'afternoon') {
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