<br>

## 🌻️ ERD


[ERD 다이어그램 url](https://drive.google.com/file/d/1rbtHuxrIUOgK3eVmim1Ep22Cle-bNTAN/view?usp=sharing)

<br>


## 📝️ Frontend

<br>

<details>
<summary>프로젝트 설계</summary>

<br>
피그마 화면 설계서
<hr>

[Figma url](https://www.figma.com/community/file/1360459368600151333/be02-fin-hello-r-loha-hrsystem)


<br/>

시스템 아키텍쳐
<hr>

![아키텍처](https://github.com/kkm4232/be02-1st-kkm-practice/assets/149145532/a3fc3d4a-01cc-494c-a46a-135016b7fdd3)

<br>
</details>

<details>
<summary>기능 명세서</summary>
<br>
<details>
<summary>회원 기능</summary>

직원 회원 가입 & 로그인
<br>
- 인사 담당자가 직원의 인적사항을 입력 후 직원의 id를 부여하고 직원에게 알려준다.
- 직원은 인사 담당자가 승인해준 id, pw로만 가입이 가능하다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/e29cb4d0-eb34-4320-98d7-997c60076803">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/6d1c6453-0161-4603-ab37-6b8ccebd6c2f">


</p>


<br>
</details>

<details>
<summary>인사 담당자 기능</summary>

인사 담당자 회원 가입 & 로그인
<br>
- 인사 담당자는 본인뿐만 아니라 직원의 회원가입도 담당한다.
- 인사 담당자는 신청한 직원의 계정을 승인해줘야한다.
- 인사 담당자는 본인이 권한을 부여할 수 있다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/e29cb4d0-eb34-4320-98d7-997c60076803">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/6d1c6453-0161-4603-ab37-6b8ccebd6c2f">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/ffe79f67-4bc5-4914-a21c-1eb300aed7d2">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/d8441df9-4773-4437-a9a5-b45e1df42fc2">

</p>
<br>

</details>

<details>
<summary>결재 기능</summary>

결재 생성
<br>
- 직원이 결재 생성 페이지에서 내용 입력과 결재자 선택 후 제출 버튼을 누르면 결재 생성이 된다.
- 직원은 파일 선택 버튼을 누르면 파일을 첨부할 수 있다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/59feb284-c127-4b60-860a-469f35a186c3">
</p>

결재 조회
<br>
- 직원은 결재 조회 페이지에서 결재 기록을 전체 볼 수 있다.
- 직원은 기안 중 버튼을 누르면 기안중인 상태의 결재 내역만 볼 수 있다.
- 직원은 진행 중 버튼을 누르면 진행중인 상태의 결재 내역만 볼 수 있다.
- 직원은 반려 버튼을 누르면 반려 상태의 결재 내역만 볼 수 있다.
- 직원은 결재 완료 버튼을 누르면 결재 완료된 상태의 결재 내역만 볼 수 있다.
- 직원은 결재 만들기 버튼을 누르면 결재만들기 페이지로 이동한다.
- 직원은 결재 목록을 누르면 결재 상세 페이지로 이동한다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/1a027569-0173-470c-8652-123e6c0debea">
</p>

결재 상세 조회
<br>
- 직원은 자신의 결재에 대한 상세 정보를 조회할 수 있다.
- 결재자는 결재나 반려 버튼을 눌러 작성한 결재를 결재 또는 반려할 수 있다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/a028f1bc-14a1-48b3-b1eb-441f122ebf87">
</p>

</details>

<details>
<summary>휴가 기능</summary>

직원 휴가 생성
<br>
- 직원이 휴가 페이지에서 드롭 다운으로 신청자, 대리인, 휴가 유형, 휴가 시작/종료일, 첨부파일(선택), 결재자1/2 선택 후 제출 버튼을 누른다.
- 휴가 올린 사람은 로그인한 사람으로 저장되고 나머지는 선택한 정보들로 저장된다.
  <br>
<hr>
<p align="center">

<img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/1c3311e7-a30c-4a03-adaf-962d1fd4c215">
</p>

직원 휴가 수정
<br>
- 결재자1과 결재자 2가 모두 반려한 글에서만 수정이 가능하다.
- 모든 선택사항은 수정이 가능하다.
- 휴가 결재를 올린 사람만 수정이 가능하다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/8d2374e6-802a-4405-8f56-12ec6b542030">
</p>

직원 휴가 조회
<br>
- 휴가 생성때 저장되었던 모든 정보가 보인다.
- 로그인한 사람의 유에 따라 수정, 삭제 버튼이 보이거나 결재, 반려 버튼이 보인다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/a31b2042-985d-4d0f-9c4d-a341d58f726e">
</p>

직원 휴가 상세 조회
<br>
- 각 휴가의 생성때 저장되었던 모든 정보가 보인다.
- 로그인한 사람의 유형에 따라 수정, 삭제 버튼이 보이거나 결재, 반려 버튼이 보인다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/92948ad3-2ec7-43c2-8ddf-7bd630df3dab">
</p>

</details>

<details>
<summary>출퇴근 기능</summary>

직원 출근 생성
<br>
- 직원이 로그인 후 메인 페이지에서 출근 버튼을 누르면 해당 서버 시간으로 출근 시간이 등록 된다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/20d97636-8bfe-4261-a8fd-2ff1e9145067">

</p>

직원 퇴근 생성
<br>
- 직원이 퇴근할 시 메인 페이지에서 퇴근 버튼을 누르면 퇴근 시간과 함께 총 업무시간이 계산되어 메인페이지에 출력이 된다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/d0ad5636-0544-4828-aace-24592cccbac5">
   <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/636cece4-85ed-4c23-a945-b235f83fbf50">
</p>


직원 출퇴근 수정
<br>
- 인사 담당자만 직원의 출퇴근 시간을 수정할 수 있다.
  <br>
<hr>
<p align="center">

</p>



</details>

<details>
<summary>초과 근무 기능</summary>

직원 초과 근무 생성
<br>
- 직원이 초과 근무 페이지에서 날짜, 시작/종료 시간, 초과 근무 사유를 입력하여 생성한다.
- 직원은 승인이 된 초과 근무만 인정이 된다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/2e2fd672-2229-43c2-add7-9b20506f7a5a">
</p>

직원 초과 근무 수정
<br>
- 직원은 작성한 모든 요구사항에 대한 수정이 가능하다.
- 특수한 경우는 인사 담당자에세 문의해야한다.
- 직원이 수정한 초과 근무는 대기중으로 상태가 변경된다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/29b73274-2e4d-40f0-a125-07fb1a419c04">
</p>


직원 초과 근무 목록 조회
<br>
- 직원은 초과 근무 페이지에서 초과 근무 목록을 볼 수 있다.
- 직원은 초과 근무 목록 페이지에서 날짜, 시간, 사유, 상태를 볼 수 있다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/a89701cb-5c2b-4d6f-a722-0ed9abd7c79d">
</p>

직원 초과 근무 상세 목록 조회
<br>
- 직원은 초과 근무 목록에서 상세 목록 조회를 할 수 있다.
- 직원은 초과 근무 상세 페이지에서 날짜, 시간, 사유, 상태를 볼 수 있다.
- 인사 관리자는 직원이 신청한 초과 근무에 승인할 수 있다.
- 직원은 인사 관리자가 승인한 초과 근무만 사용할 수 있다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/27c28898-c160-4b89-b127-3eb7fbabf7ba">
</p>

</details>

<details>
<summary>급여 기능</summary>

급여 조회
<br>
- 직원이 초과 근무 페이지에서 날짜, 시작/종료 시간, 초과 근무 사유를 입력하여 생성한다.
- 직원은 승인이 된 초과 근무만 인정이 된다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/9d117022-8ab4-4861-87be-f8ec8077ce14">
</p>

--
<br>
- 특수한 경우는 인사 담당자에세 문의해야한다.

  <br>
<hr>
<p align="center">
  <img src="">
</p>


--
<br>
- --

  <br>
<hr>
<p align="center">
  <img src="">
</p>

</details>

<details>
<summary>공지사항 기능</summary>

공지사항 생성
<br>
- 인사 담당자와 직원은 언제든 공지사항을 작성할 수 있다.
- 인사 담당자와 직원은 제목과 내용을 작성하여 공지사항을 생성할 수 있다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/62a58d93-5b3e-42c4-99f4-6074a070a6a0">
</p>


<br>

공지사항 수정
<br>
- 인사 담당자와 직원은 작성한 공지사항에 대해 수정할 수 있다.
- 공지사항은 본인외 수정이 불가하다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/aff9ef4d-c320-463a-9da7-cc9f256286a5">
</p>


<br>

공지사항 조회
<br>
- 인사 담당자와 직원은 생성한 공지사항에 대해 조회가 가능하다.
  <br>
<hr>
<p align="center">
  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/23a7f703-2356-4fde-85a1-833edc94de0e">
</p>

공지사항 상세 조회
<br>
- 인사 담당자와 직원은 생성된 공지사항에 대해 상세 조회가 가능하다.
- 생성된 공지사항을 클릭하면 그에 따른 상세정보 보기가 가능하다.
  <br>
<hr>
<p align="center">

  <img src="https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/f486114c-e255-4c47-a612-aefcd4b15c66">
</p>


<br>
</details>
</details>
