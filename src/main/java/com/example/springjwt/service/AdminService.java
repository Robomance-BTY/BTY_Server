package com.example.springjwt.service;

import com.example.springjwt.entity.OrderEntity;
import com.example.springjwt.entity.RentalEntity;
import com.example.springjwt.entity.ReservationEntity;
import com.example.springjwt.entity.UserEntity;
import com.example.springjwt.repository.OrderRepository;
import com.example.springjwt.repository.RentalRepository;
import com.example.springjwt.repository.ReservationRepository;
import com.example.springjwt.repository.UserRepository;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminService {
private final UserRepository userRepository;
private final RentalRepository rentalRepository;
private final ReservationRepository reservationRepository;

private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository, RentalRepository rentalRepository, ReservationRepository reservationRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.reservationRepository = reservationRepository;
        this.orderRepository = orderRepository;
    }

public List<UserEntity> getAlUserInfo() {

        return userRepository.findAll();
}

    public List<RentalEntity> getAllRentals() {
        return rentalRepository.findAll();
    }
    public List<ReservationEntity> getAllReservations(){
        return reservationRepository.findAll();
    }

    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }
}
