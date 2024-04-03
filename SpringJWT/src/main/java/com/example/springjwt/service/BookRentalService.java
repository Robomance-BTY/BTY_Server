package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.repository.BookRepository;
import com.example.springjwt.repository.RentalRepository;
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
    private ModelMapper modelMapper = new ModelMapper();

    public BookRentalService(RentalRepository rentalRepository, BookRepository bookRepository) {
        this.rentalRepository = rentalRepository;
        this.bookRepository = bookRepository;
        modelMapper.getConfiguration().setAmbiguityIgnored(true); // 모호성 무시 설정
        modelMapper.typeMap(RentalDTO.class, RentalEntity.class).addMappings(mapper -> {
                    mapper.map(RentalDTO::getBookId, RentalEntity::setBookId);
                    mapper.map(RentalDTO::getUserId, RentalEntity::setUserId);
                    // setId 메소드에 대한 매핑 무시
                    mapper.skip(RentalEntity::setId);
        });
    }


//    @Transactional
//    public BookEntity rentBook(RentalDTO rentalDTO) {
//        boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
//        if (isAlreadyRented) {
//            return null; // 이미 대여 중인 경우
//        }
//
//        RentalEntity rentalEntity = modelMapper.map(rentalDTO, RentalEntity.class);
//        rentalEntity.setRentalState(true);
//        rentalEntity.setRentalTime(new Date());
//        rentalRepository.save(rentalEntity);
//
//        // 대여 성공 시, 대여한 책의 정보 반환
//        return bookRepository.findById(rentalDTO.getBookId()).orElse(null);
//    }
//
@Transactional
public BookEntity rentBook(RentalDTO rentalDTO) {
    // 책의 존재 여부 확인
    Optional<BookEntity> bookOptional = bookRepository.findById(rentalDTO.getBookId());
    if (!bookOptional.isPresent()) {
        throw new EntityNotFoundException("Book does not exist.");
    }

    // 이미 대여 중인지 확인
    boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
    if (isAlreadyRented) {
        throw new IllegalStateException("Book is already rented.");
    }

    RentalEntity rentalEntity = modelMapper.map(rentalDTO, RentalEntity.class);
    rentalEntity.setRentalState(true);
    rentalEntity.setRentalTime(new Date());
    rentalRepository.save(rentalEntity);

    // 대여 성공 시, 대여한 책의 정보 반환
    return bookOptional.get();
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
        rentalToReturn.setReturnTime(new Date());
        rentalRepository.save(rentalToReturn);

        return true;
    }

    public List<RentalEntity> getUnreturnedRentalsByUserId(Long userId){
        return rentalRepository.findByUserIdAndRentalStateTrue(userId);
    }

}
