package com.example.springjwt.service;

import com.example.springjwt.dto.BookDTO;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.BookRepository;
import com.example.springjwt.repository.RentalRepository;
import com.example.springjwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookRentalService {


    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;

    public BookRentalService(RentalRepository rentalRepository, BookRepository bookRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
    }


    @Transactional
    public boolean rentBook(Long userId, Long bookId, Boolean rentalState) {
        // 책이 이미 대여 중인지 확인
        boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(bookId, true);

        if (isAlreadyRented) {
            // 이미 대여 중인 경우
            return false;
        }

        // 책 대여 로직
        RentalEntity newRental = new RentalEntity();
        newRental.setUserId(userId);
        newRental.setBookId(bookId);
        newRental.setRentalState(true); // 대여 상태를 true로 설정
        newRental.setRentalTime(new Date()); // 현재 시간을 대여 시간으로 설정
        rentalRepository.save(newRental);

        return true;
    }

    @Transactional
    public boolean returnBook(Long bookId) {
        List<RentalEntity> rentals = rentalRepository.findByBookId(bookId);

        // 대여 중인 (rentalState가 true인) 첫 번째 기록을 찾습니다.
        RentalEntity rentalToReturn = rentals.stream()
                .filter(RentalEntity::getRentalState)
                .findFirst()
                .orElse(null);

        if (rentalToReturn == null) {
            // 해당 책이 대여 중이 아닌 경우, 반납할 수 없음
            return false;
        }

        // 대여 상태를 false로 변경하여 반납 처리
        rentalToReturn.setRentalState(false);
        rentalRepository.save(rentalToReturn);

        return true;
    }


}


    //3번째 시도, 거의성공 ㅠ
//    @Transactional
//    public boolean rentBook(Long userId, Long bookId, Boolean rentalState) {
//        // 책 상태 확인
//        Optional<RentalEntity> bookToRent = rentalRepository.findByBookId(bookId);
//
//        if (bookToRent.isPresent()) {
//            RentalEntity bookToRentOpt = bookToRent.get();
//
//            // rentalState가 null이거나 true일 경우, 책을 대여할 수 있는 로직으로 변경
//            if (rentalState == null || rentalState) {
//                // 책 대여 상태를 true(대여 중)로 업데이트
//                bookToRentOpt.setRentalState(true);
//                rentalRepository.save(bookToRentOpt); // 변경된 상태 저장
//
//                // 대여 정보 저장
//                RentalEntity newRental = new RentalEntity();
//                newRental.setUserId(userId);
//                newRental.setBookId(bookId);
//                newRental.setRentalTime(new Date()); // 현재 시간을 대여 시간으로 설정
//                rentalRepository.save(newRental);
//
//                return true;
//            }
//            // 이미 대여 중인 경우
//            return false;
//        }
//        // 책이 존재하지 않는 경우
//        return false;
//}
//    }

//    @Transactional
//    public boolean rentBook(Long userId, Long bookId) {
//        Optional<RentalEntity> existingRental = rentalRepository.findByBookIdAndRentalState(bookId,true);
//        if (existingRental.isPresent()) {
//            // 이미 대여 중인 경우
//            return false;
//        }
//
//        // 책 대여 로직
//        RentalEntity newRental = new RentalEntity();
//        newRental.setUserId(userId);
//        newRental.setBookId(bookId);
//        rentalRepository.save(newRental);
//
//        return true;
//    }


