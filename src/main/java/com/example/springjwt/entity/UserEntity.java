package com.example.springjwt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

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

    @Temporal(TemporalType.TIMESTAMP) // Date 타입 사용 시 Temporal 어노테이션 추가
    @Column(nullable = true, updatable = true)
    private Date loginTime;

    private Long usedTimeInSeconds;

    private Long usageFee;
}

//    public long getUsedTimeInSeconds() {
//        if (this.loginTime == null) {
//            return 0;
//        }
//        // Date 타입을 사용하여 현재 시간과의 차이 계산
//        long secondsBetween = ChronoUnit.SECONDS.between(this.loginTime.toInstant(), Instant.now());
//        return secondsBetween;
//    }
//
//    public long getUsageFee(){
//        if(this.loginTime == null){
//            return 0;
//        }
//        long secondsBetween = ChronoUnit.SECONDS.between(this.loginTime.toInstant(), Instant.now());
//        long fee = (long) Math.ceil(secondsBetween/ 60.0) * 200;
//        return fee;
//    }
//
//    public void resetTimes(){
//        setLoginTime(null);
//    }
//
//}
