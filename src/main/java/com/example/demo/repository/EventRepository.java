package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

	// Requête perso 1 : événements à venir dans une ville
	@Query("SELECT e FROM Event e WHERE e.venue.city = :city AND e.startDate > :maintenant ORDER BY e.startDate ASC")
	List<Event> trouverEvenementsAVenirParVille(@Param("city") String city,
			@Param("maintenant") LocalDateTime maintenant);

	// Requête perso 2 (top 5 réservés) : à ajouter quand l'entité Reservation existe
	// (voir le guide, étape 9)
}
