package com.example.springjwt.service;

import com.example.springjwt.dto.CustomUserDetails;
import com.example.springjwt.entity.AdminEntity;
import com.example.springjwt.repository.AdminLoginRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminLoginRepository adminLoginRepository;
    //DB에서 조회를 해야하기 때문에, 생성자 주입을 해여한다.
    public CustomUserDetailsService(AdminLoginRepository adminLoginRepository) {

        this.adminLoginRepository = adminLoginRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //DB에서 조회를 해야하기 때문에,
        AdminEntity userData = adminLoginRepository.findByAdminId(username);

        if (userData != null) {

            //UserDetails를 이용하기 위해 커스텀된 CustomUserDetails에게 값을 넘겨준다.
            //그럼 UserDetails에서 authenticationManager에게 넘겨준다.
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
