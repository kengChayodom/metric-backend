package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private GameDTO game; // ◀️ ส่ง GameDTO ที่ปลอดภัย
    private Double priceAtPurchase;
    private String platform;
    private Integer quantity;
    private String title;
    private String mainImageUrl;
}