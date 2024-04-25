package com.example.springjwt.seeder;

import com.example.springjwt.entity.ProductEntity;
import com.example.springjwt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public DatabaseSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 상품 데이터 생성
        ProductEntity product1 = new ProductEntity();
        product1.setName("상품1");
        product1.setPrice(10000L);
        product1.setCategory("카테고리A");

        ProductEntity product2 = new ProductEntity();
        product2.setName("상품2");
        product2.setPrice(20000L);
        product2.setCategory("카테고리B");

        // 상품 데이터 저장
        productRepository.save(product1);
        productRepository.save(product2);

        // 추가적으로 더 많은 상품 데이터를 생성 및 저장할 수 있습니다.
    }
}
