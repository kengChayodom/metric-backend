package se331.metricbackend.service;

import se331.metricbackend.entity.UserOrder;
import java.util.List;

public interface OrderService {

    UserOrder checkout();

    List<UserOrder> getOrderHistory();
    List<UserOrder> findAllOrders();
}