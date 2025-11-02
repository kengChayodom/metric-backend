package se331.metricbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se331.metricbackend.dao.OrderDao;

import se331.metricbackend.entity.Cart;
import se331.metricbackend.entity.UserOrder;
import se331.metricbackend.security.user.User;
import se331.metricbackend.security.user.UserDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    final OrderDao orderDao;
    final UserDao userDao;
    final CartService cartService; // เราต้องการ CartService เพื่อดึงและล้างตะกร้า

    @Override
    public UserOrder checkout() {
        // 1. ดึง User ที่ล็อกอินอยู่
        User user = getCurrentUser();

        // 2. ดึงตะกร้าปัจจุบันของ User
        Cart cart = cartService.getCartForCurrentUser();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }

        // 3. แปลง CartItem -> OrderItem (พร้อมคัดลอกข้อมูล Denormalized)
        List<UserOrder.OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> UserOrder.OrderItem.builder()
                        .game(cartItem.getGame()) // @DBRef
                        .priceAtPurchase(cartItem.getPriceAtPurchase())
                        .quantity(cartItem.getQuantity())
                        .platform(cartItem.getPlatform())
                        .title(cartItem.getTitle()) // Denormalized
                        .mainImageUrl(cartItem.getMainImageUrl()) // Denormalized
                        .build())
                .collect(Collectors.toList());

        // 4. คำนวณยอดรวม
        Double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();

        // 5. สร้าง Order ใหม่
        UserOrder newOrder = UserOrder.builder()
                .user(user)
                .items(orderItems)
                .totalAmount(totalAmount)
                .status("completed") // สถานะเริ่มต้น
                .orderDate(LocalDateTime.now())
                .build();

        // 6. บันทึก Order
        UserOrder savedOrder = orderDao.save(newOrder);

        // 7. (สำคัญมาก) ล้างตะกร้า
        cartService.clearCart();

        return savedOrder;
    }

    @Override
    public List<UserOrder> getOrderHistory() {
        User user = getCurrentUser();
        return orderDao.findByUserId(user.getId());
    }

    /**
     * (Helper) ดึง User ที่ล็อกอินอยู่
     */
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByUsername(username);
    }
}