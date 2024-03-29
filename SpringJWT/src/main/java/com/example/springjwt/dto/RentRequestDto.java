package com.example.springjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RentRequestDto {
    private Long bookId; // 대여하고자 하는 아이템의 ID
    private Date startDate; // 대여 시작 날짜
    private Date endDate; // 대여 종료 날짜
    private Long userId; // 대여 요청자의 사용자 ID

    // 기본 생성자, 모든 인자를 받는 생성자, getter 및 setter 생략

    public RentRequestDto(){}

}
