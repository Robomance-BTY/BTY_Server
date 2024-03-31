3/27일
데이터 베이스에 쿼리문을 통해 Book_entity를 생성했고, 사용자가 대여를 하면, rental state를 1에서 0으로 바꿨다.
주의할점 쿼리문으로 만든 DB의 이름과 jpa로 만든 이름의 혼돈으로 문제가 발생할 수 있음.

3/28일
유저와 맞게 책을 빌리면 rentalTable에 업데이트가 되게 만들었다.
하지만, rental_Table에 rental_state는 삭제해야 할것 같고,
Book_entity에서 Rentalstate가 0인 책은 빌릴 수 없게 제한을 다시 걸어야 할 것 같다.

3/29일
회원별 책 대여 제한 x, 이미 빌린책은 대여 x, 책 반납로직 o, 로봇의 상태를 확인하는 통신 미구현,

3/31일
Controller에서 Entity를 직접 받는 방식이 부적합하다고 판단하여, MappingMapper을 이용하여 DTO객체로 변환하는 방식으로 수정
반납로직을 이용시, DB에 Return 시간을 남기는 방식 추가
사용자별 빌린 책, 빌린 시간 확인 기능 추가
