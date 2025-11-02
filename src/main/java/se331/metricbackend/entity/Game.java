package se331.metricbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "games")
public class Game {

    @Id
    private String id;

    private String title;
    private Double price; // ใช้ Double แทน DECIMAL
    private Double promotionPrice;
    private String description;
    private String mainImageUrl;
    private String icongameUrl;
    private String trailerUrl;

    // 1. จากตาราง game_images (OneToMany)
    // เราจะ "ฝัง" (Embed) List ของ URL รูปภาพไว้ใน Document นี้เลย
    private List<String> images;

    // 2. จากตาราง game_category (ManyToMany)
    // เราจะ "อ้างอิง" (Reference) ไปยัง Category Documents
    // @DBRef จะบอก Spring ให้เก็บแค่ ID แต่ตอนดึงข้อมูลให้ไป Join มาให้
    @DBRef
    private List<Category> categories;
}