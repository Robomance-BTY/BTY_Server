package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.service.BookRentalService;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Slf4j
@RestController
@RequestMapping("application/api")
public class BookRentalController {
    private final BookRentalService bookRentalService;

    public BookRentalController(BookRentalService bookRentalService) {
        this.bookRentalService = bookRentalService;
    }


    @Operation(
            summary = "대여 요청",
            description = "대여 요청 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자의 책 대여 요청",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RentalEntity.class)
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Book rented successfully"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Book is already rented or does not exist."
    )
    @PostMapping("/rent")
    public ResponseEntity<?> rentBook(@RequestBody RentalDTO rentalDTO) {
        try {
            BookEntity rentedBook = bookRentalService.rentBook(rentalDTO);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Book rented successfully.");
            responseBody.put("locationInfo", rentedBook.getLocationInfo());
            return ResponseEntity.ok().body(responseBody);
        } catch (EntityActionVetoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
//마지막꺼
//    @PostMapping("/rent")
//    public ResponseEntity<?> rentBook(@RequestBody RentalDTO rentalDTO) {
//        BookEntity rentedBook = bookRentalService.rentBook(rentalDTO);
//        if (rentedBook != null) {
//            Map<String, Object> responseBody = new HashMap<>();
//            responseBody.put("message", "Book rented successfully.");
//            responseBody.put("locationInfo", rentedBook.getLocationInfo());
//            return ResponseEntity.ok().body(responseBody);
//        } else {
//            return ResponseEntity.badRequest().body("Book is already rented or does not exist.");
//        }



//    @PostMapping("/rent")
//    public ResponseEntity<?> rentBook(@RequestBody RentalDTO rentalDTO) {
//        boolean success = bookRentalService.rentBook(rentalDTO);
//        log.info("getbookid = {},getuserid = {}, rentalStatie = {}", rentalDTO.getBookId(), rentalDTO.getUserId(), rentalDTO.getRentalState());
//        if (success) {
//            return ResponseEntity.ok().body("Book rented successfully.");
//        } else {
//            return ResponseEntity.badRequest().body("Book is already rented or does not exist.");
//        }
//    }

    @Operation(
            summary = "도서 반납",
            description = "특정 사용자가 도서를 반납하는 API입니다. Authorization 헤더에 토큰 값을 포함해야 합니다.",
            parameters = {
                    @Parameter(name = "Authorization", required = true, description = "Bearer [Token]", schema = @Schema(type = "string"))
            }
    )
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody RentalDTO rentRequestDto) {
        boolean success = bookRentalService.returnBook(rentRequestDto.getBookId());
        log.info("bookId = {}", rentRequestDto.getBookId());
        if (success) {
            return ResponseEntity.ok().body("Book returned successfully.");
        } else {
            return ResponseEntity.badRequest().body("Book was not rented or does not exist.");
        }
    }

    @Operation(
            summary = "미반납 도서 목록 조회",
            description = "특정 사용자의 미반납 도서 목록을 조회하는 API입니다. Authorization 헤더에 토큰 값을 포함해야 합니다.",
            parameters = {
                    @Parameter(name = "Authorization", required = true, description = "Bearer [Token]", schema = @Schema(type = "string"))
            }
    )
    @GetMapping("/list/{userId}")
    public List<RentalEntity> getUnreturnedRentalsByUserId(@PathVariable Long userId) {
        return bookRentalService.getUnreturnedRentalsByUserId(userId);
    }
}
