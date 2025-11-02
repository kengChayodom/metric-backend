package se331.metricbackend.security.user;

import org.springframework.data.domain.Page;

public interface UserDao {
    User findByUsername(String username);
    User save(User user);
    Page<User> getUsers(Integer pageSize, Integer page);

    // เปลี่ยนจาก Integer เป็น String
    User findById(String id);
}