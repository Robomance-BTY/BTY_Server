package com.example.springjwt.controller;

import com.example.springjwt.dto.*;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.ReservationEntity;
import com.example.springjwt.service.ApplicationLoginService;
import com.example.springjwt.service.BookRentalService;

import com.example.springjwt.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final MessageService messageService;
    public BookRentalController(BookRentalService bookRentalService, MessageService messageService) {
        this.bookRentalService = bookRentalService;

        this.messageService = messageService;
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
//
//    @PostMapping("/rentOrReserve")
//    public ResponseEntity<?> rentOrReserveBook(@RequestBody RentalDTO rentalDTO) {
//        try {
//            ResponseDTO response = bookRentalService.rentOrReserveBook(rentalDTO);
//            if (response.getStatus().equals("대여됨")) {
//                return ResponseEntity.ok().body(Map.of("message", "책이 성공적으로 대여되었습니다.", "locationInfo", response.getLocationInfo()));
//            } else if (response.getStatus().equals("예약됨")) {
//                return ResponseEntity.ok().body(Map.of("message", "책이 이미 대여 중이므로 예약되었습니다. "+response.getMessage(), "locationInfo", response.getLocationInfo()));
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 알 수 없는 오류가 발생했습니다.");
//            }
//        } catch (Exception e) {
//            log.error("Book rental or reservation failed", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("책 대여 또는 예약 실패");
//        }
//    }
    @PostMapping("/rentOrReserve")
    public ResponseEntity<?> rentOrReserveBook(@RequestBody RentalDTO rentalDTO) {
        try {
            ResponseDTO response = bookRentalService.rentOrReserveBook(rentalDTO);
            if (response.getStatus().equals("대여됨")) {
                messageService.assignRegion();
                messageService.rent(response.getLocationInfo(),rentalDTO.getUserId(),rentalDTO.getBookId(), messageService.getAssignedRegion());

                return ResponseEntity.ok().body(Map.of("message", "책이 성공적으로 대여되었습니다.", "locationInfo", response.getLocationInfo()));
            } else if (response.getStatus().equals("예약됨")) {

//                messageService.sendLocationInfoToSubscribers(response.getLocationInfo(),rentalDTO.getUserId());
//                messageService.rent(response.getLocationInfo(),response.getUserId(),rentalDTO.getBookId(), messageService.getAssignedRegion());

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
        Long bookId = rentRequestDto.getBookId();
        Long userId = rentRequestDto.getUserId(); // userId를 함께 받음
        log.info("return execution bookId = {}, userId = {}", bookId, userId);

        ResponseDTO response = bookRentalService.returnBook(bookId, userId);

        if (response.getStatus().equals("성공")) {
            // sendLocationInfoToSubscribers 메서드 호출 시 userId도 전달
//ddddd            messageService.sendLocationInfoToSubscribers(response.getLocationInfo(), response.getUserId());
            // Map을 사용하여 응답 바디 구성
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", response.getMessage());
            responseBody.put("userId", response.getUserId());

            return ResponseEntity.ok().body(responseBody);

        } else {
            // 실패한 경우도 메시지만 전달
            Map<String, String> errorBody = new HashMap<>();
            errorBody.put("message", response.getMessage());
            return ResponseEntity.badRequest().body(errorBody);
        }
    }

//
//    @PostMapping("/return")
//    public ResponseEntity<?> returnBook(@RequestBody RentalDTO rentRequestDto) {
//        Long bookId = rentRequestDto.getBookId();
//        Long userId = rentRequestDto.getUserId(); // userId를 함께 받음
//        log.info("bookId = {}, userId = {}", bookId, userId);
//
//        ResponseDTO response = bookRentalService.returnBook(bookId, userId);
//
//        if (response.getStatus().equals("성공")) {
//            if (response.getMessage().contains("예약된 사용자에게")) {
//                // 예약된 사용자에게 대여된 경우
//                messageService.sendLocationInfoToSubscribers(response.getLocationInfo(), response.getUserId());
//            } else {
//                // 반납만 된 경우
//                messageService.sendLocationInfoToSubscribers(null, response.getUserId());
//            }
//
//            // Map을 사용하여 응답 바디 구성
//            Map<String, Object> responseBody = new HashMap<>();
//            responseBody.put("message", response.getMessage());
//            responseBody.put("userId", response.getUserId());
//
//            return ResponseEntity.ok().body(responseBody);
//        } else {
//            // 실패한 경우도 메시지만 전달
//            Map<String, String> errorBody = new HashMap<>();
//            errorBody.put("message", response.getMessage());
//            return ResponseEntity.badRequest().body(errorBody);
//        }
//    }
//

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


//    private void sendLocationInfoToSubscribers(String locationInfo) {
//        simpMessageSendingOperations.convertAndSend("/sub/channel/eddy", locationInfo);

    @PostMapping("/testMethodForReserve")
    public Boolean testMethod(@RequestBody RentalDTO rentalDTO){
        return bookRentalService.testMethod(rentalDTO);
    }


    @PostMapping("/testMethodForReturn")
        public ResponseEntity<?> getRentalStatusAndFirstReservation(@RequestBody RentalDTO rentalDTO) {
            try {
                RentalAndReservationInfoDto infoDto = bookRentalService.getRentalStatusAndFirstReservation(rentalDTO.getBookId());
                return new ResponseEntity<>(infoDto, HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }

    @PostMapping("/cancelReserve")
    public List<ReservationEntity> cancelReservations(@RequestBody RentalDTO rentalDTO) {
        return bookRentalService.cancelAllReservationsByUserId(rentalDTO.getUserId());
    }

    @PostMapping("users-by-book")
    public Map<String, Long> getUserIdByBookId(@RequestBody RentalDTO rentalDTO) {
        List<Long> userIds = bookRentalService.getUserIdsByBookId(rentalDTO.getBookId());
        Map<String, Long> response = new HashMap<>();

        if (!userIds.isEmpty()) {
            response.put("userId", userIds.get(0));  // 첫 번째 사용자 ID를 반환
        }
        return response;
    }
    }





