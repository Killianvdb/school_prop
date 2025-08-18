package com.example.demo.Controller;

import com.example.demo.Entities.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.CartService;
import com.example.demo.Service.ReservationService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final UserService userService;

    private final CartService cartService;


    @Autowired
    public CartController(CartRepo cartRepository,
                          CartItemRepo cartItemRepository,
                          ItemRepository itemRepository,
                          UserRepository userRepository,
                          ReservationService reservationService,
                          UserService userService,
                          CartService cartService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.reservationService = reservationService;
        this.userService = userService;
        this.cartService = cartService;
    }

    // Get cart for logged-in user
    @GetMapping("/my")
    public ResponseEntity<Cart> getMyCart(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return cartRepository.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new Cart()));
    }

    // Add item to logged-in user's cart (now with dates)
    @PostMapping("/my/add")
    public ResponseEntity<?> addItemToMyCart(
            Authentication authentication,
            @RequestParam Long itemId,
            @RequestParam(defaultValue = "1") int quantity,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        User user = getAuthenticatedUser(authentication);

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (end.isBefore(start)) {
            return ResponseEntity.badRequest().body("End date must be after start date");
        }

        // If item already in cart for SAME dates → increase quantity
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(ci -> ci.getItem().getId().equals(itemId)
                        && start.equals(ci.getStartDate())
                        && end.equals(ci.getEndDate()))
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
        cartItem.setStartDate(start);
        cartItem.setEndDate(end);

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

    // Checkout for logged-in user -> creates a Reservation
    @PostMapping("/my/checkout")
    public ResponseEntity<?> checkoutMyCart(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Reservation reservation = reservationService.checkout(user);
        return ResponseEntity.ok(reservation);
    }

    // Helper method to get authenticated user from Spring Security
    private User getAuthenticatedUser(Authentication authentication) {
        String emailOrUsername = authentication.getName();
        // Try email first, then username
        return userRepository.findByEmail(emailOrUsername)
                .or(() -> userRepository.findByUsername(emailOrUsername))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            Authentication authentication,
            @RequestParam Long itemId,
            @RequestParam int quantity) {

        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(cartService.addItem(user, itemId, quantity));
    }
}
