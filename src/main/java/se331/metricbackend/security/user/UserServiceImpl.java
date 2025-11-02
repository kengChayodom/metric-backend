package se331.metricbackend.security.user;

// 1. ลบ import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserDao userDao;

    // 2. ลบ @Transactional (JTA)
    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    // 3. ลบ @Transactional (JTA)
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Page<User> getUsers(Integer pageSize, Integer page) {
        return userDao.getUsers(pageSize,page);
    }

    // 4. ลบ @Transactional (JTA) และเปลี่ยน ID
    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    // 5. ลบ @Transactional (JTA) และเปลี่ยน ID
    @Override
    public User updateRoles(String userId, List<Role> roles, Boolean enabled) {
        var u = userDao.findById(userId);
        if (u == null) throw new IllegalArgumentException("User not found");
        u.setRoles(new ArrayList<>(roles));
        if (enabled != null) u.setEnabled(enabled);
        return userDao.save(u); // การ save document เดียวเป็น Atomic อยู่แล้ว
    }
}