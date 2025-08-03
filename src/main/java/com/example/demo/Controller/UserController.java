package com.example.demo.Controller;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.Entities.User;
import com.example.demo.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.register(user);
            User responseUser = new User();
            responseUser.setId(savedUser.getId());
            responseUser.setUsername(savedUser.getUsername());
            responseUser.setEmail(savedUser.getEmail());
// Do not set the password at all
            return ResponseEntity.ok(responseUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

            User responseUser = new User();
            responseUser.setId(user.getId());
            responseUser.setUsername(user.getUsername());
            responseUser.setEmail(user.getEmail());
            // Don't return password

            return ResponseEntity.ok(responseUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by username
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
