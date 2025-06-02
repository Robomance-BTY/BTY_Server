package com.example.springjwt.controller;

import com.example.springjwt.dto.OrderDTO;
import com.example.springjwt.entity.OrderEntity;
import com.example.springjwt.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/application")
public class ApplicationOrderController {

    private final OrderService orderService;

    public ApplicationOrderController(OrderService orderService) {
        this.orderService = orderService;

    }

    @PostMapping("/order")
    public OrderEntity createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO.getUserId(), orderDTO.getProductId());
    }

}
