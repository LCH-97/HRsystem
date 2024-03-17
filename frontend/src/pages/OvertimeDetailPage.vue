<template>
    <div>
        <HeaderComponent />
      <SideBar />
      <h1>초과 근무 상세 정보</h1>
      <hr>
      <div v-for="detail in details" :key="detail.id" class="detail-info">
        <label>{{ detail.label }}</label>
        <span>{{ detail.value }}</span>
      </div>
      <button @click="goBack" class="back-button">뒤로 가기</button>
    </div>
  </template>
  <script>
  import axios from 'axios';
  import SideBar from '../components/SideBar.vue';
  import HeaderComponent from '../components/HeaderComponent.vue';

  export default {
    name: 'OvertimeDetailPage',
  components: {
        SideBar,
      HeaderComponent,
    },
    data() {
      return {
        details: [
          { id: 'id', label: 'id', value: '' },
          { id: 'date', label: 'Date', value: '' },
          { id: 'shift', label: 'Shift', value: '' },
          { id: 'startTime', label: 'Start Time', value: '' },
          { id: 'endTime', label: 'End Time', value: '' },
          { id: 'reason', label: 'Reason', value: '' },
          { id: 'status', label: 'Status', value: '' },
        ]
      }
    },
    async created() {
      try {
        const response = await axios.patch(`http://localhost:8080/employee/overtime/read/${this.$route.params.id}`);
        const overtime = response.data;
        this.details = this.details.map(detail => {
          return { ...detail, value: overtime[detail.id] };
        });
      } catch (error) {
        console.error("Error:", error);
      }
    },
    methods: {
        goBack() {
        this.$router.push('/overtimelist');
        },

        goToDetailPage(id) {
  if (id) {
    this.$router.push(`/overtime/read/${id}`);
  } else {
    console.error("ID is undefined");
  }
}
    }
  }
  </script>
  <style scoped>
  h1 {
    font-size: 30px;
    margin-bottom: 47px;
    margin-top: 96px;
    margin-left: 266px;
  }
  .detail-info {
    margin-bottom: 28px;
    margin-left: 273px;
  }
  .detail-info label {
    font-weight: bold;
  }
  hr {
    border: none;
    border-top: 1px solid #ccc;
    margin: 20px 0;
  }
  .back-button {
    padding: 5px 10px;
    background-color: #070707;
    border: none;
    color: white;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 14px;
    margin-top: 10px;
    cursor: pointer;
    border-radius: 4px;
  }
  .back-button:hover {
    background-color: #EFA535;
  }
  </style>