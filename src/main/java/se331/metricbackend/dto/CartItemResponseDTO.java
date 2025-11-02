package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    // ดึงข้อมูลจาก Game Entity
    private String gameId;
    private String title;
    private String mainImageUrl;
    private String icongameUrl;
    private Double price;          // หรือ promotionPrice ก็ได้ แล้วแต่ logic
    private Double promotionPrice;

    // ข้อมูลจาก CartItem
    private String platform;
    private Integer quantity;
}