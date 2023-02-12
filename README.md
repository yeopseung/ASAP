

### 2021 숭실대학교 컴퓨터학부 소프트웨어 공모전 출품작 (총장상)
##  작품명 : ASAP(Auto Sorting APplication)

### 기획 의도
택배 노동자 총파업의 불씨가 된 일명 ‘택배 까대기’란 마구잡이로 쏟아지는 택배 속에서 기사님 본인이 배송 해야 할 물건을 직접 찾아가며 분류하고 차량에 싣는 것을 일컫는 말이다. 평균적으로 택배 기사님 한 분당 하루에 배송해야 할 물건이 200개가량 되는데, 이를 배송 순서대로 정리하고 차량에 싣는 데에만 2~3시간이 소요된다. 이는 비효율적인 분류 시스템이며, 불필요한 노동력을 야기한다. 심지어 이러한 과정은 노동 시간으로 인정되지 않는다. 그 결과 과로로 사망하는 택배 기사님들이 늘어났고, 이것이 택배 총파업의 주 원인으로 지목되며 이 문제를 해결해야 한다는 목소리가 커졌다.
 <p></p>
 ASAP 팀은 이 문제를 체감하기 위해 직접 택배 기사님 보조 아르바이트를 해보며 문제점을 몸소 느껴보았다. 실제 아르바이트를 통해 택배 분류 및 배송 과정을 경험해본 결과 택배를 배송 순서대로 정렬하여 싣는 과정과 네비게이션에 경유지를 일일이 입력하는 부분 등을 소프트웨어적으로 해결한다면 효율적인 작업에 도움이 될 것이라 판단하였고 이 어플리케이션을 기획하게 되었다.
 <p></p> <p></p>

### 작품 개요
 <div align="center">
 <img src="https://user-images.githubusercontent.com/77184523/172041427-70c32eac-5aee-42f2-954f-bed82facc6fe.png" width="600" height="300"/>
   </div>
 ASAP 앱은 서브(지역)터미널에서 배달까지의 과정에서 택배 기사님들의 분류 및 배송 작업을 보조하기 위한 어플리케이션이다. 서브(지역)터미널에서 (1)택배의 바코드를 찍는 과정, (2)배송 순서에 따라 택배에 상차하는 과정을 보조하는 자동 정렬, (3)배달과정에서 경유지를 일일이 네비게이션에 입력하는 과정을 어플리케이션으로 제작하였다. 
 <p></p>
(1) -> 목록 생성하기
 <p></p>
(2) -> 경유지 자동 정렬 및 사용자 임의 수정, 배송 순서 확인하기
 <p></p>
(3) -> T-map 연동하기
 <p></p>
택배 기사님들의 불필요한 노동을 최소화하기 위해 ASAP 어플리케이션 내부에서 (1),(2),(3)의 문제를 위와 같은 기능으로 구현하였다.
 <p></p>
*택배 운송장 바코드에는 기본적으로 주소, 운송장 번호 정보가 기재되어 있으나 보안상의 문제로 택배 기사 만이 열람할 수 있게 되어 있다. 따라서 운송장 바코드는 임의로 직접 만들었다.
 <p></p>
  <p></p>

### 시스템 구성도
#### [Application 구성도]
   <div align="center">
  <img src="https://user-images.githubusercontent.com/77184523/172041520-b07c4d6b-ca5e-460c-81da-5c0651aa8e14.png" width="600" height="300"/>
     </div>
     본 Application의 구성도이다. SQLite로 구현한 Data Base를 중심으로 구성하였으며, 각 기능에서 Data Base에 저장된 데이터를 활용하는 방식으로 구현하였다.  <p></p>
 ZXing API로 구현한 QR Reader에서 데이터를 입력 받아 Date Base에 저장한다. Google Map과 연결된 Recycler View에서 DB 데이터를 복사하여 수정 및 삭제할 수 있도록 구현하였고, Recycler View에서 수정된 부분이 바로 Google Map에 반영될 수 있도록 하였다.  <p></p>
 T-map과 연결된 Recycler View에서 DB 테이블 목록을 출력하였고, 각 테이블마다 T-map 내비게이션 기능을 연동하여 각각의 경유지에 따른 길 안내가 가능하도록 구현하였다.  <p></p>  <p></p>

#### [Use-Case Diagram]
1) 목록 생성하기
   <div align="center">
    <img src="https://user-images.githubusercontent.com/77184523/172041609-4f816fa1-e136-4a27-9d5d-da0a91256c36.png" width="600" height="300"/>
   </div>
    <p></p>
2) 지도에서 확인하기
   <div align="center">
    <img src="https://user-images.githubusercontent.com/77184523/172041616-ac9bc6b8-b9ca-4931-bea9-cd6d6c459494.png" width="600" height="300"/>
   </div>
       <p></p>
3) 배송 순서 확인하기
    <div align="center">
    <img src="https://user-images.githubusercontent.com/77184523/172041617-c1763eb4-271d-4693-92ee-e12226ce992a.png" width="600" height="300"/>
   </div>
       <p></p>
4) T-map 연동하기
    <div align="center">
    <img src="https://user-images.githubusercontent.com/77184523/172041619-bd49d0e4-c133-41a3-babc-53a84929fd55.png" width="600" height="300"/>
   </div>
       <p></p>

### 공모전 제출용 유튜브 링크
https://youtu.be/HMigR5Y_JDQ <p></p>

## 대표 사진


<div align="center">
<img src="https://user-images.githubusercontent.com/77184523/172040704-345a80f1-b7e6-4b41-a604-9ede4b2b073e.png" width="600" height="300"/>
 <p></p>
<img src="https://user-images.githubusercontent.com/77184523/172041074-cdf777fd-eb0d-4961-af70-c2d701c0116a.jpg" width="400" height="500"/>
 </div>
 
 