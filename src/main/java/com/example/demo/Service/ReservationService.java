package com.example.demo.Service;

import com.example.demo.Entities.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final CartRepo cartRepo;

    @Autowired
    public ReservationService(ReservationRepository reservationRepo,
                              CartRepo cartRepo) {
        this.reservationRepo = reservationRepo;
        this.cartRepo = cartRepo;
    }
    public List<Reservation> myReservations(User user) {
        return reservationRepo.findByUser(user);
    }

    public Reservation checkout(User user) {
        Cart cart = cartRepo.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Validate all items have dates and no conflicts
        cart.getItems().forEach(ci -> {
            if (ci.getStartDate() == null || ci.getEndDate() == null) {
                throw new IllegalArgumentException("Start and end dates are required for all cart items");
            }
            if (ci.getEndDate().isBefore(ci.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date");
            }
            boolean conflict = reservationRepo.existsConflict(
                    ci.getItem().getId(), ci.getStartDate(), ci.getEndDate());
            if (conflict) {
                throw new IllegalArgumentException("Item '" + ci.getItem().getName() + "' is already reserved in that period");
            }
        });

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setUser(user);

        // Copy cart items into reservation items
        for (CartItem ci : cart.getItems()) {
            ReservationItem ri = new ReservationItem();
            ri.setReservation(reservation);
            ri.setItem(ci.getItem());
            ri.setQuantity(ci.getQuantity());
            ri.setStartDate(ci.getStartDate());
            ri.setEndDate(ci.getEndDate());
            reservation.getItems().add(ri);
        }

        // Persist reservation (cascades reservation items)
        Reservation saved = reservationRepo.save(reservation);

        // Clear the cart after successful reservation
        cart.getItems().clear();
        cartRepo.save(cart);

        return saved;
    }
}