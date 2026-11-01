package com.example.demo.dto;

import com.example.demo.Entity.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long userId,
        Long eventId
) {
    public static ReviewResponse from(Review r) {
        return new ReviewResponse(r.getId(), r.getRating(), r.getComment(), r.getCreatedAt(),
                r.getUser().getId(), r.getEvent().getId());
    }
}
