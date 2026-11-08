package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Event;
import com.example.demo.Entity.Reservation;
import com.example.demo.Entity.User;
import com.example.demo.exception.NoSeatsAvailableException;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation trouverReservationParId(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
    }

    @Transactional
    public Reservation ajouterReservation(Long userId, Long eventId, int seats) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        if (seats > placesRestantes(event)) {
            throw new NoSeatsAvailableException(
                    "Plus assez de places disponibles pour cet événement");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);
        reservation.setSeats(seats);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation modifierReservation(Long id, int seats,
                                           Reservation.ReservationStatus status) {
        Reservation reservation = trouverReservationParId(id);

        Long total = reservationRepository.countReservedSeats(reservation.getEvent().getId());
        int autres = (total == null) ? 0 : total.intValue();

        if (reservation.getStatus() != Reservation.ReservationStatus.CANCELLED) {
            autres = autres - reservation.getSeats();
        }

        if (status != Reservation.ReservationStatus.CANCELLED
                && autres + seats > reservation.getEvent().getCapacity()) {
            throw new NoSeatsAvailableException(
                    "Plus assez de places disponibles pour cet événement");
        }

        reservation.setSeats(seats);
        reservation.setStatus(status);

        return reservationRepository.save(reservation);
    }

    public void supprimerReservation(Long id) {
        reservationRepository.delete(trouverReservationParId(id));
    }

    public int placesRestantes(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));

        return placesRestantes(event);
    }

    private int placesRestantes(Event event) {
        Long total = reservationRepository.countReservedSeats(event.getId());
        int reservees = (total == null) ? 0 : total.intValue();

        return event.getCapacity() - reservees;
    }

    public List<Reservation> rechercherParUtilisateurEtPeriode(
            Long userId, LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByUserAndPeriod(userId, start, end);
    }
}