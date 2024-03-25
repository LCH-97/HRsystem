<template>
  <div>
    <HeaderComponent />
      <SideBar />

    <h1>초과 근무 신청</h1>

    <form @submit.prevent="submitForm" id="overtimeForm">
      <div class="section-divider"></div>
      <!-- 초과근무 신청서 -->
      <div class="flex-container">
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
        let response = await axios.post("http://localhost:8080/employee/overtime/create", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        console.log("ID:", response.data.id);
        // Handle response as needed
        alert("초과 근무 신청이 완료되었습니다."); // 신청 완료 메시지 출력
      } catch (error) {
        console.error("Error:", error);
      }
    },
    async submitForm() {
      try {
        const formData = new FormData();
        for (let key in this.form) {
          formData.append(key, this.form[key]);
        }
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
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: rgba(255, 254, 254, 0.955); /* 회색 배경 */
        }
        h1 {
            text-align: left;
            font-size: 30px; /* 크기 작게 */
            margin-bottom: -14px; /* 간격 추가 */
            margin-top: 70px;
            margin-left: 249px;
        }
        p {
            text-align: left;
            font-size: 14px;
            margin-left: 249px;
            margin-top: -54px;
        }
        form {
            max-width: 1170px; /* 전체 폼의 최대 너비 조정 */
            margin: 37 auto;
            padding: 47px;
            background-color: #fff; /* 흰색 배경 */
            border-radius: 6px;
            box-shadow: 0 0px 0px rgba(0, 0, 0, 0.1);
            border: 0px solid #ffffff; /* 테두리 색 설정 */
            text-align: left; /* 왼쪽 정렬 */
            margin-left: 201px;
        }
        label {
            display: inline-block; /* 인라인 블록 요소로 설정 */
            width: 120px; /* 레이블 너비 설정 */
            margin-bottom: 5px;
            margin-right: 10px;
        }
        input[type="date"],
        select,
        textarea {
            width: calc(100% - 130px); /* 테두리 고려하여 너비 조정 */
            padding: 8px;
            margin-bottom: 10px;
            border:1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            background-color: #1a1817; /* 오렌지색 버튼 */
            color: white;
            padding: 10px 15px; /* 작은 사이즈로 조정 */
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px; /* 폰트 크기 조정 */
            float: right; /* 우측 정렬 */
            margin-top: 40px;
        }
        input[type="submit"]:hover {
            background-color: #ef7b2d; /* 오렌지색 버튼 호버 상태 */
        }
        .section-divider {
            border-bottom: 1px solid #ccc; /* 밑줄 추가 */
            margin-bottom: 20px; /* 간격 추가 */
            padding-bottom: 10px; /* 간격 추가 */
            font-weight: bold; /* 제목 굵게 */
        }
        .flex-container {
            display: flex;
            flex-direction: row;
            justify-content: flex-start; /* 왼쪽 정렬 */
            margin-bottom: 10px; /* 간격 추가 */
        }
        .flex-item {
            margin-right: 10px; /* 간격 추가 */
        }
    </style>
