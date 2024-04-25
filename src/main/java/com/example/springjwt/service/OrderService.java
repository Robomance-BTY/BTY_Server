package com.example.springjwt.service;

import com.example.springjwt.entity.OrderEntity;
import com.example.springjwt.entity.ProductEntity;
import com.example.springjwt.repository.OrderRepository;
import com.example.springjwt.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderEntity createOrder(Long userId, Long productId) {
        // 상품 존재 여부 확인
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 주문 엔티티 생성 및 저장
        OrderEntity order = new OrderEntity();
        order.setUserId(userId);
        order.setProductId(productId);
        order.setOrderTime(new Date());
        order.setOrderState(true);

        return orderRepository.save(order);
    }
}
