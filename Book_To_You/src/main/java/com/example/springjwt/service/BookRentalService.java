package com.example.springjwt.service;

import com.example.springjwt.dto.BookDTO;
import com.example.springjwt.dto.RentalAndReservationInfoDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

@Slf4j
@Service
public class BookRentalService {


    private final RentalRepository rentalRepository;
    private final BookRepository bookRepository;
    private final MessageService messageService;

    private final ReservationRepository reservationRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public BookRentalService(RentalRepository rentalRepository, BookRepository bookRepository, MessageService messageService, ReservationRepository reservationRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        this.messageService = messageService;
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
            log.info("successfully reserved userId = {},bookId = {}",rentalDTO.getUserId(),rentalDTO.getBookId());
            return new ResponseDTO("예약됨", bookEntity.getLocationInfo(), "예약된 책이 반납되어 배송됩니다.",null);
        } else {
            // 대여 가능한 경우, 대여 처리
            RentalEntity rentalEntity = new RentalEntity();
            rentalEntity.setUserId(rentalDTO.getUserId());
            rentalEntity.setBookId(rentalDTO.getBookId());
            rentalEntity.setRentalTime(new Date());
            rentalEntity.setRentalState(true);
            rentalRepository.save(rentalEntity);
            log.info("successfully rent userId = {},bookId = {}",rentalDTO.getUserId(),rentalDTO.getBookId());
            return new ResponseDTO("대여됨", bookEntity.getLocationInfo(), null,null);
        }
    }



@Transactional
public ResponseDTO returnBook(Long bookId, Long userId) {
    List<RentalEntity> rentals = rentalRepository.findByBookId(bookId);

    RentalEntity rentalToReturn = rentals.stream()
            .filter(RentalEntity::getRentalState)
            .findFirst()
            .orElse(null);

    if (rentalToReturn == null) {
        return new ResponseDTO("실패", null, "존재하지 않거나 이미 반납된 책입니다.", null);
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


        messageService.reservationReturn(userId,newRental.getUserId(),bookId,messageService.getAssignedRegion());


        return new ResponseDTO("성공", bookEntity.getLocationInfo(), "책이 예약된 사용자에게 성공적으로 대여되었습니다.", firstReservation.getUserId());
    } else {
        BookEntity bookEntity = bookRepository.findById(bookId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));

        messageService.assignRegion();
        messageService.bookReturn(userId,bookId,messageService.getAssignedRegion());

        return new ResponseDTO("성공", bookEntity.getLocationInfo(), "책이 성공적으로 반납되었습니다.", userId);
    }
}


    public Boolean testMethod(RentalDTO rentalDTO){
        return rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
    }

        public RentalAndReservationInfoDto getRentalStatusAndFirstReservation(Long bookId) {
            boolean isRented = rentalRepository.existsByBookIdAndRentalState(bookId, true);
            ReservationEntity firstReservation = reservationRepository.findByBookIdOrderByReservationTimeAsc(bookId)
                    .stream()
                    .findFirst()
                    .orElse(null);

            return new RentalAndReservationInfoDto(isRented, firstReservation);
        }

    @Transactional
    public List<ReservationEntity> cancelAllReservationsByUserId(Long userId) {
        List<ReservationEntity> reservations = reservationRepository.findByUserId(userId);
        reservationRepository.deleteByUserId(userId);
        return reservations; // 삭제 전 조회한 예약 정보를 반환
    }

    public List<RentalEntity> getUnreturnedRentalsByUserId(Long userId){
        return rentalRepository.findByUserIdAndRentalStateTrue(userId);
    }



public List<Long> getUserIdsByBookId(Long bookId) {
    return rentalRepository.findUserIdsByBookIdAndRentalStateTrue(bookId);
}

}


