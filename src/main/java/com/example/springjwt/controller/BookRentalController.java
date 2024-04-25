package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.dto.ResponseDTO;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.service.ApplicationLoginService;
import com.example.springjwt.service.BookRentalService;

import io.swagger.v3.oas.annotations.tags.Tag;
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

@CrossOrigin
@Tag(name = "책 관리", description = "책 대여,예약,반납 API")
@Slf4j
@RestController
@RequestMapping("/application/api")
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

    @PostMapping("/rentOrReserve")
    public ResponseEntity<?> rentOrReserveBook(@RequestBody RentalDTO rentalDTO) {
        try {
            ResponseDTO response = bookRentalService.rentOrReserveBook(rentalDTO);
            if (response.getStatus().equals("대여됨")) {
                return ResponseEntity.ok().body(Map.of("message", "책이 성공적으로 대여되었습니다.", "locationInfo", response.getLocationInfo()));
            } else if (response.getStatus().equals("예약됨")) {
                return ResponseEntity.ok().body(Map.of("message", "책이 이미 대여 중이므로 예약되었습니다. "+response.getMessage(), "locationInfo", response.getLocationInfo()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 알 수 없는 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            log.error("Book rental or reservation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("책 대여 또는 예약 실패");
        }
    }



    @Operation(
            summary = "도서 반납",
            description = "특정 사용자가 도서를 반납하는 API입니다. Authorization 헤더에 토큰 값을 포함해야 합니다.",
            parameters = {
                    @Parameter(name = "Authorization", required = true, description = "Bearer [Token]", schema = @Schema(type = "string"))
            }
    )
    @PostMapping("/return")
    public ResponseEntity<?> returnBook(@RequestBody RentalDTO rentRequestDto) {
        // RentalDTO에서 bookId 추출
        Long bookId = rentRequestDto.getBookId();
        log.info("bookId = {}", bookId);

        // 서비스에서 책 반납 처리 후 결과 받기
        ResponseDTO response = bookRentalService.returnBook(bookId);

        // 반환된 ResponseDTO의 상태에 따라 응답 분기 처리
        if (response.getStatus().equals("성공")) {
            // 반납 성공 혹은 예약된 사용자에게 대여 성공 시
            return ResponseEntity.ok().body(response.getMessage() + "userId: " + response.getUserId() +"location_info:" + response.getLocationInfo());
        } else {
            // 반납 실패 시 (책이 대여 중이지 않거나, 존재하지 않는 경우)
            return ResponseEntity.badRequest().body(response.getMessage());
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
