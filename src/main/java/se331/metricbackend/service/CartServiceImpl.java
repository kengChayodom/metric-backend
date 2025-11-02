package se331.metricbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se331.metricbackend.dao.CartDao;
import se331.metricbackend.dao.GameDao;

import se331.metricbackend.dto.CartItemDTO;
import se331.metricbackend.entity.Cart;
import se331.metricbackend.entity.Game;
import se331.metricbackend.security.user.User;
import se331.metricbackend.security.user.UserDao;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    final CartDao cartDao;
    final UserDao userDao;
    final GameDao gameDao;

    @Override
    public Cart getCartForCurrentUser() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user); // 1. ดึงตะกร้ามา

        // 🔽🔽🔽 (แก้ไข) เพิ่ม Logic ตรงนี้ 🔽🔽🔽

        Double oldPrice = cart.getTotalPrice(); // 2. เก็บราคาเดิม (อาจจะเป็น null)
        recalculateTotalPrice(cart);            // 3. คำนวณราคาใหม่
        Double newPrice = cart.getTotalPrice(); // 4. เอาราคาที่คำนวณใหม่

        // 5. (สำคัญ) ถ้าไม่เท่ากัน (เช่น DB เป็น null หรือราคาเก่า) ค่อย save
        //    เพื่อป้องกันการ Save ซ้ำซ้อนโดยไม่จำเป็นใน GET request
        if (!Objects.equals(oldPrice, newPrice)) {
            cartDao.save(cart);
        }

        return cart; // 6. ส่งตะกร้าที่ราคารวมถูกต้องกลับไป
    }

    @Override
    public Cart addItemToCart(CartItemDTO itemDTO) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        Game game = gameDao.findById(itemDTO.getGameId())
                .orElseThrow(() -> new RuntimeException("Game not found"));

        Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getGame().getId().equals(itemDTO.getGameId())
                        && item.getPlatform().equals(itemDTO.getPlatform()))
                .findFirst();

        if (existingItem.isPresent()) {
            Cart.CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + itemDTO.getQuantity());
        } else {
            Cart.CartItem newItem = Cart.CartItem.builder()
                    .game(game)
                    .platform(itemDTO.getPlatform())
                    .quantity(itemDTO.getQuantity())
                    .title(game.getTitle())
                    .mainImageUrl(game.getMainImageUrl())
                    .priceAtPurchase(game.getPromotionPrice() != null ? game.getPromotionPrice() : game.getPrice())
                    .build();
            cart.getItems().add(newItem);
        }

        // 🔽🔽🔽 (1) คำนวณราคารวมก่อน Save 🔽🔽🔽
        recalculateTotalPrice(cart);
        return cartDao.save(cart);
    }

    @Override
    public Cart removeItemFromCart(String gameId, String platform) {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);

        cart.getItems().removeIf(item ->
                item.getGame().getId().equals(gameId) && item.getPlatform().equals(platform)
        );

        // 🔽🔽🔽 (2) คำนวณราคารวมก่อน Save 🔽🔽🔽
        recalculateTotalPrice(cart);
        return cartDao.save(cart);
    }

    @Override
    public Cart clearCart() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();

        // 🔽🔽🔽 (3) คำนวณราคารวมก่อน Save (ซึ่งจะได้ 0) 🔽🔽🔽
        recalculateTotalPrice(cart);
        return cartDao.save(cart);
    }

    /**
     * (Helper) ดึง User ที่ล็อกอินอยู่ปัจจุบัน
     */
    private User getCurrentUser() {
        // ... (โค้ดเดิม) ...
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userDao.findByUsername(username);
    }

    /**
     * (Helper) ดึงตะกร้าของ User ถ้าไม่มี ให้สร้างใหม่
     */
    private Cart getOrCreateCart(User user) {
        // ... (โค้ดเดิม) ...
        Optional<Cart> optionalCart = cartDao.findByUserId(user.getId());

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            // ตอนสร้างใหม่ totalPrice จะเป็น 0.0 (จาก @Builder.Default ใน Entity)
            Cart newCart = Cart.builder()
                    .user(user)
                    .build();
            Cart savedCart = cartDao.save(newCart);

            user.setCart(savedCart);
            userDao.save(user);

            return savedCart;
        }
    }

    // 🔽🔽🔽 (4) สร้างเมธอด Helper สำหรับคำนวณราคา 🔽🔽🔽
    /**
     * (Helper) คำนวณราคารวมของตะกร้า
     */
    private void recalculateTotalPrice(Cart cart) {
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            cart.setTotalPrice(0.0);
            return;
        }

        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();

        cart.setTotalPrice(total);
    }
}