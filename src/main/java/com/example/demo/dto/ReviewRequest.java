package com.example.demo.dto;

/** Données envoyées pour créer / modifier un avis (vérifiées dans ReviewService). */
public record ReviewRequest(
		Integer rating,
		String comment,
		Long userId,
		Long eventId
) {}
