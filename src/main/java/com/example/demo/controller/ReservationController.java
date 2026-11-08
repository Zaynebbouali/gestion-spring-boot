package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Reservation;
import com.example.demo.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Corps JSON : {"userId":1,"eventId":2,"seats":3}
    public record ReservationRequest(Long userId, Long eventId, int seats) { }

    // Corps JSON : {"seats":2,"status":"CONFIRMED"}
    public record ModificationRequest(int seats, Reservation.ReservationStatus status) { }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation ajouterReservation(@RequestBody ReservationRequest req) {
        return reservationService.create(req.userId(), req.eventId(), req.seats());
    }

    @GetMapping
    public List<Reservation> listerReservations() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public Reservation afficherReservation(@PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PutMapping("/{id}")
    public Reservation modifierReservation(@PathVariable Long id, @RequestBody ModificationRequest req) {
        return reservationService.update(id, req.seats(), req.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerReservation(@PathVariable Long id) {
        reservationService.delete(id);
    }

    @GetMapping("/event/{eventId}/remaining")
    public Map<String, Integer> afficherPlacesRestantes(@PathVariable Long eventId) {
        return Map.of("remainingSeats", reservationService.remainingSeats(eventId));
    }

    // Ex : /api/reservations/search?userId=1&start=2026-01-01T00:00:00&end=2026-12-31T23:59:59
    @GetMapping("/search")
    public List<Reservation> rechercherReservations(
            @RequestParam Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return reservationService.findByUserAndPeriod(userId, start, end);
    }
}