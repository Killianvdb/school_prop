package com.example.demo.Service;

import com.example.demo.Entities.*;
import com.example.demo.Repository.CartRepo;
import com.example.demo.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CartRepo cartRepo;

    public ReservationService(ReservationRepository reservationRepository, CartRepo cartRepo) {
        this.reservationRepository = reservationRepository;
        this.cartRepo = cartRepo;
    }

    public List<Reservation> myReservations(User user) {
        return reservationRepository.findByUser(user);
    }

    @Transactional
    public Reservation checkout(User user, LocalDate start, LocalDate end) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid start/end dates");
        }

        Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new IllegalStateException("Cart not found"));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        // Availability check: any overlap for items in cart?
        List<Long> itemIds = cart.getItems().stream().map(ci -> ci.getItem().getId()).toList();
        boolean conflict = !reservationRepository.findOverlapping(itemIds, start, end).isEmpty();
        if (conflict) {
            throw new IllegalStateException("One or more items are already reserved in this period");
        }

        // Create reservation
        Reservation r = new Reservation();
        r.setUser(user);
        r.setStartDate(start);
        r.setEndDate(end);

        for (CartItem ci : cart.getItems()) {
            ReservationItem ri = new ReservationItem();
            ri.setReservation(r);
            ri.setItem(ci.getItem());
            ri.setQuantity(ci.getQuantity());
            r.getItems().add(ri);
        }

        Reservation saved = reservationRepository.save(r);

        // Clear cart
        cart.getItems().clear();
        cartRepo.save(cart);

        return saved;
    }
}