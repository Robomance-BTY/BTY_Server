package com.example.springjwt.repository;

import com.example.springjwt.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLoginRepository extends JpaRepository<AdminEntity, Integer> {
//데이터 베이스에 같은 username 이 있는지 확인해주는 쿼리 메소드 기능이다. JPA 는 메소드의 이름만 보고
// SQL 쿼리를 자동으로 생성하는 기능을 수행한다.
    Boolean existsByAdminId(String username);

// DB에서 데이터를 조회하여 UserDetailsService에게 넘겨줘야 하기 때문에 entity타입으로 반환해야한다.
    AdminEntity findByAdminId(String username);


}