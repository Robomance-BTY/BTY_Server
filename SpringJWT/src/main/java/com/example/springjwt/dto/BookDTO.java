package com.example.springjwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

    private String title;
    private String author;
    private String genre;
    private String locationInfo;
    private String rentalState;
}
