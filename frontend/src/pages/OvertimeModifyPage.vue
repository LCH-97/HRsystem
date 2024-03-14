<template>
    <div>

        <HeaderComponent />
      <SideBar />


      <h1>초과 근무 수정</h1>
  
      <form id="overtimeForm">
        <div class="section-divider"></div> 
        <!-- 초과 근무 수정 -->
        <div class="flex-container">
          <label for="id">ID</label>
          <input type="text" id="id" name="id" required v-model="formData.id">
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
          </select>
        </div>
        <div class="flex-container">
          <label for="endTime">End Time</label>
          <select id="endTime" name="endTime" v-model="formData.endTime">
            <!-- Options dynamically populated based on shift selection -->
          </select>
        </div>
        <div class="section-divider">Reason</div>
        <textarea id="reason" name="reason" rows="4" cols="50" v-model="formData.reason"></textarea>
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
    name: 'OvertimeEditPage',
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
        }
      };
    },
    methods: {
      async updateData(formData) {
        try {
          let response = await axios.patch(`http://localhost:8080/employee/overtime/update/${formData.id}`, formData, {
            headers: { "Content-Type": "multipart/form-data" },
          });
  
          console.log("ID:", response.data.id);
          alert("초과 근무 수정이 완료되었습니다."); // 신청 완료 메시지 출력
        } catch (error) {
          console.error("Error:", error);
        }
      },
      handleShiftChange() {
        const startTimeSelect = document.getElementById("startTime");
        const endTimeSelect = document.getElementById("endTime");
  
        if (this.formData.shift === "morning") {
          this.populateTimeOptions(startTimeSelect, endTimeSelect, 6, 8, true);
        } else if (this.formData.shift === "afternoon") {
          this.populateTimeOptions(startTimeSelect, endTimeSelect, 18, 21, false);
        }
      },
      populateTimeOptions(startSelect, endSelect, startHour, endHour, isMorning) {
        startSelect.innerHTML = "";
        endSelect.innerHTML = "";
        const period = isMorning ? "AM" : "PM";
        for (let i = startHour; i <= endHour; i++) {
          const option = document.createElement("option");
          const hour = i < 10 ? `0${i}` : `${i}`;
          option.text = `${hour}:00 ${period}`;
          option.value = `${hour}:00`;
          startSelect.add(option);
          endSelect.add(option.cloneNode(true));
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