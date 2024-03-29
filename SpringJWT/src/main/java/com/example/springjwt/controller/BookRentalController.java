package com.example.springjwt.controller;

import com.example.springjwt.dto.BookDTO;
import com.example.springjwt.dto.RentRequestDto;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.service.BookRentalService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class BookRentalController {
    private final BookRentalService bookRentalService;

    public BookRentalController(BookRentalService bookRentalService) {
        this.bookRentalService = bookRentalService;
    }


    @PostMapping("/rent")
    public ResponseEntity<?> rentBook(@RequestBody RentalEntity rentalEntity) {

//        boolean rentalState = rentalEntity.getRentalState() == null || Boolean.TRUE.equals(rentalEntity.getRentalState());
        boolean success = bookRentalService.rentBook(rentalEntity.getUserId()
                ,rentalEntity.getBookId()
                ,rentalEntity.getRentalState());
        log.info("getbookid = {},getuserid = {}, rentalStatie = {}",rentalEntity.getBookId(),rentalEntity.getId(),rentalEntity.getRentalState());
        if (success) {
            return ResponseEntity.ok().body("Book rented successfully.");
        } else {
            return ResponseEntity.badRequest().body("Book is already rented or does not exist.");
        }
    }
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody RentRequestDto rentRequestDto) {
        boolean success = bookRentalService.returnBook(rentRequestDto.getBookId());
        log.info("bookId = {} ",rentRequestDto.getBookId());
        if (success) {
            return ResponseEntity.ok().body("Book returned successfully.");
        } else {
            return ResponseEntity.badRequest().body("Book was not rented or does not exist.");
        }
    }
}
//2번째
//@RestController
//@RequestMapping("/api")
//public class BookRentalController {
//
//    @Autowired
//    private BookRentalService bookRentalService;
//
//    @PostMapping("/rent")
//    public ResponseEntity<?> rentBook(@RequestBody RentRequestDto rentRequest) {
//        try {
//            boolean rented = bookRentalService.rentBook(rentRequest.getUserId(), rentRequest.getBookId());
//            if (!rented) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("This book is already rented.");
//            }
//            return ResponseEntity.ok().body("Book rented successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
//        }
//    }
//}

//@Slf4j
//@RestController
//@RequestMapping("/api")
//public class RentalController {
//
//    @Autowired
//    private final RentalService rentalService;
//
//
//    public RentalController(RentalService rentalService) {
//        this.rentalService = rentalService;
//    }
//
//    @PostMapping("/rent")
//    public ResponseEntity<String> rentBook(@RequestBody Map<String, String> request) {
//        String title = request.get("title");
//        rentalService.rentBookByTitle(title);
//        return ResponseEntity.ok("대여 처리가 완료되었습니다.");
//    }
//}
