package com.example.springjwt.dto;

import com.example.springjwt.entity.UserEntity;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String loginCode;
    private Boolean loginState;
    private Date loginTime;
    private Long usedTimeInSeconds;
    private Long usageFee;

    public static UserDTO toDTO(UserEntity userEntity){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setLoginCode(userEntity.getLoginCode());
        userDTO.setLoginState(userEntity.getLoginState());
        // LocalDate에서 Date로 타입 변경
        userDTO.setLoginTime(userEntity.getLoginTime());
        userDTO.setUsedTimeInSeconds(userEntity.getUsedTimeInSeconds());
        userDTO.setUsageFee(userEntity.getUsageFee());

        return userDTO;
    }
}
