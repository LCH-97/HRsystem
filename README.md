# hr_system

![ea3b64c35a81da70e7697c7b7f51a40c](https://github.com/kkm4232/be02-pre-Jenkins-practice/assets/149145532/27cbc02e-0c2f-48d9-8acc-53115237b506)




> **[플레이 데이터] 한화시스템 BEYOND SW캠프/ (🔶Hello_R_loha-HRsystem🔶)**
---


<br>

## &nbsp;🌺 [ Hello_R_loha 데모](http://192.168.0.51)

<br>

## ️️ 📝️ CI/CD

<br>

CI/CD 소개
>CI/CD는 개발 프로세스를 자동화하여 소프트웨어 개발 및 배포를 더 빠르고 안정적으로 만드는 방법입니다. 본 프로젝트에서는 Docker, Kubernetes(k8s), Jenkins를 활용하여 CI/CD 파이프라인을 구축합니다.

## ✨ 기술 스택
<div style="display: flex; gap: 10px; flex-wrap: wrap;">
<img src="https://img.shields.io/badge/GitHub-181717?style=flat&logo=GitHub&logoColor=white&color=black">
<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=Git&logoColor=white&color=ffa500">
<img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat&logo=GitHub-Actions&logoColor=white">
<img src="https://img.shields.io/badge/Jenkins-D24939?style=flat&logo=Jenkins&logoColor=white&color=blue">
<img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=Docker&logoColor=white">
<img src="https://img.shields.io/badge/Kubernetes-326CE5?style=flat&logo=Kubernetes&logoColor=white">
<img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=slack&logoColor=white">
</div>

📌 프로젝트 목표
---
>1. **자동화된 테스트 및 배포 프로세스 구현:** 프로젝트의 주요 목표는 개발 및 배포 프로세스를 자동화하여 소프트웨어 개발 생명주기(SDLC)에서 발생할 수 있는 수동 작업의 오류를 최소화하고, 개발 팀의 생산성을 향상시키는 것입니다.
>2. **Docker를 이용한 환경 일관성 보장:** Docker 컨테이너를 활용하여 개발, 테스트, 운영 환경 간의 일관성을 보장하고, 소프트웨어가 다양한 환경에서도 예측 가능하게 동작하도록 합니다.
>3. **Kubernetes를 통한 컨테이너 오케스트레이션:** Kubernetes를 이용하여 컨테이너화된 어플리케이션의 배포, 확장 및 관리를 자동화합니다. 이는 시스템의 안정성과 확장성을 보장하는 데 중요한 역할을 합니다.
>4. **Jenkins를 활용한 CI/CD 파이프라인 구축:** Jenkins를 사용하여 코드 통합(CI) 및 지속적인 배포(CD) 파이프라인을 구축하고, 코드 변경 사항이 자동으로 테스트되고, 안정적으로 생산 환경에 배포될 수 있도록 합니다.
>5. **개발 및 운영 효율성 증대:** 이러한 자동화된 CI/CD 파이프라인을 통해 개발에서 배포까지의 시간을 단축시키고, 더 빠른 피드백 및 이터레이션을 가능하게 하여 전체적인 소프트웨어 개발 및 운영 효율성을 증대시킵니다.
>6. **고가용성 및 무중단 배포 실현:** Kubernetes와 Jenkins를 활용하여 고가용성을 보장하고, 사용자 서비스에 영향을 주지 않으면서 새로운 버전의 소프트웨어를 무중단으로 배포할 수 있는 시스템을 구현합니다.


## 🖼️ CI/CD 시스템 아키텍처
<details>
<summary>아키텍쳐</summary>

![](https://github.com/beyond-sw-camp/be02-fin-Hello_R_loha-HRsystem/assets/93915072/d62aaa78-56e7-4f61-b5b7-113935c809ae)

</details>



## 🔄 배포 시나리오 : Rolling Update 방식을 이용한 무중단 배포

### Rolling Update 방식을 사용한 이유
>Rolling Update 방식은 구현 방식이 간단하고, 팀원 중에 쿠버네티스를 다뤄본 사람이 없다는 현재 상황에서,
복잡한 방법보다는 가장 간단한 구현 방식을 선택하게 되었습니다.


>1. develop branch에서 통합이 이루어지면, github action이 Junit을 통해 작성된 테스트 코드를 실행한다.
>2. 깃허브(원격 저장소) main branch 에 최신 버전의 프로젝트가 "push" 된다.
>3. 깃허브는 젠킨스에게 Webhook을 보낸다.
>4. 젠킨스는 파이프라인에 저장된 절차를 실행한다.
    <br>
 　 <br>a. 젠킨스 서버에 깃허브의 있는 프로젝트를 가져온다. (git clone)
 　 <br>b. 프로젝트가 벡엔드라면 "mvn package", 프로젝트가 프론트엔드라면 "npm run build" 를 통해 빌드 한다.
 　 <br>c. 빌드를 통해 생긴 "jar" 또는 "dist 폴더" 를 이용해 Dockerfile로 Docker image 를 만든다.
 　 <br>d. Docker image를 Docker hub에 "push" 한다.
 　 <br>e. 젠킨스 서버에서 k8s master에 "deployment" yaml file을 전송한다.
 　 <br>f. k8s master에서 yaml file들을 적용시킨다. ( kubectl apply )
 　 <br>g. 파이프라인을 진행하면서 단계마다 시작, 종료, 결과를 젠킨스 서버에서 Jenkins CI 를 통해
     　 Slack으로 전송한다.
 　 <br>h. Slack을 통해 개발자들은 파이프라인 진행 현황을 확인할 수 있다.
    <br><br>
>5. 최종적으로 Rolling Update 방식을 통해 무중단 배포가 이루어 진다.



## 🎥 CI/CD 시연 영상
<details>
<summary>Backend CI/CD</summary>
사진 넣을 예정
</details>
<details>
<summary>Frontend CI/CD</summary>
사진 넣을 예정
</details>



