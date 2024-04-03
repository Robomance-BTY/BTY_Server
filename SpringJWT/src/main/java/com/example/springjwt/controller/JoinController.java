//package com.example.springjwt.controller;
//
//import com.example.springjwt.dto.JoinDTO;
//import com.example.springjwt.service.JoinService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//
//@Tag(name = "User", description = "User 관련 API 입니다.")
//@RestController
//@CrossOrigin
//@RequestMapping("/admin")
//public class JoinController {
//
//
//    private final JoinService joinService;
//
//    public JoinController(JoinService joinService) {
//
//        this.joinService = joinService;
//    }
//
//
//    @Operation(
//            summary = "회원가입",
//            description = "회원가입 API",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    description = "회원가입을 위한 정보",
//                    required = true,
//                    content = @Content(
//                            schema = @Schema(implementation = JoinDTO.class)
//                    )
//            )
//    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "ok"
//    )
//    @PostMapping("/join")
//    public String joinProcess(JoinDTO joinDTO) {
//
//        System.out.println(joinDTO.getUsername());
//        joinService.joinProcess(joinDTO);
//
//        return "ok";
//    }
//}
