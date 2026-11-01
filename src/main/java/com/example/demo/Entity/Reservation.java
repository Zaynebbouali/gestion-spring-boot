package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    public enum ReservationStatus { PENDING, CONFIRMED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationDate = LocalDateTime.now();

    @Column(nullable = false)
    private int seats = 1;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // Entité du Membre 2 : doit avoir getCapacity()
    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    // Entité du Membre 4 : Payment porte la clé étrangère (champ "reservation")
    @JsonIgnore
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    public Reservation() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }
}