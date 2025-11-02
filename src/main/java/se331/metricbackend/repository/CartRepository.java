package se331.metricbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se331.metricbackend.entity.Cart;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

    // ค้นหา Cart ด้วย ID ของ User ที่ @DBRef ไป
    Optional<Cart> findByUser_Id(String userId);
}