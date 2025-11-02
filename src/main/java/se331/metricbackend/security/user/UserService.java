package se331.metricbackend.security.user;

import org.springframework.data.domain.Page;
// 1. ลบ import jakarta.transaction.Transactional;
import java.util.List;

public interface UserService {
    User save(User user);

    // 2. ลบ @Transactional (JTA)
    User findByUsername(String username);

    Page<User> getUsers(Integer pageSize, Integer page);

    // 3. เปลี่ยน ID เป็น String
    User findById(String id);

    // 4. เปลี่ยน ID เป็น String
    User updateRoles(String userId, List<Role> roles, Boolean enabled);
}