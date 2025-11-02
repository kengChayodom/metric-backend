package se331.metricbackend.security.token;

// --- ลบ Import ของ JPA ทิ้ง ---
// import jakarta.persistence.*;

// --- เพิ่ม Import ของ MongoDB ---
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// --- แก้ไข Import ของ User ให้ตรงกับ @Document User ---
import se331.metricbackend.security.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Token {

  @Id
  private String id;


  @Indexed(unique = true)
  public String token;


  @Builder.Default
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @DBRef
  public User user;
}