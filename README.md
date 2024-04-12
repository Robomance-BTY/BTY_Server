3/27일 데이터 베이스에 쿼리문을 통해 Book_entity를 생성했고, 사용자가 대여를 하면, rental state를 1에서 0으로 바꿨다. 주의할점 쿼리문으로 만든 DB의 이름과 jpa로 만든 이름의 혼돈으로 문제가 발생할 수 있음.

3/28일 유저와 맞게 책을 빌리면 rentalTable에 업데이트가 되게 만들었다. 하지만, rental_Table에 rental_state는 삭제해야 할것 같고, Book_entity에서 Rentalstate가 0인 책은 빌릴 수 없게 제한을 다시 걸어야 할 것 같다.

3/29일 회원별 책 대여 제한 x, 이미 빌린책은 대여 x, 책 반납로직 o, 로봇의 상태를 확인하는 통신 미구현,

3/31일 Controller에서 Entity를 직접 받는 방식이 부적합하다고 판단하여, MappingMapper을 이용하여 DTO객체로 변환하는 방식으로 수정 반납로직을 이용시, DB에 Return 시간을 남기는 방식 추가 사용자별 빌린 책, 빌린 시간 확인 기능 추가

4월2일 login성공시 response Body에 userID를 반환

4월4일

user_entity 와 admin_entity로 분리(app,cms)
login_code, login_state,login_time으로 변경(used_time은 메소드를 호출하여 현재시간 - login_time)
book_entity(country,description,publishedDate 추가)
경로 분리
데이터 베이스 수정
rental 테스트 성공(예외 테스트를 좀 해봐야함)
return 테스트 성공
application 로그인 테스트 성공
admin 회원가입 및 로그인 테스트 성공
admin 권한으로 main페이지 접속 테스트 성공
4/5일

login시 로그인 시작시간 기록
사용자들의 현재까지 사용시간 요청 api 개발
대여중인 책 요청시 , 예약으로 넣고, 예약중인 책 반납하면, 대여리스트로 넘기는 로직 구현
각 파트에 걸맞는 return값 구현
Swagger 재연동
다음: Swagger 정리 및 각종 예외처리 테스트

4/11일

Notion에 api 명세서 업데이트
logout로직 구현



![image](https://github.com/Robomance-BTY/BTY_Server/assets/118197691/53254d74-8acf-4532-9348-8a354609cd45)



