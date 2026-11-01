package com.example.demo.controller;

import com.example.demo.dto.EventRatingDTO;
import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.example.demo.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ---------- CRUD ----------

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@RequestBody ReviewRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(req));
    }

    @GetMapping
    public List<ReviewResponse> findAll() {
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public ReviewResponse findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    @GetMapping("/event/{eventId}")
    public List<ReviewResponse> findByEvent(@PathVariable Long eventId) {
        return reviewService.findByEvent(eventId);
    }

    @GetMapping("/user/{userId}")
    public List<ReviewResponse> findByUser(@PathVariable Long userId) {
        return reviewService.findByUser(userId);
    }

    @PutMapping("/{id}")
    public ReviewResponse update(@PathVariable Long id, @RequestBody ReviewRequest req) {
        return reviewService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Requêtes personnalisées ----------

    /** Événements d'une catégorie triés par note moyenne décroissante. */
    @GetMapping("/stats/category/{categoryId}")
    public List<EventRatingDTO> eventsByCategorySortedByRating(@PathVariable Long categoryId) {
        return reviewService.eventsByCategorySortedByRating(categoryId);
    }

    /** Note moyenne d'un événement. */
    @GetMapping("/stats/event/{eventId}")
    public EventRatingDTO averageRatingOfEvent(@PathVariable Long eventId) {
        return reviewService.averageRatingOfEvent(eventId);
    }

    /** Note moyenne de tous les événements notés. */
    @GetMapping("/stats/events")
    public List<EventRatingDTO> averageRatingOfAllEvents() {
        return reviewService.averageRatingOfAllEvents();
    }
}
