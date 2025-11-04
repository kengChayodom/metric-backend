package se331.metricbackend.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

// --- DTOs ---
import se331.metricbackend.dto.*;

// --- Entities / Documents ---
import se331.metricbackend.entity.*; // ◀️ Import Entities
import se331.metricbackend.security.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface LapMapper {
    LapMapper INSTANCE = Mappers.getMapper(LapMapper.class);

    // ===================================
    // 1. Game Mappers
    // ===================================

    // (ขาเข้า)
    @Mapping(target = "categories", ignore = true)
    Game toGame(GameDTO gameDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateGameFromDto(GameDTO gameDTO, @MappingTarget Game game);

    // (ขาออก)
    @Mapping(source = "categories", target = "categoryIds")
    GameDTO toGameDTO(Game game);

    default List<GameDTO> toGameDTOs(List<Game> games) {
        if (games == null) {
            return null;
        }
        return games.stream()
                .map(this::toGameDTO)
                .collect(Collectors.toList());
    }

    default List<String> mapCategoriesToCategoryIds(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            return null;
        }
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    // ===================================
    // 2. Category Mappers
    // ===================================

    // (ขาเข้า)
    Category toCategory(CategoryDTO categoryDTO);
    @Mapping(target = "id", ignore = true)
    void updateCategoryFromDto(CategoryDTO categoryDTO, @MappingTarget Category category);

    // (ขาออก)
    CategoryDTO toCategoryDTO(Category category);
    List<CategoryDTO> toCategoryDTOs(List<Category> categories);

    // ===================================
    // 3. User Mapper
    // ===================================

    // (ขาออก)
    UserReporter getUserReporterDto(User user);

    List<UserReporter> getUserReporters(List<User> users);
    // ===================================
    // 4. Cart Mappers (ที่เติมให้)
    // ===================================

    // (ขาออก)
    // ◀️ fields 'id', 'totalPrice' จะถูก map อัตโนมัติถ้าชื่อตรงกัน
    @Mapping(source = "items", target = "items") // ◀️ Map List<CartItem> -> List<CartItemResponseDTO>
    CartDTO toCartDTO(Cart cart);

    // ◀️ นี่คือการ "Flatten" ข้อมูลจาก Game Entity
    @Mapping(source = "game.id", target = "gameId")
    @Mapping(source = "game.title", target = "title")
    @Mapping(source = "game.mainImageUrl", target = "mainImageUrl")
    @Mapping(source = "game.icongameUrl", target = "icongameUrl")
    @Mapping(source = "game.price", target = "price")
    @Mapping(source = "game.promotionPrice", target = "promotionPrice")
    // ◀️ 'platform' และ 'quantity' จะถูก map อัตโนมัติ
    CartItemResponseDTO toCartItemResponseDTO(Cart.CartItem cartItem); // ◀️ (สมมติว่า Entity ชื่อ CartItem)


    // ===================================
    // 5. Order Mappers (ที่เติมให้)
    // ===================================

    // (ขาออก)
    List<UserOrderDTO> toUserOrderDTOs(List<UserOrder> userOrders);

    // ◀️ fields 'id', 'orderDate', 'status', 'totalAmount' จะ map อัตโนมัติ
    @Mapping(source = "items", target = "items") // ◀️ Map List<OrderItem> -> List<OrderItemResponseDTO>
    UserOrderDTO toUserOrderDTO(UserOrder userOrder);

    // 🔽🔽🔽 แก้ไขตรงนี้ 🔽🔽🔽
    //
    // ลบ @Mapping ที่ไม่จำเป็นออก
    // MapStruct จะจัดการ field ที่ชื่อตรงกัน (priceAtPurchase, platform, quantity, title, mainImageUrl) ให้อัตโนมัติ
    // และมันจะใช้เมธอด toGameDTO(Game game) ที่มีอยู่แล้ว เพื่อแปลง 'game' (Entity) ไปเป็น 'game' (DTO) ให้เอง
    //
    OrderItemResponseDTO toOrderItemResponseDTO(UserOrder.OrderItem orderItem);
}