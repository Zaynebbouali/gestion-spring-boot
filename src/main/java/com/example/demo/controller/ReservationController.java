package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Reservation;
import com.example.demo.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Corps JSON : {"userId":1,"eventId":2,"seats":3}
    public record CreateRequest(Long userId, Long eventId, int seats) { }

    // Corps JSON : {"seats":2,"status":"CONFIRMED"}
    public record UpdateRequest(int seats, Reservation.ReservationStatus status) { }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation ajouterReservation(@RequestBody CreateRequest req) {
        return reservationService.ajouterReservation(req.userId(), req.eventId(), req.seats());
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable Long id) {
        return reservationService.trouverReservationParId(id);
    }

    @PutMapping("/{id}")
    public Reservation modifierReservation(@PathVariable Long id,
                                           @RequestBody UpdateRequest req) {
        return reservationService.modifierReservation(id, req.seats(), req.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerReservation(@PathVariable Long id) {
        reservationService.supprimerReservation(id);
    }

    @GetMapping("/event/{eventId}/remaining")
    public Map<String, Integer> placesRestantes(@PathVariable Long eventId) {
        return Map.of("remainingSeats", reservationService.placesRestantes(eventId));
    }

    // Ex : /api/reservations/search?userId=1&start=2026-01-01T00:00:00&end=2026-12-31T23:59:59
    @GetMapping("/search")
    public List<Reservation> rechercherParUtilisateurEtPeriode(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return reservationService.rechercherParUtilisateurEtPeriode(userId, start, end);
    }
}