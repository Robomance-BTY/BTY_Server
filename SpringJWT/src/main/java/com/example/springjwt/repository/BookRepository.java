package com.example.springjwt.repository;

import com.example.springjwt.entity.BookEntity;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
//    Optional<BookEntity> findByIdAndRentalState(Long id, Boolean rental_state);
//
}

