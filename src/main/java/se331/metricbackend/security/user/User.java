package se331.metricbackend.security.user;

// --- ลบ import ที่เกี่ยวกับ JPA ทั้งหมด ---
// import jakarta.persistence.*;
// import org.hibernate.annotations.LazyCollection;
// import org.hibernate.annotations.LazyCollectionOption;

// --- เพิ่ม import ของ MongoDB ---
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// --- Import อื่นๆ ยังคงเดิม ---
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se331.metricbackend.entity.Cart;         // <-- (เพิ่ม) สมมติว่านี่คือ Cart Document
import se331.metricbackend.entity.UserOrder;  // <-- (เพิ่ม) สมมติว่านี่คือ UserOrder Document
import se331.metricbackend.security.token.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users") // <-- เปลี่ยน @Entity @Table เป็น @Document
public class User implements UserDetails {

  @Id
  private String id; // <-- เปลี่ยนจาก Integer เป็น String (สำหรับ ObjectId)

  // --- (ลบ @GeneratedValue) ---

  // --- เพิ่ม Index สำหรับฟิลด์ที่ต้องค้นหาบ่อยและควรเป็น Unique ---
  @Indexed(unique = true)
  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;

  // --- รวมฟิลด์จากสคริปต์ SQL เดิมของคุณ ---
  private String firstname;
  private String lastname;
  private String phoneNumber;
  private String address;
  private String profileImage; // จาก profile_image

  @Builder.Default
  private Boolean enabled = true;

  // --- @ElementCollection และ @Enumerated ไม่จำเป็นสำหรับ MongoDB ---
  @Builder.Default
  private List<Role> roles = new ArrayList<>(); // MongoDB เก็บ List<Enum> ได้เลย

  // --- เปลี่ยน @OneToMany เป็น @DBRef ---
  @DBRef // บอก Spring ให้เก็บเป็น Reference ID
  @Builder.Default
  private List<Token> tokens = new ArrayList<>();

  // --- (ลบ @LazyCollection) ---

  // ==========================================================
  // --- (เพิ่ม) ความสัมพันธ์ไปยัง Cart และ Order
  // ==========================================================

  @DBRef // 1 User มี 1 Cart (One-to-One)
  private Cart cart;

  @DBRef // 1 User มีหลาย Order (One-to-Many)
  @Builder.Default
  private List<UserOrder> orders = new ArrayList<>();

  // ==========================================================
  // --- ส่วนของ UserDetails ไม่ต้องแก้ไข ---
  // ==========================================================
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (roles == null) {
      return List.of();
    }
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }
}