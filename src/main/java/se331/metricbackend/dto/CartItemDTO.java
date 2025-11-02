package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String gameId;     // ID ของเกมที่จะเพิ่ม
    private String platform;   // แพลตฟอร์มที่เลือก
    private Integer quantity;  // จำนวน
}