package se331.metricbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import se331.metricbackend.security.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class UserOrder {

    @Id
    private String id;

    // อ้างอิงไปยัง User (ต้องมี Entity/Document User ด้วย)
    @DBRef
    private User user;

    // "ฝัง" (Embed) รายการ OrderItem (จากตาราง order_item)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Builder.Default
    private LocalDateTime orderDate = LocalDateTime.now();

    private String status; // "pending", "completed", "cancelled"
    private Double totalAmount;

    /**
     * คลาสย่อยสำหรับเก็บรายการสินค้าใน Order
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        @DBRef
        private Game game; // อ้างอิงไปที่ Game Document

        private Double priceAtPurchase;
        private String platform;
        private Integer quantity;

        // Denormalized data
        private String title;
        private String mainImageUrl;
    }
}