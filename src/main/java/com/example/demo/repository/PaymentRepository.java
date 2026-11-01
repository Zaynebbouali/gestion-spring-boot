package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Payment;
import com.example.demo.Entity.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByReservationId(Long reservationId);

	boolean existsByReservationId(Long reservationId);

	List<Payment> findByStatus(PaymentStatus status);

	/**
	 * Vérifie qu'un utilisateur a une réservation PAYÉE et NON ANNULÉE pour un événement.
	 * Utilisé par la règle métier « un avis seulement après participation ».
	 */
	@Query("""
			SELECT COUNT(p) > 0 FROM Payment p
			WHERE p.reservation.user.id  = :userId
			  AND p.reservation.event.id = :eventId
			  AND p.status = com.example.demo.Entity.enums.PaymentStatus.PAYE
			  AND p.reservation.status <> com.example.demo.Entity.Reservation.ReservationStatus.CANCELLED
			""")
	boolean hasPaidReservation(@Param("userId") Long userId, @Param("eventId") Long eventId);
}
