# 🍽️ Gurakbu Delivery (구락부 딜리버리)

🚀 **"맛집을 내 손 안에, 빠르고 간편하게!"**  
Gurakbu Delivery는 사용자와 음식점을 연결하는 배달 서비스입니다.  
사용자는 메뉴를 조회하고 주문할 수 있으며, 사장님은 자신의 가게와 메뉴를 관리할 수 있습니다.

---

## 📢 주요 기능

### 1️⃣ **사용자 기능**
- 🔹 회원가입 & 로그인 (JWT 인증)
- 🔹 가게 & 메뉴 조회
- 🔹 음식 주문 및 결제

### 2️⃣ **사장님 기능**
- 🔹 가게 등록 및 수정
- 🔹 메뉴 추가/수정/삭제
- 🔹 주문 관리

### 3️⃣ **관리자 기능**
- 🔹 유저 관리
- 🔹 주문 & 가게 모니터링

---

## 🛠 **Wireframe**
| 1.로그인 | 2. USER회원가입 | 3. OWNER회원가입 |
|---|---|---|
|![image](https://github.com/user-attachments/assets/33009aa7-a86a-4364-b09f-d8f714066f8a)|![image](https://github.com/user-attachments/assets/f2bf4074-baf1-48b2-8b2f-77a35fdb300b)|![image](https://github.com/user-attachments/assets/df79faf5-a36d-4220-8a8f-6be0e390604c)|

| 4. USER 회원정보 수정/탈퇴 | 5. OWNER 회원정보 수정/탈퇴 | 6. USER 주문 |
|---|---|---|
|![image](https://github.com/user-attachments/assets/c3d04e3f-7db2-4a1b-8994-0894e03ca230)|![image](https://github.com/user-attachments/assets/9cbeabb7-b6f4-40f4-8469-ca9003c80402)|![image](https://github.com/user-attachments/assets/06a5b440-819f-44b2-9fd3-5d72dc5a6ecd)|

| 7. USER 주문내역 | 8. OWNER 주문수락| 9. USER 리뷰작성 |
|---|---|---|
|![image](https://github.com/user-attachments/assets/5c759473-9cfe-4d5d-b899-79fadecd7d72)|![image](https://github.com/user-attachments/assets/b5aa2b39-c983-4746-989f-7d0f1aa3976c)|![image](https://github.com/user-attachments/assets/3afcbb7d-b23d-4156-b9b6-82cb21161694)|

| 10. OWNER 리뷰관리 | 11. OWNER 매장등록/삭제 | 12. OWNER 메뉴 등록/삭제 |
|---|---|---|
|![image](https://github.com/user-attachments/assets/f93b3bba-cbb2-49c3-94fb-5003b7fff32d)|![image](https://github.com/user-attachments/assets/46fc4626-88f1-4148-8753-0bb4d543644e)|![image](https://github.com/user-attachments/assets/ff575966-e6ee-42ff-9f9d-d4994ca14d29)|

---

## 🛠 **API Reference**
### Auth
![image](https://github.com/user-attachments/assets/55d31785-22cf-4ac1-bed4-7baf019c1e67)

### User
![image](https://github.com/user-attachments/assets/fc93b09f-7e20-4b35-884d-2ad3d5541eda)

### Restaurant
![image](https://github.com/user-attachments/assets/cdd91dc3-f768-4831-911d-d1c65d40e08d)

### Menu
![image](https://github.com/user-attachments/assets/dee5457c-c491-46a6-baae-1d804f5ddb3a)

### Order
![image](https://github.com/user-attachments/assets/057e43e1-05cc-48aa-84e8-25ce4d0ed7d4)

### Review
![image](https://github.com/user-attachments/assets/079c69d3-826b-455a-acde-6b30f0d38b38)

---

## 🛠 **ERD**
![image](https://github.com/user-attachments/assets/b8fd5eb5-4654-406e-9646-1b48facf07f0)

---
## 🛠 **Test Coverage**
이미지

---
## 🛠 **기술 스택**

| 기술  | 사용 목적 |
|-------|--------|
| **Spring Boot 3** | 백엔드 프레임워크 |
| **Spring Security & JWT** | 사용자 인증 및 권한 관리 |
| **JPA (Hibernate)** | 데이터베이스 ORM |
| **MySQL** | 개발/운영 환경 DB |
| **Lombok** | 코드 간결화 |
| **Postman** | API 테스트 |
| **Swagger** | API 문서 자동화 |

---

## 🔥 **팀원 소개**

| 이름 | 담당 기능 |
|------|------------------------------|
| **[김한이](https://github.com/kim-hani)** | 주문, 리뷰 (메뉴 주문, 주문 수락, 주문 요청 및 상태 변경, 리뷰 생성/수정/조회) |
| **[민혜원](https://github.com/Heni0717)** | 가게 (가게 생성: 3개 제한 & 폐업 가게 카운팅 제외, 가게 수정, 단건/다건/카테고리별 목록 조회, 가게 폐업) |
| **[김원준](https://github.com/Kim-WonJoon)** | 회원 (회원가입, 회원탈퇴, 로그인/로그아웃) |
| **[김혜민](https://github.com/learner-nosilv)** | 메뉴 (메뉴 생성, 수정, 조회(단독 조회 X, 가게와 함께 조회), 삭제) |
