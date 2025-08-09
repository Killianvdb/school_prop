package com.example.demo.Controller;

import com.example.demo.Entities.Cart;
import com.example.demo.Entities.CartItem;
import com.example.demo.Entities.Item;
import com.example.demo.Entities.User;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Repository.CartItemRepo;
import com.example.demo.Repository.ItemRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // GET: All carts
    @GetMapping(produces = "application/json")
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // GET: Cart by userId
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return cartRepository.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new Cart()));
    }

    // POST: Add item to cart
    @PostMapping("/{userId}/add")
    public ResponseEntity<CartItem> addItemToCart(@PathVariable Long userId,
                                                  @RequestParam Long itemId,
                                                  @RequestParam(defaultValue = "1") int quantity) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) return ResponseEntity.notFound().build();

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);

        return ResponseEntity.ok(cartItemRepository.save(cartItem));
    }

    // DELETE: Remove item from cart
    @DeleteMapping("/{userId}/remove/{cartItemId}")
    public ResponseEntity<String> removeItemFromCart(@PathVariable Long userId, @PathVariable Long cartItemId) {
        if (!userRepository.existsById(userId)) return ResponseEntity.notFound().build();
        cartItemRepository.deleteById(cartItemId);
        return ResponseEntity.ok("Item removed from cart");
    }

    // POST: Checkout (clear cart)
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<String> checkoutCart(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart != null && cart.getItems() != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }

        return ResponseEntity.ok("Checkout successful");
    }
}
