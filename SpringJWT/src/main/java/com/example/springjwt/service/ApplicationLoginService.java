package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationLoginService {

    private final UserRepository userRepository;

    public ApplicationLoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserDTO login(UserDTO userDTO) {
        Optional<UserEntity> byLoginCode = userRepository.findByLoginCode(userDTO.getLoginCode());
        if (byLoginCode.isPresent()) {
            //조회결과가 있다.
            UserEntity userEntity = byLoginCode.get();
            if (userEntity.getLoginCode().equals(userDTO.getLoginCode())) {
                // 비밀번호 일치
                // entity -> member 변환 후 리턴
                UserDTO userdto = UserDTO.toDTO(userEntity);
                return userdto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
