package com.example.springjwt.controller;

import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.service.ApplicationLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/application")
public class ApplicationLoginController {

    private final ApplicationLoginService applicationLoginService;

    @PostMapping("/login")
    public Long login(@RequestBody UserDTO userDTO){
        UserDTO loginResult = applicationLoginService.login(userDTO);
        if(loginResult != null) {
            log.info("login = {} success,",loginResult);
            //login 성공
            return userDTO.getId();
        } else {
            log.info("login fail");
            return null;
        }
    }

}
