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
public class CartResponseDTO {
    private String id;
    private List<CartItemResponseDTO> items;
    private Double totalPrice; // สมมติว่า Cart Entity มี field นี้
    // สังเกต: เราไม่มี field User user; ที่นี่!
}