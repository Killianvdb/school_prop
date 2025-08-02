package com.example.demo.Service;

import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // WARNING: storing raw passwords is insecure. Only for demonstration!
        user.setPassword(user.getPassword());

        return userRepository.save(user);
    }

}
