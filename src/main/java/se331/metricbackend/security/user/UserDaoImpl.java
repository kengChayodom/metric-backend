package se331.metricbackend.security.user;

// ... imports ...

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    final UserRepository userRepository;

    // ... (findByUsername, save, getUsers ไม่ต้องแก้) ...
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override public Page<User> getUsers(Integer pageSize, Integer page) {
        return userRepository.findAll(PageRequest.of(page - 1 , pageSize));
    }

    // 1. เปลี่ยนจาก Integer เป็น String
    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }
}