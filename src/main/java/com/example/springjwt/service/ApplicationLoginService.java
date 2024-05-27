package com.example.springjwt.service;

import com.example.springjwt.dto.RentalDTO;
import com.example.springjwt.dto.ResponseDTO;
import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.RentalRepository;
import com.example.springjwt.repository.ReservationRepository;
import com.example.springjwt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ApplicationLoginService {

    private final BookRentalService bookRentalService;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final UserTimeFeeService userTimeFeeService;
    private final ReservationRepository reservationRepository;

    public ApplicationLoginService(BookRentalService bookRentalService, UserRepository userRepository, RentalRepository rentalRepository, UserTimeFeeService userTimeFeeService, ReservationRepository reservationRepository) {
        this.bookRentalService = bookRentalService;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.userTimeFeeService = userTimeFeeService;
        this.reservationRepository = reservationRepository;
    }


    public UserDTO login(UserDTO userDTO) {
        Optional<UserEntity> byLoginCode = userRepository.findByLoginCode(userDTO.getLoginCode());
        if (byLoginCode.isPresent()) {
            // 조회결과가 있다.
            UserEntity userEntity = byLoginCode.get();
            if (userEntity.getLoginCode().equals(userDTO.getLoginCode())) {
                // 비밀번호 일치
                // 로그인 시간과 상태 업데이트
                userEntity.setLoginTime(new Date()); // 엔티티 내 로그인 시간 설정
                userEntity.setLoginState(Boolean.TRUE); // 엔티티 내 로그인 상태 설정

                // 엔티티의 변경사항을 데이터베이스에 저장
                userRepository.save(userEntity);

                // 엔티티 -> DTO 변환 후 리턴
                UserDTO userdto = UserDTO.toDTO(userEntity);
                return userdto;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

//    @Transactional
//    public UserDTO logout(UserDTO userDTO) {
//        Optional<UserEntity> byId = userRepository.findById(userDTO.getId());
//        if (byId.isPresent()) {
//            UserEntity userEntity = byId.get();
//
//            // 사용자가 빌린 모든 책의 ID를 찾습니다.
//            List<RentalEntity> rentals = rentalRepository.findByUserIdAndRentalState(userDTO.getId(), true);
//            for (RentalEntity rental : rentals) {
//                // 각 책에 대해 반납 로직을 실행합니다.
//                ResponseDTO returnResponse = bookRentalService.returnBook(rental.getBookId());
//                if (!returnResponse.getStatus().equals("성공")) {
//                    // 반납 과정에서 실패한 경우, 로그 기록 또는 추가 처리를 할 수 있습니다.
//                    log.warn("Failed to return book during logout. Book ID: {}", rental.getBookId());
//                }
//            }
//
//
//
//            // 해당 사용자의 예약 정보가 있는지 확인 후 삭제
//            if (reservationRepository.existsByUserId(userDTO.getId())) {
//                reservationRepository.deleteByUserId(userDTO.getId());
//            }
//
//            // 해당 사용자의 대여 정보가 있는지 확인 후 삭제
//             if (rentalRepository.existsByUserId(userDTO.getId())) {
//                 rentalRepository.deleteByUserId(userDTO.getId());
//             }
//
//            userEntity.setLoginState(Boolean.FALSE);
//
//
//            long usedTime = getUsedTimeInSeconds(userDTO.getId());
//            if (usedTime > 0) {
//                // 사용 시간이 0보다 크면 로그로 기록하거나 다른 처리를 할 수 있습니다.
//                log.info("User ID: {}, Used Time: {} seconds", userDTO.getId(), usedTime);
//            } else {
//                // 사용 시간을 가져오는 데 실패했을 경우의 처리
//                log.info("Failed to get used time for User ID: {}", userDTO.getId());
//            }
//
////            userEntity.setLoginTime(null);
//            userRepository.save(userEntity);
//
//            return UserDTO.toDTO(userEntity);
//        } else {
//            // 해당 ID의 사용자가 없을 경우
//            return null;
//        }
//    }


    @Transactional
    public UserDTO logout(UserDTO userDTO) {
        Optional<UserEntity> byId = userRepository.findById(userDTO.getId());
        if (byId.isPresent()) {
            UserEntity userEntity = byId.get();

            // 사용자가 빌린 모든 책의 ID를 찾습니다.
            List<RentalEntity> rentals = rentalRepository.findByUserIdAndRentalState(userDTO.getId(), true);
            for (RentalEntity rental : rentals) {
                // 각 책에 대해 반납 로직을 실행합니다.
                ResponseDTO returnResponse = bookRentalService.returnBook(rental.getBookId(),rental.getUserId());
                if (!returnResponse.getStatus().equals("성공")) {
                    // 반납 과정에서 실패한 경우, 로그 기록 또는 추가 처리를 할 수 있습니다.
                    log.warn("Failed to return book during logout. Book ID: {}", rental.getBookId());
                }
            }

            // 해당 사용자의 예약 정보가 있는지 확인 후 삭제
            if (reservationRepository.existsByUserId(userDTO.getId())) {
                reservationRepository.deleteByUserId(userDTO.getId());
            }

            // 해당 사용자의 대여 정보가 있는지 확인 후 삭제
            if (rentalRepository.existsByUserId(userDTO.getId())) {
                rentalRepository.deleteByUserId(userDTO.getId());
            }

            userEntity.setLoginState(Boolean.FALSE);

            long usedTime = userTimeFeeService.getUsedTimeInSeconds(UserDTO.toDTO(userEntity));
            if (usedTime > 0) {
                // 사용 시간이 0보다 크면 로그로 기록하거나 다른 처리를 할 수 있습니다.
                log.info("User ID: {}, Used Time: {} seconds", userDTO.getId(), usedTime);
            } else {
                // 사용 시간을 가져오는 데 실패했을 경우의 처리
                log.info("Failed to get used time for User ID: {}", userDTO.getId());
            }

            userRepository.save(userEntity);

            return UserDTO.toDTO(userEntity);
        } else {
            // 해당 ID의 사용자가 없을 경우
            return null;
        }
    }
}



//    public long getUsedTimeInSeconds(Long id) {
//            Optional<UserEntity> user = userRepository.findById(id);
//            return user.map(UserEntity::getUsedTimeInSeconds).orElse(0L);
//        }





