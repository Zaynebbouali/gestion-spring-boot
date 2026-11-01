package com.example.demo.dto;

/**
 * Résultat des requêtes personnalisées : un événement avec sa note moyenne
 * et son nombre d'avis. Construit directement par JPQL (constructor expression).
 */
public record EventRatingDTO(
        Long eventId,
        String eventTitle,
        Double averageRating,
        Long reviewCount
) {}
