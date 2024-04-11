package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.service.ApplicationLoginService;
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
    public ResponseEntity<?> logout(@RequestBody UserDTO userDTO){
        UserDTO logoutResult = applicationLoginService.logout(userDTO);
        if(logoutResult != null) {
            // 사용자의 사용 시간을 계산
            long usedTime = applicationLoginService.getUsedTimeInSeconds(userDTO.getId());

            // 로그아웃 정보와 사용 시간을 함께 담을 Map 생성
            Map<String, Object> response = new HashMap<>();
            response.put("logoutResult", logoutResult);
            response.put("usedTime", usedTime);

            log.info("logout = {} success, usedTime = {}", logoutResult, usedTime);
            // 로그인 성공 및 사용 시간 포함하여 반환
            return ResponseEntity.ok(response);
        } else {
            log.info("logout fail");
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/usedTime")
    public ResponseEntity<?> getUsedTimeInSeconds(@RequestBody UserDTO userDTO) {
        long usedTime = applicationLoginService.getUsedTimeInSeconds(userDTO.getId());

        // 사용자의 사용 시간이 0이 아니라면 성공으로 간주합니다.
        // 여기서는 0을 실패의 조건으로 가정하고 있습니다.
        // 실제 조건은 applicationLoginService.getUsedTimeInSeconds 메서드의 구현에 따라 다를 수 있습니다.
        if (usedTime > 0) {
            Map<String, Long> response = new HashMap<>();
            response.put("usedTime", usedTime);
            return ResponseEntity.ok(response);
        } else {
            // 사용자의 사용 시간을 가져오는 데 실패했을 경우, 즉, 사용자 ID에 해당하는 사용자가 없을 경우 404 상태 코드를 반환합니다.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    }

