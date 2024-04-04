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
import org.modelmapper.ModelMapper;
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
            return null;
        }
    }


    @PostMapping("/usedTime")
    public Map<String, Long> getUsedTimeInSeconds(@RequestBody UserDTO userDTO) {
        long usedTime = applicationLoginService.getUsedTimeInSeconds(userDTO.getId());
        Map<String, Long> response = new HashMap<>();
        response.put("usedTime", usedTime);
        return response;
    }

}
