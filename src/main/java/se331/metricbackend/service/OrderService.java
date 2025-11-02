package se331.metricbackend.service;

import se331.metricbackend.entity.UserOrder;
import java.util.List;

public interface OrderService {

    /**
     * สร้าง Order จากตะกร้าสินค้าปัจจุบันของผู้ใช้
     */
    UserOrder checkout();

    /**
     * ดึงประวัติคำสั่งซื้อทั้งหมดของผู้ใช้ปัจจุบัน
     */
    List<UserOrder> getOrderHistory();
}