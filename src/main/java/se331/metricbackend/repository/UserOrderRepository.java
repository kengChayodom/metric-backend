package se331.metricbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se331.metricbackend.entity.UserOrder;
import java.util.List;

public interface UserOrderRepository extends MongoRepository<UserOrder, String> {

    /**
     * ค้นหา Order ทั้งหมดของ User คนเดียว
     * โดยเรียงจากวันที่สั่งซื้อล่าสุด (Desc)
     */
    List<UserOrder> findByUser_IdOrderByOrderDateDesc(String userId);
}