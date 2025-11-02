package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrderDTO {
    private String id;
    private UserReporter user; // ◀️ ส่ง UserReporter DTO ที่ปลอดภัย
    private List<OrderItemResponseDTO> items; // ◀️ ส่ง DTO ของ Item
    private LocalDateTime orderDate;
    private String status;
    private Double totalAmount;
}