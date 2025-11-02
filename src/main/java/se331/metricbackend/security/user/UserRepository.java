package se331.metricbackend.security.user;

// 1. เปลี่ยนเป็น MongoRepository
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

// 2. เปลี่ยน JpaRepository เป็น MongoRepository, ID จาก Integer เป็น String
public interface UserRepository extends MongoRepository<User, String> {

  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

}