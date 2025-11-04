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
        User user = getCurrentUser();

        Cart cart = cartService.getCartForCurrentUser();
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot checkout.");
        }

        List<UserOrder.OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> UserOrder.OrderItem.builder()
                        .game(cartItem.getGame())
                        .priceAtPurchase(cartItem.getPriceAtPurchase())
                        .quantity(cartItem.getQuantity())
                        .platform(cartItem.getPlatform())
                        .title(cartItem.getTitle())
                        .mainImageUrl(cartItem.getMainImageUrl())
                        .build())
                .collect(Collectors.toList());

        Double totalAmount = orderItems.stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();

        UserOrder newOrder = UserOrder.builder()
                .user(user)
                .items(orderItems)
                .totalAmount(totalAmount)
                .status("completed")
                .orderDate(LocalDateTime.now())
                .build();

        UserOrder savedOrder = orderDao.save(newOrder);

        cartService.clearCart();

        return savedOrder;
    }

    @Override
    public List<UserOrder> getOrderHistory() {
        User user = getCurrentUser();
        return orderDao.findByUserId(user.getId());
    }


    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByUsername(username);
    }

    @Override
    public List<UserOrder> findAllOrders() {
        List<UserOrder> orders = orderDao.findAll();
        orders.sort((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()));
        return orders;
    }
}