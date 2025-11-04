package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se331.metricbackend.entity.UserOrder;
import se331.metricbackend.service.OrderService;
import se331.metricbackend.util.LapMapper; // ◀️ เพิ่ม

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> createOrderFromCart() {
        try {
            UserOrder newOrder = orderService.checkout();
            return ResponseEntity.ok(LapMapper.INSTANCE.toUserOrderDTO(newOrder));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderHistory() {
        List<UserOrder> history = orderService.getOrderHistory();
        return ResponseEntity.ok(LapMapper.INSTANCE.toUserOrderDTOs(history));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrdersForAdmin() {
        List<UserOrder> allOrders = orderService.findAllOrders();
        return ResponseEntity.ok(LapMapper.INSTANCE.toUserOrderDTOs(allOrders));
    }

}