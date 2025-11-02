package se331.metricbackend.service;

import se331.metricbackend.dto.CartItemDTO;
import se331.metricbackend.entity.Cart;

public interface CartService {

    /**
     * ดึงตะกร้าปัจจุบันของผู้ใช้ที่ล็อกอินอยู่
     */
    Cart getCartForCurrentUser();

    /**
     * เพิ่ม/อัปเดต จำนวนสินค้าในตะกร้า
     * @param itemDTO ข้อมูลสินค้า (gameId, platform, quantity)
     * @return ตะกร้าที่อัปเดตแล้ว
     */
    Cart addItemToCart(CartItemDTO itemDTO);

    /**
     * ลบสินค้าออกจากตะกร้า
     * @param gameId ID ของเกม
     * @param platform แพลตฟอร์ม
     * @return ตะกร้าที่อัปเดตแล้ว
     */
    Cart removeItemFromCart(String gameId, String platform);

    /**
     * ล้างตะกร้าสินค้า (ใช้หลังจาก Checkout)
     */
    Cart clearCart();
}