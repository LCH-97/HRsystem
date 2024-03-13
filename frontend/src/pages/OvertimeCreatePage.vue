<template>
    <div>
      <HeaderComponent />
      <SideBar />
  
      <div class="page-content">
        <h1>초과 근무 신청</h1>
  
        <form @submit.prevent="submitForm">
          <div class="section-divider"></div>
          <!-- 초과근무 신청서 -->
          <div class="flex-container">
            <label for="date">Date</label>
            <input type="date" id="date" name="date" required v-model="formData.date">
          </div>
          <div class="flex-container">
            <label for="shift">Shift</label>
            <select id="shift" name="shift" required v-model="formData.shift" @change="handleShiftChange">
              <option value="morning">Morning</option>
              <option value="afternoon">Afternoon</option>
            </select>
          </div>
          <div class="flex-container">
            <label for="startTime">Start Time</label>
            <select id="startTime" name="startTime" required v-model="formData.startTime">
              <option disabled :selected="!formData.startTime">Select Start Time</option>
              <option v-for="time in startTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
            </select>
          </div>
          <div class="flex-container">
            <label for="endTime">End Time</label>
            <select id="endTime" name="endTime" required v-model="formData.endTime">
              <option disabled :selected="!formData.endTime">Select End Time</option>
              <option v-for="time in endTimes" :key="time.value" :value="time.value">{{ time.label }}</option>
            </select>
          </div>
          <div class="section-divider">Reason</div>
          <textarea id="reason" name="reason" rows="4" cols="50" required v-model="formData.reason"></textarea>
          <input type="submit" value="Submit">
        </form>
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
        formData: {
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
      async submitForm() {
        try {
          let response = await axios.post("http://localhost:8080/employee/overtime/create", this.formData, {
            headers: { "Content-Type": "multipart/form-data" },
          });

          alert("초과 근무 신청 완료했습니다.");
  
          console.log("ID:", response.data.id);
          // Handle response as needed
        } catch (error) {
          console.error("Error:", error);
        }
      },
      handleShiftChange() {
        if (this.formData.shift === 'morning') {
          this.startTimes = [{ label: '06:00 AM', value: '06:00' }, { label: '07:00 AM', value: '07:00' }, { label: '08:00 AM', value: '08:00' }];
          this.endTimes = [{ label: '07:00 AM', value: '07:00' }, { label: '08:00 AM', value: '08:00' }, { label: '09:00 AM', value: '09:00' }];
        } else if (this.formData.shift === 'afternoon') {
          this.startTimes = [{ label: '06:00 PM', value: '18:00' }, { label: '07:00 PM', value: '19:00' }, { label: '08:00 PM', value: '20:00' }];
          this.endTimes = [{ label: '07:00 PM', value: '19:00' }, { label: '08:00 PM', value: '20:00' }, { label: '09:00 PM', value: '21:00' }];
        }
      }
    }
  };
  </script>
  
  <style scoped>
  body {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 20px;
    background-color: rgba(255, 254, 254, 0.955);
  }
  
  h1 {
    text-align: left;
    font-size: 30px;
    margin-bottom: -14px;
    margin-top: 25px;
    margin-left: 265px;
  }
  
  p {
    text-align: left;
    font-size: 14px;
    margin-left: 0;
    margin-top: -54px;
  }
  
  form {
    max-width: 1170px;
    margin: 37px auto;
    padding: 47px;
    background-color: #fff;
    border-radius: 6px;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.1);
    border: 0px solid #ffffff;
    text-align: left;
    margin-left: 216px;
    margin-right: -57px;
  }
  
  label {
    display: inline-block;
    width: 120px;
    margin-bottom: 5px;
    margin-right: 10px;
  }
  
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
  
  
  