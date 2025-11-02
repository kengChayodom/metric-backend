package se331.metricbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import se331.metricbackend.security.user.User;

import java.util.ArrayList;
import java.util.List;

// นี่คือ Document หลัก (เทียบเท่าตาราง cart)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    // อ้างอิงไปยัง User (ต้องมี Entity/Document User ด้วย)
    @DBRef
    private User user;

    // "ฝัง" (Embed) รายการ CartItem (จากตาราง cart_item)
    // โดย CartItem ไม่ต้องเป็น @Document ของตัวเอง
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();


    @Builder.Default
    private Double totalPrice = 0.0;
    /**
     * คลาสย่อยสำหรับเก็บรายการสินค้าในตะกร้า
     * นี่คือการ "Embedding" ไม่จำเป็นต้องสร้างไฟล์แยก
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        @DBRef
        private Game game; // อ้างอิงไปที่ Game Document

        private Double priceAtPurchase;
        private String platform;
        private Integer quantity;

        // เราอาจจะ "Denormalize" (คัดลอกข้อมูล) บางส่วนของ Game มาไว้ที่นี่
        // เพื่อให้แสดงผลตะกร้าได้เร็วขึ้น โดยไม่ต้อง lookup Game
        private String title;
        private String mainImageUrl;
    }
}