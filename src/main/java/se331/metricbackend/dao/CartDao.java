package se331.metricbackend.dao;

import se331.metricbackend.entity.Cart;
import java.util.Optional;

public interface CartDao {
    Cart save(Cart cart);
    Optional<Cart> findByUserId(String userId);
}