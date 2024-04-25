package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.*;
import com.example.springjwt.service.AdminService;
import com.example.springjwt.service.BookRentalService;
import com.example.springjwt.service.JoinService;
import com.example.springjwt.service.UserTimeFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@CrossOrigin
@Tag(name = "JWT 확인", description = "JWT의 작동을 확인하는 API")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final JoinService joinService;
    private final AdminService adminService;

    private final BookRentalService bookRentalService;
    private final UserTimeFeeService userTimeFeeService;
    public AdminController(JoinService joinService, AdminService adminService, BookRentalService bookRentalService, UserTimeFeeService userTimeFeeService) {

        this.joinService = joinService;
        this.adminService = adminService;
        this.bookRentalService = bookRentalService;

        this.userTimeFeeService = userTimeFeeService;
    }


    @Operation(
            summary = "JWT 확인하는 api",
            description = "JWT 확인을 위해 헤더의 Authorization에 토큰 값을 설정 하여 요청 합니다.",
            parameters = {
                    @Parameter(name = "Authorization", required = true, description = "Bearer [Token]", schema = @Schema(type = "string"))
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = " "
    )

    @GetMapping("/main/api")
    public String admin() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return "Main Controller, name = " + name + " Role = " + role;
    }


    @Operation(
            summary = "회원가입",
            description = "회원가입 API",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회원가입을 위한 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = JoinDTO.class)
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "ok"
    )
    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) {

        System.out.println(joinDTO.getUsername());
        joinService.joinProcess(joinDTO);

        return "ok";
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        List<UserEntity> userEntities = adminService.getAlUserInfo();
        return ResponseEntity.ok(userEntities);
    }


    @GetMapping("/orderList")
    public ResponseEntity<List<OrderEntity>> getAllOrder(){
        List<OrderEntity> orderEntities = adminService.getAllOrders();
        return ResponseEntity.ok(orderEntities);
    }

    @GetMapping("/rentalList")
    public ResponseEntity<List<RentalEntity>> getAllRentalList(){
        List<RentalEntity> allRentals = adminService.getAllRentals();
        return ResponseEntity.ok(allRentals);
    }

    @GetMapping("/reservationList")
    public ResponseEntity<List<ReservationEntity>> getAllReservationList(){
        List<ReservationEntity> allReservation = adminService.getAllReservations();
        return ResponseEntity.ok(allReservation);
    }


    @GetMapping("/usingUser")
    public ResponseEntity<List<UserDTO>> getAllLoggedInUsersInfo() {
        List<UserDTO> loggedInUsersInfo = userTimeFeeService.getAllLoggedInUsersInfo();
        return ResponseEntity.ok(loggedInUsersInfo);
    }
}


