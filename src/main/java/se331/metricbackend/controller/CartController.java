package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se331.metricbackend.dto.CartItemDTO;
// 🔽🔽🔽 1. แก้ไข Import 🔽🔽🔽
import se331.metricbackend.dto.CartDTO;
import se331.metricbackend.entity.Cart;
import se331.metricbackend.service.CartService;
import se331.metricbackend.util.LapMapper;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    final CartService cartService;
    // ◀️ ไม่ต้องฉีด LapMapper เพราะเราจะใช้ INSTANCE

    /**
     * ดึงตะกร้าปัจจุบันของผู้ใช้
     */
    @GetMapping("/me")
    public ResponseEntity<?> getMyCart() {
        Cart cart = cartService.getCartForCurrentUser();
        // 🔽🔽🔽 2. แก้ไขชื่อ DTO และชื่อเมธอด 🔽🔽🔽
        CartDTO cartDTO = LapMapper.INSTANCE.toCartDTO(cart);
        return ResponseEntity.ok(cartDTO);
    }

    /**
     * เพิ่มสินค้า (หรืออัปเดตจำนวนถ้ามีอยู่แล้ว)
     */
    @PostMapping("/item")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDTO itemDTO) {
        Cart updatedCart = cartService.addItemToCart(itemDTO);
        // 🔽🔽🔽 3. แก้ไขชื่อ DTO และชื่อเมธอด 🔽🔽🔽
        CartDTO cartDTO = LapMapper.INSTANCE.toCartDTO(updatedCart);
        return ResponseEntity.ok(cartDTO);
    }

    /**
     * ลบสินค้า 1 รายการ (ตาม gameId และ platform)
     */
    @DeleteMapping("/item")
    public ResponseEntity<?> removeItemFromCart(
            @RequestParam("gameId") String gameId,
            @RequestParam("platform") String platform
    ) {
        Cart updatedCart = cartService.removeItemFromCart(gameId, platform);
        // 🔽🔽🔽 4. แก้ไขชื่อ DTO และชื่อเมธอด 🔽🔽🔽
        CartDTO cartDTO = LapMapper.INSTANCE.toCartDTO(updatedCart);
        return ResponseEntity.ok(cartDTO);
    }

    /**
     * ล้างตะกร้า
     */
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        Cart clearedCart = cartService.clearCart();
        // 🔽🔽🔽 5. แก้ไขชื่อ DTO และชื่อเมธอด 🔽🔽🔽
        CartDTO cartDTO = LapMapper.INSTANCE.toCartDTO(clearedCart);
        return ResponseEntity.ok(cartDTO);
    }
}