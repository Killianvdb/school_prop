package com.example.demo.Repository;

import com.example.demo.Entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
}
