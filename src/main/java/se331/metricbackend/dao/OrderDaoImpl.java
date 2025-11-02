package se331.metricbackend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se331.metricbackend.entity.UserOrder;
import se331.metricbackend.repository.UserOrderRepository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {

    final UserOrderRepository userOrderRepository;

    @Override
    public UserOrder save(UserOrder userOrder) {
        return userOrderRepository.save(userOrder);
    }

    @Override
    public List<UserOrder> findByUserId(String userId) {
        // ใช้เมธอดที่เราสร้างไว้ใน Repository
        return userOrderRepository.findByUser_IdOrderByOrderDateDesc(userId);
    }
}