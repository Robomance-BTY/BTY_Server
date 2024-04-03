package com.example.springjwt.dto;

import com.example.springjwt.entity.AdminEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    // UserEntity를 받아야 함으로, 생성자 초기화를 해준다.
    private final AdminEntity adminEntity;

    public CustomUserDetails(AdminEntity adminEntity) {

        this.adminEntity = adminEntity;
    }

    public AdminEntity getUserEntity() {
        return adminEntity;
    }
    public Integer getUserId() {
        return adminEntity.getId();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
// GrantedAuthority 컬렉션의 List 를 만들어준다.
        Collection<GrantedAuthority> collection = new ArrayList<>();
// GrantedAuthority 를 이용하여 AdminEntity 에서 Role 값만 뽑는다.
        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return adminEntity.getRole();
            }
        });
// 컬렉션 리턴
        return collection;
    }

    @Override
    public String getPassword() {

        return adminEntity.getAdminPassword();
    }

    @Override
    public String getUsername() {

        return adminEntity.getAdminId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}