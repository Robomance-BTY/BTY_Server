package com.example.springjwt.repository;

import com.example.springjwt.entity.RentalEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<RentalEntity, Long> {
    List<RentalEntity> findByBookId(Long bookId);
    boolean existsByBookIdAndRentalState(Long bookId, Boolean rentalState);

    List<RentalEntity> findByUserIdAndRentalStateTrue(Long userId);

    @Transactional
    void deleteByUserId(Long id);

    boolean existsByUserId(Long id);

    List<RentalEntity> findByUserIdAndRentalState(Long id, boolean b);



//    @Query("SELECT r.userId FROM RentalEntity r WHERE r.bookId = :bookId")
//    List<Long> findUserIdsByBookId(@Param("bookId") Long bookId);
@Query("SELECT r.userId FROM RentalEntity r WHERE r.bookId = :bookId AND r.rentalState = true")
List<Long> findUserIdsByBookIdAndRentalStateTrue(@Param("bookId") Long bookId);



}
