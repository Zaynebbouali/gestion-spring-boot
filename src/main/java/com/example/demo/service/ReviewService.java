package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Event;
import com.example.demo.Entity.Review;
import com.example.demo.Entity.User;
import com.example.demo.dto.EventRatingDTO;
import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;

@Service
@Transactional
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;
	private final EventRepository eventRepository;

	public ReviewService(ReviewRepository reviewRepository, PaymentRepository paymentRepository,
			UserRepository userRepository, EventRepository eventRepository) {
		this.reviewRepository = reviewRepository;
		this.paymentRepository = paymentRepository;
		this.userRepository = userRepository;
		this.eventRepository = eventRepository;
	}

	public ReviewResponse create(ReviewRequest req) {
		validate(req);
		User user = userRepository.findById(req.userId())
				.orElseThrow(() -> new ResourceNotFoundException("Utilisateur", req.userId()));
		Event event = eventRepository.findById(req.eventId())
				.orElseThrow(() -> new ResourceNotFoundException("Événement", req.eventId()));

		checkParticipation(user.getId(), event);

		if (reviewRepository.existsByUserIdAndEventId(user.getId(), event.getId())) {
			throw new BusinessException("Vous avez déjà donné un avis pour cet événement.");
		}

		Review review = new Review();
		review.setRating(req.rating());
		review.setComment(req.comment());
		review.setUser(user);
		review.setEvent(event);
		return ReviewResponse.from(reviewRepository.save(review));
	}

	@Transactional(readOnly = true)
	public List<ReviewResponse> findAll() {
		return reviewRepository.findAll().stream().map(ReviewResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ReviewResponse findById(Long id) {
		return ReviewResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public List<ReviewResponse> findByEvent(Long eventId) {
		return reviewRepository.findByEventId(eventId).stream().map(ReviewResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public List<ReviewResponse> findByUser(Long userId) {
		return reviewRepository.findByUserId(userId).stream().map(ReviewResponse::from).toList();
	}

	// Mise à jour : seuls la note et le commentaire sont modifiables
	public ReviewResponse update(Long id, ReviewRequest req) {
		validate(req);
		Review review = getOrThrow(id);
		if (!review.getUser().getId().equals(req.userId())
				|| !review.getEvent().getId().equals(req.eventId())) {
			throw new BusinessException("L'auteur et l'événement d'un avis ne peuvent pas être modifiés.");
		}
		review.setRating(req.rating());
		review.setComment(req.comment());
		return ReviewResponse.from(review);
	}

	public void delete(Long id) {
		reviewRepository.delete(getOrThrow(id));
	}

	// ---------- Requêtes personnalisées ----------

	@Transactional(readOnly = true)
	public List<EventRatingDTO> eventsByCategorySortedByRating(Long categoryId) {
		return reviewRepository.findEventsByCategoryOrderByAverageRating(categoryId);
	}

	@Transactional(readOnly = true)
	public EventRatingDTO averageRatingOfEvent(Long eventId) {
		return reviewRepository.findAverageRatingByEvent(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Événement", eventId));
	}

	@Transactional(readOnly = true)
	public List<EventRatingDTO> averageRatingOfAllEvents() {
		return reviewRepository.findAverageRatingForAllEvents();
	}

	// ---------- Règle métier ----------

	/**
	 * Un avis n'est autorisé qu'après participation :
	 *  1. l'utilisateur a une réservation payée (PAYE) et non annulée pour cet événement ;
	 *  2. l'événement a déjà commencé (startDate dans le passé).
	 */
	private void checkParticipation(Long userId, Event event) {
		if (!paymentRepository.hasPaidReservation(userId, event.getId())) {
			throw new BusinessException("Avis refusé : aucune réservation payée pour cet événement.");
		}
		if (event.getStartDate() != null && event.getStartDate().isAfter(LocalDateTime.now())) {
			throw new BusinessException("Avis refusé : l'événement n'a pas encore eu lieu.");
		}
	}

	// Validation des données reçues
	private void validate(ReviewRequest req) {
		if (req.rating() == null || req.rating() < 1 || req.rating() > 5) {
			throw new BusinessException("La note est obligatoire et doit être entre 1 et 5.");
		}
		if (req.comment() != null && req.comment().length() > 1000) {
			throw new BusinessException("Le commentaire ne doit pas dépasser 1000 caractères.");
		}
		if (req.userId() == null || req.eventId() == null) {
			throw new BusinessException("userId et eventId sont obligatoires.");
		}
	}

	private Review getOrThrow(Long id) {
		return reviewRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Avis", id));
	}
}
