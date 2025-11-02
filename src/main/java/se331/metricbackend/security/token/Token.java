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
@Document(collection = "tokens") // <-- 1. เปลี่ยนเป็น @Document
public class Token {

  @Id // <-- 2. เปลี่ยนเป็น @Id ของ Spring Data
  private String id; // <-- 3. เปลี่ยนเป็น String (สำหรับ ObjectId)

  // (ลบ @GeneratedValue)

  @Indexed(unique = true) // <-- 4. เปลี่ยน @Column(unique=true) เป็น @Indexed
  public String token;

  // (ลบ @Enumerated) MongoDB เก็บ Enum เป็น String ให้โดยอัตโนมัติ
  @Builder.Default
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @DBRef // <-- 5. เปลี่ยน @ManyToOne เป็น @DBRef
  // (ลบ @JoinColumn)
  public User user;
}