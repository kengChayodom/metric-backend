package se331.metricbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se331.metricbackend.security.user.Role; // <-- Import Role enum

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReporter {

    private String id; // <-- 1. (สำคัญมาก) เปลี่ยนเป็น String

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String profileImage;

    // 2. นี่คือส่วนที่เช็ค "Role Admin" ที่คุณบอกครับ
    // Frontend จะได้รับ List นี้ (เช่น ["ROLE_READER", "ROLE_ADMIN"])
    // และใช้ตัดสินใจว่าจะแสดงปุ่ม Admin Panel หรือไม่
    private List<Role> roles;
}