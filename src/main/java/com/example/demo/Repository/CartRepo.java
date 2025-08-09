package com.example.demo.Repository;

import com.example.demo.Entities.Cart;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CartRepo  extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
