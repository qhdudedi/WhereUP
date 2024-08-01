# WhereUP
> **수도권 내 팝업 스토어 정보를 제공해주는 서비스** <br/> **개발기간: 2024.04 ~ 2024.06**

## 프로젝트 소개
WhereUP은 수도권 내 팝업 스토어 정보를 제공해주는 서비스입니다.
팝업 스토어 정보는 주로 SNS를 통해 이루어짐에 있어서 정보 접근의 제한성과 짧은 기간에서 놓치는 정보의 휘발성을 보완하고자 서비스를 제공하게 되었습니다.

---

## Stacks

### Development
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6db33f?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6db33f?style=for-the-badge&logo=springsecurity&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-e34f26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572b6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

### Architecture
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-ff9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![AWS_Lambda](https://img.shields.io/badge/AWS_Lambda-FF9900?style=for-the-badge&logo=amazon&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![Amazon_Route_53](https://img.shields.io/badge/Amazon_Route_53-8c4fff?style=for-the-badge&logo=amazonroute53&logoColor=white)
![Amazon CloudFront](https://img.shields.io/badge/Amazon_CloudFront-8C4FFF?style=for-the-badge&logo=cloudfront&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)

### DataBase
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Amazon RDS](https://img.shields.io/badge/Amazon_RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)

### Environment
![IntelliJ](https://img.shields.io/badge/IntelliJ-000000?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)

### ETC
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)

## 주요 기능

### ⭐️ OAuth2.0을 이용한 소셜로그인
- Naver / Kakao 소셜 로그인 기능

### ⭐️ 팝업 스토어 정보 검색 및 필터링
- 팝업 스토어의 위치, 운영 기간, 행사 내용 정보 제공
- 특정 키워드로 팝업 스토어를 검색할 수 있는 기능
- 지역별 필터링 옵션

### ⭐️ 팝업 스토어 후기 및 커뮤니티 기능
- Markdown 형식으로 팝업 방문 후기를 남기며 커뮤니티 형성

### ⭐️ 일정 관리 기능
- 가고자 하는 팝업 일정 등록 및 관리

### ⭐️ 알림 서비스 
- 관심 카테고리를 등록하여, 신규 팝업 등록 알람이
---
## 아키텍쳐


### 디렉토리 구조
```
📦 WhereUP
├─ .gitignore
├─ README.md
├─ build.gradle
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.properties
├─ gradlew
├─ gradlew.bat
├─ scripts
│  └─ deploy.sh
├─ settings.gradle
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ project
   │  │        └─ whereup
   │  │           ├─ WhereUpApplication.java
   │  │           ├─ board
   │  │           │  ├─ controller
   │  │           │  │  └─ BoardController.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ Board.java
   │  │           │  │  ├─ BoardImage.java
   │  │           │  │  ├─ BoardLike.java
   │  │           │  │  └─ Category.java
   │  │           │  ├─ dto
   │  │           │  │  ├─ BoardDesc.java
   │  │           │  │  ├─ BoardRequestDto.java
   │  │           │  │  └─ BoardSummary.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ BoardImageRepository.java
   │  │           │  │  ├─ BoardLikeRepository.java
   │  │           │  │  └─ BoardRepository.java
   │  │           │  └─ service
   │  │           │     ├─ BoardLikeService.java
   │  │           │     └─ BoardService.java
   │  │           ├─ calendar
   │  │           │  ├─ controller
   │  │           │  │  ├─ CalendarApiController.java
   │  │           │  │  └─ CalendarViewController.java
   │  │           │  ├─ dto
   │  │           │  │  └─ CalendarRequestDto.java
   │  │           │  ├─ entity
   │  │           │  │  └─ Calendar.java
   │  │           │  ├─ repository
   │  │           │  │  └─ CalendarRepository.java
   │  │           │  └─ service
   │  │           │     └─ CalendarService.java
   │  │           ├─ oauth
   │  │           │  ├─ KakaoUserInfo.java
   │  │           │  ├─ NaverUserInfo.java
   │  │           │  ├─ OAuth2Response.java
   │  │           │  ├─ PrincipalDetails.java
   │  │           │  └─ PrincipalOauth2UserService.java
   │  │           ├─ post
   │  │           │  ├─ controller
   │  │           │  │  ├─ CommentController.java
   │  │           │  │  └─ PostController.java
   │  │           │  ├─ domain
   │  │           │  │  ├─ Comment.java
   │  │           │  │  └─ Post.java
   │  │           │  ├─ dto
   │  │           │  │  └─ PostSummary.java
   │  │           │  ├─ repository
   │  │           │  │  ├─ CommentRepository.java
   │  │           │  │  └─ PostRepository.java
   │  │           │  └─ service
   │  │           │     ├─ CommentService.java
   │  │           │     ├─ MarkdownService.java
   │  │           │     └─ PostService.java
   │  │           ├─ s3
   │  │           │  ├─ config
   │  │           │  │  └─ S3Config.java
   │  │           │  ├─ controller
   │  │           │  │  └─ S3Controller.java
   │  │           │  └─ service
   │  │           │     └─ S3Service.java
   │  │           ├─ search
   │  │           │  └─ controller
   │  │           │     └─ SearchController.java
   │  │           ├─ telegram
   │  │           │  ├─ controller
   │  │           │  │  └─ TelegramController.java
   │  │           │  ├─ domain
   │  │           │  │  └─ TelegramUserInfo.java
   │  │           │  ├─ repository
   │  │           │  │  └─ TelegramUserInfoRepository.java
   │  │           │  └─ service
   │  │           │     └─ TelegramService.java
   │  │           └─ user
   │  │              ├─ config
   │  │              │  └─ SecurityConfig.java
   │  │              ├─ controller
   │  │              │  ├─ UserApiController.java
   │  │              │  └─ UserViewController.java
   │  │              ├─ dto
   │  │              │  └─ request
   │  │              │     └─ UserRequestDto.java
   │  │              ├─ entity
   │  │              │  ├─ User.java
   │  │              │  └─ eum
   │  │              │     └─ Role.java
   │  │              ├─ repository
   │  │              │  └─ UserRepository.java
   │  │              └─ service
   │  │                 ├─ UserDetailService.java
   │  │                 └─ UserService.java
   │  └─ resources
   │     ├─ application.yml
   │     ├─ ssl
   │     │  └─ www.whereupp.com_2024052192E55.pfx
   │     ├─ static
   │     │  ├─ css
   │     │  │  ├─ board.css
   │     │  │  ├─ boardAndPost.css
   │     │  │  ├─ fullcalendar.css
   │     │  │  ├─ headerAndFooter.css
   │     │  │  ├─ main.css
   │     │  │  ├─ mypage.css
   │     │  │  ├─ origin.css
   │     │  │  └─ post.css
   │     │  ├─ img
   │     │  │  ├─ icon
   │     │  │  │  ├─ map.png
   │     │  │  │  ├─ review.png
   │     │  │  │  └─ user.png
   │     │  │  ├─ kakao.png
   │     │  │  ├─ nav_icon.png
   │     │  │  ├─ naver.png
   │     │  │  └─ whereup.png
   │     │  ├─ js
   │     │  │  ├─ calendar.js
   │     │  │  ├─ whereup-boardImgNext.js
   │     │  │  ├─ whereup-postValidCheck.js
   │     │  │  ├─ whereup-postWithComment.js
   │     │  │  └─ whereup-simplemdeCustom.js
   │     │  └─ kakao_login_m.png
   │     └─ templates
   │        ├─ adminInsertBoard.html
   │        ├─ board.html
   │        ├─ boardList.html
   │        ├─ calendar.html
   │        ├─ filteringBoard.html
   │        ├─ fragments
   │        │  ├─ footer.html
   │        │  └─ header.html
   │        ├─ index.html
   │        ├─ login.html
   │        ├─ mypage.html
   │        ├─ newPost.html
   │        ├─ post.html
   │        ├─ postList.html
   │        ├─ search.html
   │        ├─ signup.html
   │        └─ updatePost.html
   
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)