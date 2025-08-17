package com.example.demo.Controller;

import com.example.demo.DTO.CheckoutRequest;
import com.example.demo.Entities.Reservation;
import com.example.demo.Entities.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Entities.CustomUserDetails;
import com.example.demo.Service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRepository userRepository;

    public ReservationController(ReservationService reservationService, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> myReservations(Authentication auth) {
        User me = ((CustomUserDetails) auth.getPrincipal()).getUser();
        return ResponseEntity.ok(reservationService.myReservations(me));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication auth, @RequestBody CheckoutRequest body) {
        try {
            User me = ((CustomUserDetails) auth.getPrincipal()).getUser();
            Reservation r = reservationService.checkout(me, body.getStartDate(), body.getEndDate());
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
