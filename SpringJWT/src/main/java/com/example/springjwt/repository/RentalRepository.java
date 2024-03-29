package com.example.springjwt.repository;

import com.example.springjwt.entity.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
    List<RentalEntity> findByBookId(Long bookId);
    boolean existsByBookIdAndRentalState(Long bookId, Boolean rentalState);

}
