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
        select distinct r from Reservation r
        join r.items ri
        where ri.item.id in (:itemIds)
          and r.startDate <= :endDate
          and r.endDate >= :startDate
    """)
    List<Reservation> findOverlapping(List<Long> itemIds, LocalDate startDate, LocalDate endDate);
}