<template>
  <div class="button-container">
    <br>
    <button @click="goToGooutCreate">휴가 등록</button>
    <br><br>
  </div>

  <div class="container-fluid px-4">
    <h1 class="mt-4">모든 휴가</h1>
    <ol class="breadcrumb mb-4">
      <li class="breadcrumb-item active">모든 휴가</li>
    </ol>
    <div class="row">
      <div class="col-xl-3 col-md-6"></div>
      <div class="col-xl-3 col-md-6"></div>
      <div class="col-xl-3 col-md-6"></div>
      <div class="col-xl-3 col-md-6"></div>
    </div>
    <div class="row">
      <div class="col-xl-6"></div>
      <div class="col-xl-6"></div>
    </div>
    <div class="card mb-4">
      <div class="card-header">
        <i class="fas fa-table me-1"></i>
        내 휴가들
      </div>
      <div class="card-body">
        <button>전체</button>
        <button>기안 중</button>
        <button>진행 중</button>
        <button>반려</button>
        <button>결제 완료</button>
        <table id="datatablesSimple">
          <thead>
            <tr>
              <th>작성자</th>
              <th>요청일</th>
              <th>휴가 시작일</th>
              <th>휴가 종료일</th>
              <th>휴가 유형</th>
              <th>대리자</th>
              <th>결재자</th>
              <th>진행상태</th>
            </tr>
          </thead>
          <tfoot>
            <tr>
              <th>작성자</th>
              <th>요청일</th>
              <th>휴가 시작일</th>
              <th>휴가 종료일</th>
              <th>휴가 유형</th>
              <th>대리자</th>
              <th>결재자</th>
              <th>진행상태</th>
            </tr>
          </tfoot>
          <tbody>
            <tr v-for="goout in goouts" :key="goout.idx">
              <td>{{ goout.employee }}</td>
              <td>{{ goout.updateAt }}</td>
              <td>{{ goout.startDate }}</td>
              <td>{{ goout.endDate }}</td>
              <td>{{ goout.type }}</td>
              <td>{{ goout.derija }}</td>
              <td>{{ goout.결재자 }}</td>
              <td>{{ goout.진행상황 }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

  
<script>
export default {
  name: 'VacationPage',

  methods: {
      goToGooutCreate() {
      this.$router.push("/goout/create");
    },
},

  data() {
    return {
      goouts: [
        // 예시 데이터입니다. 실제 데이터로 대체해야 합니다.
        {
          idx: 1,
          employee: '나',
          updateAt: '태어난 때',
          startDate: '오늘',
          endDate: '평생',
          type: '유급 휴가',
          derija: '알아서해',
          결재자: '나',
          진행상황: '결재',
        },
        // 다른 데이터 항목들...
      ],
    };
  },
};
</script>


  
<style>


.button-container {
  text-align: right;
  padding-right: 40px;
  background-color: #F7F8FA;
}
.button-container button{
  font-size:18px;
  font-weight:600;
  padding: 5px 10px;
  color:white;
  letter-spacing:0.2px;
  border: none;
  background-color: #fae14a;
}
.button-container button:hover {
  color: #555555;
}
</style>
  