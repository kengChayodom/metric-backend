package se331.metricbackend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se331.metricbackend.entity.Cart;
import se331.metricbackend.repository.CartRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartDaoImpl implements CartDao {

    final CartRepository cartRepository;

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return cartRepository.findByUser_Id(userId);
    }
}