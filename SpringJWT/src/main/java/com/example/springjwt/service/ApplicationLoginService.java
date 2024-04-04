package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
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
            // 조회결과가 있다.
            UserEntity userEntity = byLoginCode.get();
            if (userEntity.getLoginCode().equals(userDTO.getLoginCode())) {
                // 비밀번호 일치
                // 로그인 시간과 상태 업데이트
                userEntity.setLoginTime(new Date()); // 엔티티 내 로그인 시간 설정
                userEntity.setLoginState(Boolean.TRUE); // 엔티티 내 로그인 상태 설정

                // 엔티티의 변경사항을 데이터베이스에 저장
                userRepository.save(userEntity);

                // 엔티티 -> DTO 변환 후 리턴
                UserDTO userdto = UserDTO.toDTO(userEntity);
                return userdto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

        public long getUsedTimeInSeconds(Long id) {
            Optional<UserEntity> user = userRepository.findById(id);
            return user.map(UserEntity::getUsedTimeInSeconds).orElse(0L);
        }
    }



