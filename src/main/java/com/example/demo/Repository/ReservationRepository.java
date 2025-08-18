package com.example.demo.Repository;

import com.example.demo.Entities.Reservation;
import com.example.demo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);

    // Find reservations that overlap a period for any of the provided item IDs
    @Query("""
            SELECT COUNT(ri) > 0
            FROM ReservationItem ri
            WHERE ri.item.id = :itemId
              AND ri.startDate <= :endDate
              AND ri.endDate   >= :startDate
           """)
    boolean existsConflict(Long itemId, LocalDate startDate, LocalDate endDate);
}