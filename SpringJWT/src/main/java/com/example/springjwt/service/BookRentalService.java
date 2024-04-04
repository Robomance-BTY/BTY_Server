package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.dto.ResponseDTO;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.ReservationEntity;
import com.example.springjwt.repository.BookRepository;
import com.example.springjwt.repository.RentalRepository;
import com.example.springjwt.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

@Service
public class BookRentalService {


    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;

    private final ReservationRepository reservationRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public BookRentalService(RentalRepository rentalRepository, BookRepository bookRepository, ReservationRepository reservationRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.reservationRepository = reservationRepository;

        modelMapper.getConfiguration().setAmbiguityIgnored(true); // 모호성 무시 설정
        modelMapper.typeMap(RentalDTO.class, RentalEntity.class).addMappings(mapper -> {
                    mapper.map(RentalDTO::getBookId, RentalEntity::setBookId);
                    mapper.map(RentalDTO::getUserId, RentalEntity::setUserId);
                    // setId 메소드에 대한 매핑 무시
                    mapper.skip(RentalEntity::setId);
        });
    }

    @Transactional
    public ResponseDTO rentOrReserveBook(RentalDTO rentalDTO) {
        boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
        BookEntity bookEntity = bookRepository.findById(rentalDTO.getBookId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        if (isAlreadyRented) {
            // 이미 대여 중인 경우, 예약 처리
            ReservationEntity reservationEntity = new ReservationEntity();
            reservationEntity.setUserId(rentalDTO.getUserId());
            reservationEntity.setBookId(rentalDTO.getBookId());
            reservationEntity.setReservationTime(new Date());
            reservationRepository.save(reservationEntity);
            return new ResponseDTO("예약됨", bookEntity.getLocationInfo(), "예약된 책이 반납되어 배송됩니다.",null);
        } else {
            // 대여 가능한 경우, 대여 처리
            RentalEntity rentalEntity = new RentalEntity();
            rentalEntity.setUserId(rentalDTO.getUserId());
            rentalEntity.setBookId(rentalDTO.getBookId());
            rentalEntity.setRentalTime(new Date());
            rentalEntity.setRentalState(true);
            rentalRepository.save(rentalEntity);
            return new ResponseDTO("대여됨", bookEntity.getLocationInfo(), null,null);
        }
    }


//@Transactional
//public BookEntity rentBook(RentalDTO rentalDTO) {
//    // 책의 존재 여부 확인
//    Optional<BookEntity> bookOptional = bookRepository.findById(rentalDTO.getBookId());
//    if (!bookOptional.isPresent()) {
//        throw new EntityNotFoundException("Book does not exist.");
//    }
//
//    // 이미 대여 중인지 확인
//    boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
//    if (isAlreadyRented) {
//        throw new IllegalStateException("Book is already rented.");
//    }
//
//    RentalEntity rentalEntity = modelMapper.map(rentalDTO, RentalEntity.class);
//    rentalEntity.setRentalState(true);
//    rentalEntity.setRentalTime(new Date());
//    rentalRepository.save(rentalEntity);
//
//    // 대여 성공 시, 대여한 책의 정보 반환
//    return bookOptional.get();
//}


    @Transactional
    public ResponseDTO returnBook(Long bookId) {
        List<RentalEntity> rentals = rentalRepository.findByBookId(bookId);

        RentalEntity rentalToReturn = rentals.stream()
                .filter(RentalEntity::getRentalState)
                .findFirst()
                .orElse(null);

        if (rentalToReturn == null) {
            return new ResponseDTO("실패", null, "존재하지 않거나 이미 반납된 책입니다.",null);
        }

        rentalToReturn.setRentalState(false);
        rentalToReturn.setReturnTime(new Date());
        rentalRepository.save(rentalToReturn);

        // 책 반환 후 예약이 있는지 확인
        List<ReservationEntity> reservations = reservationRepository.findByBookIdOrderByReservationTimeAsc(bookId);
        if (!reservations.isEmpty()) {
            // 예약이 있다면, 가장 먼저 예약한 사용자에게 책을 대여하고 예약을 삭제
            ReservationEntity firstReservation = reservations.get(0);
            RentalEntity newRental = new RentalEntity();
            newRental.setUserId(firstReservation.getUserId());
            newRental.setBookId(bookId);
            newRental.setRentalState(true);
            newRental.setRentalTime(new Date());
            rentalRepository.save(newRental);

            reservationRepository.delete(firstReservation);

            BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
            return new ResponseDTO("성공", bookEntity.getLocationInfo(), "책이 예약된 사용자에게 성공적으로 대여되었습니다.",bookEntity.getId());
        } else {
            BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
            return new ResponseDTO("성공", bookEntity.getLocationInfo(), "책이 성공적으로 반납되었습니다.",bookEntity.getId());
        }
    }
//    @Transactional
//    public boolean returnBook(Long bookId) {
//        List<RentalEntity> rentals = rentalRepository.findByBookId(bookId);
//
//        // 대여 중인 (rentalState가 true인) 첫 번째 기록을 찾습니다.
//        RentalEntity rentalToReturn = rentals.stream()
//                .filter(RentalEntity::getRentalState)
//                .findFirst()
//                .orElse(null);
//
//        if (rentalToReturn == null) {
//            // 해당 책이 대여 중이 아닌 경우, 반납할 수 없음
//            return false;
//        }
//
//        // 대여 상태를 false로 변경하여 반납 처리
//        rentalToReturn.setRentalState(false);
//        rentalToReturn.setReturnTime(new Date());
//        rentalRepository.save(rentalToReturn);
//
//        return true;
//    }

    public List<RentalEntity> getUnreturnedRentalsByUserId(Long userId){
        return rentalRepository.findByUserIdAndRentalStateTrue(userId);
    }

}
