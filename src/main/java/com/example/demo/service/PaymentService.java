package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Payment;
import com.example.demo.Entity.Reservation;
import com.example.demo.Entity.enums.PaymentStatus;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReservationRepository;

@Service
@Transactional
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final ReservationRepository reservationRepository;

	public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
		this.paymentRepository = paymentRepository;
		this.reservationRepository = reservationRepository;
	}

	public PaymentResponse create(PaymentRequest req) {
		validate(req);
		Reservation reservation = reservationRepository.findById(req.reservationId())
				.orElseThrow(() -> new ResourceNotFoundException("Réservation", req.reservationId()));

		if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
			throw new BusinessException("Impossible de payer une réservation annulée.");
		}

		// OneToOne : une réservation ne peut avoir qu'un seul paiement
		if (paymentRepository.existsByReservationId(reservation.getId())) {
			throw new BusinessException("Cette réservation possède déjà un paiement.");
		}

		Payment payment = new Payment();
		payment.setAmount(req.amount());
		payment.setMethod(req.method());
		payment.setStatus(req.status() != null ? req.status() : PaymentStatus.EN_ATTENTE);
		payment.setReservation(reservation);
		return PaymentResponse.from(paymentRepository.save(payment));
	}

	@Transactional(readOnly = true)
	public List<PaymentResponse> findAll() {
		return paymentRepository.findAll().stream().map(PaymentResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public PaymentResponse findById(Long id) {
		return PaymentResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public PaymentResponse findByReservation(Long reservationId) {
		return paymentRepository.findByReservationId(reservationId)
				.map(PaymentResponse::from)
				.orElseThrow(() -> new BusinessException("Aucun paiement pour la réservation " + reservationId));
	}

	public PaymentResponse update(Long id, PaymentRequest req) {
		validate(req);
		Payment payment = getOrThrow(id);

		if (!payment.getReservation().getId().equals(req.reservationId())) {
			if (paymentRepository.existsByReservationId(req.reservationId())) {
				throw new BusinessException("La réservation cible possède déjà un paiement.");
			}
			Reservation reservation = reservationRepository.findById(req.reservationId())
					.orElseThrow(() -> new ResourceNotFoundException("Réservation", req.reservationId()));
			payment.setReservation(reservation);
		}

		payment.setAmount(req.amount());
		payment.setMethod(req.method());
		if (req.status() != null) payment.setStatus(req.status());
		return PaymentResponse.from(payment);
	}

	public void delete(Long id) {
		paymentRepository.delete(getOrThrow(id));
	}

	// Validation des données reçues
	private void validate(PaymentRequest req) {
		if (req.amount() == null || req.amount().signum() <= 0) {
			throw new BusinessException("Le montant est obligatoire et doit être supérieur à 0.");
		}
		if (req.method() == null) {
			throw new BusinessException("La méthode de paiement est obligatoire (CARTE, ESPECES, VIREMENT, D17).");
		}
		if (req.reservationId() == null) {
			throw new BusinessException("L'id de la réservation est obligatoire.");
		}
	}

	private Payment getOrThrow(Long id) {
		return paymentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Paiement", id));
	}
}
