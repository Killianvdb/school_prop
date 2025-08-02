package com.example.demo.Controller;

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
}
