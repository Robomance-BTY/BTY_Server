package com.example.springjwt.dto;

import com.example.springjwt.entity.UserEntity;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String loginCode;
    private Boolean loginState;
    private LocalDate loginTime;



    public static UserDTO toDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setLoginCode(userEntity.getLoginCode());
        userDTO.setLoginState(userEntity.getLoginState());
        userDTO.setLoginTime(userEntity.getLoginTime());
        return userDTO;
    }
}
