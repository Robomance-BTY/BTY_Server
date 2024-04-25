package com.example.springjwt.service;

import com.example.springjwt.dto.UserDTO;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserTimeFeeService {

    private final UserRepository userRepository;

    public UserTimeFeeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public long getUsedTimeInSeconds(UserDTO userDTO) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userDTO.getId());
        if (!userEntityOptional.isPresent() || userEntityOptional.get().getLoginTime() == null) {
            return 0;
        }
        UserEntity user = userEntityOptional.get();
        return ChronoUnit.SECONDS.between(user.getLoginTime().toInstant(), Instant.now());
    }

    public List<UserDTO> getAllLoggedInUsersInfo() {
        List<UserEntity> loggedInUsers = userRepository.findByLoginStateTrue();
        return loggedInUsers.stream().map(user -> {
            UserDTO userDTO = UserDTO.toDTO(user);
            long usedTimeInSeconds = getUsedTimeInSeconds(userDTO);
            long usageFee = (long) Math.ceil(usedTimeInSeconds / 60.0) * 200;
            userDTO.setUsedTimeInSeconds(usedTimeInSeconds);
            userDTO.setUsageFee(usageFee);
            return userDTO;
        }).collect(Collectors.toList());
    }

    public long getUsageFee(UserDTO userDTO) {
        long usedTimeInSeconds = getUsedTimeInSeconds(userDTO);
        return (long) Math.ceil(usedTimeInSeconds / 60.0) * 200;
    }


    public void resetLoginTime(UserEntity user) {
        user.setLoginTime(null);
    }
}

