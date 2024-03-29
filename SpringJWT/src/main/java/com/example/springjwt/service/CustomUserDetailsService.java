package com.example.springjwt.service;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    //DB에서 조회를 해야하기 때문에, 생성자 주입을 해여한다.
    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회를 해야하기 때문에,
        UserEntity userData = userRepository.findByUsername(username);

        if (userData != null) {

            //UserDetails를 이용하기 위해 커스텀된 CustomUserDetails에게 값을 넘겨준다.
            //그럼 UserDetails에서 authenticationManager에게 넘겨준다.
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
