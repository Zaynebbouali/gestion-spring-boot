
package com.example.demo.repository;

import com.example.demo.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Requête 1 : calculer les places réservées
    @Query("SELECT SUM(r.seats) FROM Reservation r WHERE r.event.id = :eventId AND r.status <> 'CANCELLED'")
    Long countReservedSeats(@Param("eventId") Long eventId);

    // Requête 2 : chercher les réservations par utilisateur et période
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.reservationDate BETWEEN :start AND :end")
    List<Reservation> findByUserAndPeriod(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
