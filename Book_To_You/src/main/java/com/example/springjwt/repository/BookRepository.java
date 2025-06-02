package com.example.springjwt.repository;

import com.example.springjwt.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
//    Optional<BookEntity> findByIdAndRentalState(Long id, Boolean rental_state);
List<BookEntity> findByTitle(String title);
//
}

