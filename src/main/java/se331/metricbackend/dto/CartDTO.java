package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private String id;
    private UserReporter user; // ◀️ ส่ง UserReporter DTO ที่ปลอดภัย
    private List<CartItemResponseDTO> items; // ◀️ ส่ง DTO ของ Item
    private Double totalPrice;
}