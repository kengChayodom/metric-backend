package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    // ไม่มี ID เพราะใช้สำหรับสร้าง
    private String id;
    private String title;
    private Double price;
    private Double promotionPrice;
    private String description;
    private String mainImageUrl;
    private String icongameUrl;
    private String trailerUrl;

    // รับ List<String> ของ URL รูปภาพ
    private List<String> images;

    // รับ List<String> ของ Category ID
    private List<String> categoryIds;
}