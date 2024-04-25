package com.example.springjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDTO {
    private String status;
    private String locationInfo;
    private String message;
    private Long userId;

    // Getter와 Setter 생략
}
