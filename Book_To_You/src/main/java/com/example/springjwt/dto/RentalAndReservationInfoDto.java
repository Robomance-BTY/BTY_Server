package com.example.springjwt.dto;

import com.example.springjwt.entity.ReservationEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalAndReservationInfoDto {

    private boolean isRented;
    private ReservationEntity firstReservation;

    public RentalAndReservationInfoDto(boolean isRented, ReservationEntity firstReservation) {
        this.isRented = isRented;
        this.firstReservation = firstReservation;
    }

}
