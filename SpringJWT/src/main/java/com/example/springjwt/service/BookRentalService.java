package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.repository.BookRepository;
import com.example.springjwt.repository.RentalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
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


    @Transactional
    public boolean rentBook(RentalDTO rentalDTO) {
        // DTO를 Entity로 변환
        RentalEntity rentalEntity = modelMapper.map(rentalDTO, RentalEntity.class);
        rentalEntity.setRentalState(true); // 대여 상태를 true로 설정
        rentalEntity.setRentalTime(new Date()); // 현재 시간을 대여 시간으로 설정

        // 책이 이미 대여 중인지 확인
        boolean isAlreadyRented = rentalRepository.existsByBookIdAndRentalState(rentalDTO.getBookId(), true);
        if (isAlreadyRented) {
            return false;
        }

        rentalRepository.save(rentalEntity);
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
        rentalToReturn.setReturnTime(new Date());
        rentalRepository.save(rentalToReturn);

        return true;
    }

    public List<RentalEntity> getUnreturnedRentalsByUserId(Long userId){
        return rentalRepository.findByUserIdAndRentalStateTrue(userId);
    }

}
