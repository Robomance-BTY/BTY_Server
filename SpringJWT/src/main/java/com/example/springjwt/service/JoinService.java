package com.example.springjwt.service;


import com.example.springjwt.dto.JoinDTO;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // joinProcess 와 같은 회원가입 서비스는 보통 보이드가 아닌 bool 값으로 선언한다.
    public void joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {

            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        // 비밀번호를 생성시 bCryptPasswordEncoder 이용하여 암호를 해쉬화 해야한다.
        data.setRole("ROLE_USER");
        // 스프링은 Role 권한을 줄때 앞에 ROLE_ 이라는 접두사를 붙이고 그 뒤에 역할을 써주면 된다.

        userRepository.save(data);
    }


}
