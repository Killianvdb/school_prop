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

    public CartService(CartRepo cartRepository, CartItemRepo cartItemRepository, ItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
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

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);

        cart.getItems().add(cartItem);
        cartItemRepository.save(cartItem);
        return cartRepository.save(cart);
    }

    public void removeItem(User user, Long cartItemId) {
        Cart cart = getCart(user);
        cart.getItems().removeIf(ci -> ci.getId().equals(cartItemId));
        cartRepository.save(cart);
    }

    public void checkout(User user) {
        Cart cart = getCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
