package com.example.demo.Service;

import com.example.demo.Entities.*;
import com.example.demo.Repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartService {
    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ItemRepository itemRepository;
    private UserRepository userRepository;

    public CartService(CartRepo cartRepository, CartItemRepo cartItemRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Cart getCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    public Cart addItem(User user, Long itemId, int quantity) {
        Cart cart = getCart(user);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        CartItem existing = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId).orElse(null);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartItemRepository.save(existing);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
            cart.getItems().add(cartItem);
            cartItemRepository.save(cartItem);
        }
        return cartRepository.save(cart);
    }
    public Cart updateItemQuantity(User user, Long itemId, int quantity) {
        Cart cart = getCart(user);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        if (quantity <= 0) {
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        return cartRepository.save(cart);
    }

    public void removeItem(User user, Long itemId) {
        Cart cart = getCart(user);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    public void checkout(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
