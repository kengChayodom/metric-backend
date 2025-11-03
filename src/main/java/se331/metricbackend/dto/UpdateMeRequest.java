// se331.metricbackend.dto.UpdateMeRequest.java
package se331.metricbackend.dto;

import lombok.Data;

@Data
public class UpdateMeRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String profileImage;

    // สำหรับเปลี่ยนรหัสผ่าน (optional)
    private String currentPassword;
    private String newPassword;
}
