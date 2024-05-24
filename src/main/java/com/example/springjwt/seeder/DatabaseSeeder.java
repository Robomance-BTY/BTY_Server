package com.example.springjwt.seeder;

import com.example.springjwt.entity.BookEntity;
import com.example.springjwt.entity.ProductEntity;
import com.example.springjwt.repository.BookRepository;
import com.example.springjwt.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final BookRepository bookRepository;

    public DatabaseSeeder(ProductRepository productRepository, BookRepository bookRepository) {
        this.productRepository = productRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Call the database initialization method
        seedProductData();
    }

    // Method to create and save product data
    public void seedProductData() {
        createProductIfNotExists("상품1", 10000L, "카테고리A");
        createProductIfNotExists("상품2", 20000L, "카테고리B");
        createBookIfNotExists("나 혼자만 레벨업 1화", "장성락 저자(글) · 추공 원작", "sfFantasy,action", "2019년 09월 26일","전 세계 독자가 열광한 레이드 액션의 진수!\\n미국, 독일, 프랑스, 브라질, 스페인, 아르헨티나, 덴마크, 스웨덴, 우크라이나,\\n포르투갈, 이탈리아, 일본, 태국, 중국을 비롯, 세계 각국에 단행본 수출 중!!\\n최약체에서 최강 헌터로, 세상의 중심에 서다! 카카오페이지에서 연일 화제를 모으며, 폭발적 인기 속에 연재를 마친 장성락(REDICE STUDIO) 만화, 추공 원작, 현군 각색 『만화 나 혼자만 레벨업』 단행본 10권이 출간된다. 『만화 나 혼자만 레벨업』은 글로벌 플랫폼 웹툰 연재로 세계 각국에 팬덤이 형성될 만큼 완성도와 재미, 콘텐츠로서의 저력을 입증한 작품이다. 또한 압도적인 인기에 힘입어 각국에 단행본 수출 릴레이를 이어가는 중이며, 2024년은 넷플릭스 등 다수의 OTT 채널을 통해 TV애니메이션이 전 세계 동시 방영되었고, 넷마블에서 게임이 론칭되는 등 글로벌 단위의 인기를 실감하게 하는 한 해가 됐다.\\n단행본은 본문 전면 재연출을 거쳐 넉넉한 판형으로 출간되어, 모바일 스크롤 웹툰과는 또 다른 ‘나혼렙’ 액션신의 쾌감을 100% 만끽할 수 있다.\\n한 화 읽고 나면 정신을 차릴 수 없이 빠져드는 레이드 액션의 진수! 돈 없고, 빽 없고, 능력 없는 사상 최약체 E급 헌터, 성진우의 화려한 부활, 그리고 레벨업이 시작된다!", "");

        // You can add more product data as needed
    }

    private void createProductIfNotExists(String name, Long price, String category) {
        List<ProductEntity> existingProducts = productRepository.findByName(name);
        if (existingProducts.isEmpty()) {
            ProductEntity product = new ProductEntity();
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    private void createBookIfNotExists(String title, String author, String genre, String publishedDate, String description , String locationInfo) {
        List<BookEntity> existingBooks = bookRepository.findByTitle(title);
        if (existingBooks.isEmpty()) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setTitle(title);
            bookEntity.setAuthor(author);
            bookEntity.setGenre(genre);
            bookEntity.setLocationInfo(locationInfo);
            bookEntity.setCountry("KR");
            bookEntity.setDescription(description);
            bookEntity.setPublishedDate(publishedDate);
            bookRepository.save(bookEntity);
        }
    }
}
