<template>
  <div>
    <HeaderComponent />
      <SideBar />
    <h1>초과 근무 수정</h1>

    <form @submit.prevent="submitForm" id="overtimeForm">
      <div class="section-divider"></div>
      <!-- 초과 근무 수정 -->
      <div class="flex-container">
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
        <select id="startTime" name="startTime" v-model="formData.startTime">
          <!-- Options dynamically populated based on shift selection -->
          <option v-for="time in startTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
        </select>
      </div>
      <div class="flex-container">
        <label for="endTime">End Time</label>
        <select id="endTime" name="endTime" v-model="formData.endTime">
          <!-- Options dynamically populated based on shift selection -->
          <option v-for="time in endTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
        </select>
      </div>
      <div class="section-divider">Reason</div>
      <textarea v-model="formData.reason" id="reason" name="reason" rows="4" cols="50"></textarea>
      <input type="submit" value="Update">
      <div id="message" style="color: rgb(50, 216, 228);"></div> <!-- 수정 완료 메시지 출력 영역 -->
    </form>
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
  body {
    font-family: 'Roboto', sans-serif;
    background-color: #fff;
    color: #000;
    margin: 20px;
  }
  
  h1 {
    font-size: 34px;
    margin-left: 266px;
    margin-top: 55px;
  }
  
  form {
    max-width: 1170px;
    margin: 37 auto;
    padding: 47px;
    background-color: #fff;
    border-radius: 6px;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.1);
    border: 0px solid #ffffff;
    text-align: left;
    margin-left: 223px;
  }
  
  label {
    display: inline-block;
    width: 120px;
    margin-bottom: 5px;
    margin-right: 10px;
  }
  
  input[type="text"],
  input[type="date"],
  select,
  textarea {
    width: calc(100% - 130px);
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }
  
  input[type="submit"] {
    background-color: #1a1817;
    color: white;
    padding: 10px 15px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    float: right;
    margin-top: 40px;
  }
  
  input[type="submit"]:hover {
    background-color: #ef7b2d;
  }
  
  .section-divider {
    border-bottom: 1px solid #ccc;
    margin-bottom: 20px;
    padding-bottom: 10px;
    font-weight: bold;
  }
  
  .flex-container {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    margin-bottom: 10px;
  }
  
  .flex-item {
    margin-right: 10px;
  }
  </style>