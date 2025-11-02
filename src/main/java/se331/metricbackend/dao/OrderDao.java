package se331.metricbackend.dao;

import se331.metricbackend.entity.UserOrder;
import java.util.List;

public interface OrderDao {
    UserOrder save(UserOrder userOrder);
    List<UserOrder> findByUserId(String userId);
}