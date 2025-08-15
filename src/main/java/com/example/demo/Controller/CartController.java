package com.example.demo.Controller;

import com.example.demo.Entities.Cart;
import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Item;
import com.example.demo.Entities.User;
import com.example.demo.Repository.CartItemRepo;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartController(CartRepo cartRepository,
                          CartItemRepo cartItemRepository,
                          ItemRepository itemRepository,
                          UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    // Get cart for logged-in user
    @GetMapping("/my")
    public ResponseEntity<Cart> getMyCart(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return cartRepository.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new Cart()));
    }

    // Add item to logged-in user's cart
    @PostMapping("/my/add")
    public ResponseEntity<CartItem> addItemToMyCart(
            Authentication authentication,
            @RequestParam Long itemId,
            @RequestParam(defaultValue = "1") int quantity) {

        User user = getAuthenticatedUser(authentication);

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // If item already in cart → increase quantity
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(ci -> ci.getItem().getId().equals(itemId))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            return ResponseEntity.ok(cartItemRepository.save(existingItem));
        }

        // Else create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);

        return ResponseEntity.ok(cartItemRepository.save(cartItem));
    }

    // Remove item from logged-in user's cart
    @DeleteMapping("/my/remove/{cartItemId}")
    public ResponseEntity<String> removeItemFromMyCart(Authentication authentication,
                                                       @PathVariable Long cartItemId) {
        User user = getAuthenticatedUser(authentication);

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        boolean removed = cart.getItems().removeIf(ci -> ci.getId().equals(cartItemId));
        if (removed) {
            cartItemRepository.deleteById(cartItemId);
            cartRepository.save(cart);
            return ResponseEntity.ok("Item removed from cart");
        }
        return ResponseEntity.badRequest().body("Item not found in your cart");
    }

    // Checkout for logged-in user
    @PostMapping("/my/checkout")
    public ResponseEntity<String> checkoutMyCart(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);

        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart != null && cart.getItems() != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
        return ResponseEntity.ok("Checkout successful");
    }

    // Helper method to get authenticated user from Spring Security
    private User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
