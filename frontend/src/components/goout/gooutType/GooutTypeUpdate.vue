<template>
    <div>
      <div class="productCeoUpdateBanner">
        <div class="productCeoUpdateBannerTxt">
          <h2>휴가타입 수정하기</h2>
        </div>
      </div>
      <div class="ReqBox">
        <article class="ReqInputBox"> 
          <div class="ReqInput">
            <p>휴가타입 이름</p>
            <input
              id="name"
              type="text"
              v-model="productInfo.name"
              placeholder="휴가타입 이름"
              class="nameEx"
            /><br />
  
            <p>휴가타입 설명</p>
            <input
              id="detail"
              type="text"
              v-model="productInfo.detail"
              placeholder="휴가타입 설명"
              class="detailEx"
            /><br />
  
            <p>연간 최대 사용 횟수</p>
            <input
              id="maxHoliday"
              type="text"
              v-model="productInfo.maxHoliday"
              placeholder="숫자만 적어주세요"
              class="maxHolidayEx"
            /><br /><br/>
          </div>
        </article>
        <div class="button-container">
          <button id="send" @click="updateProduct">수정</button><br /><br />
        </div>
      </div>
    </div>
  </template>
  
  <script>
//   import axios from "axios";
  
//   export default {
//     data() {
//   return {
//     productInfo: {
//       id: null,
//       name: "",
//       detail: "",
//       maxHoliday: "",
//     },
//   };
// },
// mounted() {
//   const gooutTypeInfo = JSON.parse(localStorage.getItem('updateGooutTypeInfo'));
//   if (gooutTypeInfo) {
//     this.productInfo = { ...gooutTypeInfo };
//     localStorage.removeItem('updateGooutTypeInfo'); // 사용 후 삭제
//   }
// },
//     methods: {
//       loadProductInfo() {
//         const idx = this.$route.query.id || this.$route.params.id; // 쿼리 파라미터 또는 라우트 파라미터에서 idx를 가져옵니다.
//         axios
//           .get(`localhost:8080/gooutType/update/${id}`)
//           .then((response) => {
//             if (response.data.code === 1000) {
//               this.productInfo = { idx, ...response.data.result };
//             }
//           })
//           .catch((error) => console.error("Error loading product info:", error));
//       },
//       updateProduct() {
//   // id 값을 포함한 productInfo 객체를 전송
//   axios.patch(`localhost:8080/productCeo/updateCeo`, this.productInfo)
//     .then(() => {
//       alert("상품이 성공적으로 수정되었습니다.");
//       this.$router.push(`/productCeo/detail/${this.productInfo.idx}`);
//     })
//     .catch((error) => {
//       console.error("Error:", error);
//       alert("상품 수정에 실패하였습니다.");
//     });
// },
//     },
//   };
  </script>
  
  <style scoped>
  .productCeoUpdateBanner {
    margin: 0 auto; /* 수평 중앙 정렬 */
    text-align: center; /* 텍스트 중앙 정렬 */
    padding-top: 15px;
    padding-bottom: 20px;
    background-color: #f7f8fa;
  }
  
  .productCeoUpdateBannerTxt {
    font-size: 40px;
    font-weight: 600;
    color: rgb(85, 85, 85);
  }
  
  .ReqBox {
    margin: 0 auto;
    text-align: center;
    background-color: #f7f8fa;
    border: 1px solid #f7f8fa;
    border-radius: 12px;
  }
  
  .ReqInput {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    flex-basis: 0px;
    font-size: 20px;
    font-weight: bold;
    color: rgb(85, 85, 85);
    margin-top: 28px;
    margin-bottom: 28px;
    text-align: center;
    width: 30%;
  }
  .storeNameEx,
  .productNameEx,
  .phoneNumberEx,
  .priceEx,
  .contentsEx {
    text-align: center;
  }
  .picture {
    text-align: center;
    margin-left: 68px;
  }
  .ReqInputBox {
    margin: 0 auto;
    text-align: center;
    width: 30%;
    display: flex;
    justify-content: center;
  }
  
  .button-container button {
    font-size: 18px;
    font-weight: bold;
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
  </style>
  