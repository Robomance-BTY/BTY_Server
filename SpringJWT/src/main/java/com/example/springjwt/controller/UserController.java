//package com.example.springjwt.controller;
//
//import com.example.springjwt.dto.RentalDTO;
//import com.example.springjwt.entity.RentalEntity;
//import com.example.springjwt.service.BookRentalService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Slf4j
//@Tag(name = "User", description = "User 관련 API 입니다.")
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin
//@RequestMapping("/api/user")
//public class UserController {
//    private final BookRentalService bookRentalService;
//
//    @PostMapping("/rent")
//    public ResponseEntity<?> rentBook(@RequestBody RentalDTO rentalDTO) {
//
//        boolean success = bookRentalService.rentBook(rentalDTO);
//        log.info("getbookid = {},getuserid = {}, rentalStatie = {}",rentalDTO.getBookId(),rentalDTO.getUserId(),rentalDTO.getRentalState());
//        if (success) {
//
//            return ResponseEntity.ok().body("Book rented successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Book is already rented or does not exist.");
//        }
//    }
//
//    @PostMapping("/return")
//    public ResponseEntity<?> returnBook(@RequestBody RentalDTO rentRequestDto) {
//        boolean success = bookRentalService.returnBook(rentRequestDto.getBookId());
//        log.info("bookId = {} ",rentRequestDto.getBookId());
//        if (success) {
//            return ResponseEntity.ok().body("Book returned successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Book was not rented or does not exist.");
//        }
//    }
//
//    @GetMapping("/list/{userId}")
//    public List<RentalEntity> getUnreturnedRentalsByUserId(@PathVariable Long userId) {
//        return bookRentalService.getUnreturnedRentalsByUserId(userId);
//    }
//
//}