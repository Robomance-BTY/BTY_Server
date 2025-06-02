package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.service.ApplicationLoginService;
import com.example.springjwt.service.UserTimeFeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@Tag(name = "앱 로그인 및 시간", description = "로그인 동작과 사용시간 확인 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/application")
public class ApplicationLoginController {

    private final ApplicationLoginService applicationLoginService;
    private final UserTimeFeeService userTimeFeeService;


    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO){
        UserDTO loginResult = applicationLoginService.login(userDTO);
        if(loginResult != null) {
            log.info("login = {} success,",loginResult);
            //login 성공
            return ResponseEntity.ok(loginResult);
        } else {
            log.info("login fail");
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody UserDTO userDTO) {
        UserDTO logoutResult = applicationLoginService.logout(userDTO);
        if (logoutResult != null) {
            // 사용자의 사용 시간과 요금을 계산합니다.
            long usedTime = userTimeFeeService.getUsedTimeInSeconds(logoutResult);
            long usageFee = userTimeFeeService.getUsageFee(logoutResult);

            Map<String, Object> response = new HashMap<>();
            response.put("logoutResult", logoutResult);
            response.put("usedTime", usedTime);
            response.put("usageFee", usageFee); // 요금 정보를 응답에 포함시킵니다.

            log.info("logout = {} success, usedTime = {}, usageFee = {}", logoutResult, usedTime, usageFee);
            return ResponseEntity.ok(response);
        } else {
            log.info("logout fail");
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/usedTime")
    public ResponseEntity<?> getUsedTimeInSeconds(@RequestBody UserDTO userDTO) {
        long usedTime = userTimeFeeService.getUsedTimeInSeconds(userDTO); // 수정된 부분

        if (usedTime > 0) {
            Map<String, Long> response = new HashMap<>();
            response.put("usedTime", usedTime);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
