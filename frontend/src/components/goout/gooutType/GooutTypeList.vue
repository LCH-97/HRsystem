  <template>
    <div>
      <div class="button-container">
        <br>
        <button @click="goToGooutTypeCreate">휴가타입 등록</button>
        <br><br>
      </div>

      <div class="gooutTypeList">
        <ul>
          <li v-for="gooutType in gooutTypes" :key="gooutType.id" @click="goToGooutTypeReadPage(gooutType.id)" class="gooutTypeItem">
            {{ gooutType.name }}
          </li>
        </ul>
      </div>
    </div>
  </template>

  <script>
  import axios from 'axios';

  export default {
    name: 'VacationPage',
    data() {
      return {
        gooutTypes: [],
      }
    },
    created() {
      this.fetchGooutTypes();
    },
    methods: {
      goToGooutTypeCreate() {
        this.$router.push("/gooutType/create");
      },
      fetchGooutTypes() {
        const token = sessionStorage.getItem("token");
        axios.get('http://192.168.0.51/api/gooutType/list',{
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
          .then(response => {
            this.gooutTypes = response.data.result;
          })
          .catch(error => {
            console.error("휴가타입 목록 가져오기 실패:", error);
          });
      },
      goToGooutTypeReadPage(id) {
    if (id) {
      this.$router.push(`/gooutType/read/${id}`);
    } else {
      console.error("ID is undefined");
    }
  }
    },
  };
  </script>

  <style>
  .button-container {
    text-align: right;
    padding-right: 40px;
    background-color: #F7F8FA;
  }
  .button-container button {
    font-size: 18px;
    font-weight: 600;
    padding: 5px 10px;
    color: white;
    letter-spacing: 0.2px;
    border: none;
    background-color: #fae14a;
  }
  .button-container button:hover {
    color: #555555;
  }
  .gooutTypeList ul {
    list-style: none;
    padding: 0;
  }
  .gooutTypeItem {
    cursor: pointer;
    margin: 10px 0;
    transition: color 0.3s ease;
  }
  .gooutTypeItem:hover {
    color: #007BFF; /* 마우스를 올렸을 때 색상 변경 */
  }
  </style>