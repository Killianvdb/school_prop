package com.example.demo.Entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ReservationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Reservation reservation;

    @ManyToOne(optional = false)
    private Item item;

    private int quantity;

    private LocalDate startDate;
    private LocalDate endDate;

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}
