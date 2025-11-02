package se331.metricbackend.security.token;

// 1. เปลี่ยนเป็น MongoRepository และ ID เป็น String
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

// 2. เปลี่ยน JpaRepository เป็น MongoRepository, ID จาก Integer เป็น String
public interface TokenRepository extends MongoRepository<Token, String> {

  // 3. เปลี่ยน @Query (JPQL) เป็น Spring Data Mongo Query Method
  // เราจะหา Token ทั้งหมดที่อ้างอิงถึง User ID นี้
  // และ (ยังไม่หมดอายุ และ ยังไม่ถูกเพิกถอน)
  List<Token> findByUser_IdAndExpiredIsFalseAndRevokedIsFalse(String userId);

  Optional<Token> findByToken(String token);
}