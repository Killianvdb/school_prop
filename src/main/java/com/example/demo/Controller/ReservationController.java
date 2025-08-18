package com.example.demo.Controller;

import com.example.demo.Entities.Reservation;
import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> myReservations(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .or(() -> userRepository.findByUsername(authentication.getName()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(reservationService.myReservations(user));
    }
}
