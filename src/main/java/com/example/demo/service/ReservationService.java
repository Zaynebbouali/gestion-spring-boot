
package com.example.demo.service;

import com.example.demo.Entity.Event;
import com.example.demo.Entity.Reservation;
import com.example.demo.Entity.User;
import com.example.demo.exception.NoSeatsAvailableException;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public ReservationService(ReservationRepository reservationRepository,
                               UserRepository userRepository,
                               EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    // Afficher toutes les réservations
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // Chercher une réservation par ID
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable"));
    }

    // Ajouter une réservation
    @Transactional
    public Reservation create(Long userId, Long eventId, int seats) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable"));

        if (seats > remainingSeats(event)) {
            throw new NoSeatsAvailableException(
                    "Plus assez de places disponibles pour cet événement");
        }

        Reservation r = new Reservation();
        r.setUser(user);
        r.setEvent(event);
        r.setSeats(seats);

        return reservationRepository.save(r);
    }

    // Modifier une réservation
    @Transactional
    public Reservation update(Long id, int seats,
                              Reservation.ReservationStatus status) {

        Reservation r = findById(id);

        Long total = reservationRepository.countReservedSeats(r.getEvent().getId());

        if (total == null) {
            total = 0L;
        }

        int others = total.intValue();

        if (r.getStatus() != Reservation.ReservationStatus.CANCELLED) {
            others = others - r.getSeats();
        }

        if (status != Reservation.ReservationStatus.CANCELLED
                && others + seats > r.getEvent().getCapacity()) {
            throw new NoSeatsAvailableException(
                    "Plus assez de places disponibles pour cet événement");
        }

        r.setSeats(seats);
        r.setStatus(status);

        return reservationRepository.save(r);
    }

    // Supprimer une réservation
    public void delete(Long id) {
        reservationRepository.delete(findById(id));
    }

    // Calculer les places restantes
    public int remainingSeats(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable"));

        return remainingSeats(event);
    }

    private int remainingSeats(Event event) {

        Long total = reservationRepository.countReservedSeats(event.getId());

        if (total == null) {
            total = 0L;
        }

        return event.getCapacity() - total.intValue();
    }

    // Rechercher les réservations par utilisateur et période
    public List<Reservation> findByUserAndPeriod(
            Long userId, LocalDateTime start, LocalDateTime end) {

        return reservationRepository.findByUserAndPeriod(userId, start, end);
    }
}
