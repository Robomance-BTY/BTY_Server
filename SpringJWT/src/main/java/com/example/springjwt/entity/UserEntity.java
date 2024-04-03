package com.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String loginCode;

    @Column
    private Boolean loginState;


    @Column(nullable = true, updatable = true)
    private LocalDate loginTime;


    public long getUsedTimeInSeconds() {
        if (this.loginTime == null) {
            return 0;
        }
        return Duration.between(this.loginTime, LocalDateTime.now()).getSeconds();
    }


}
