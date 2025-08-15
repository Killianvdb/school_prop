package com.example.demo.Repository;

import com.example.demo.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndItemId(Long cartId, Long itemId);
}
