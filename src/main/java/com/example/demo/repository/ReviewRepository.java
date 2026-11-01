package com.example.demo.repository;

import com.example.demo.dto.EventRatingDTO;
import com.example.demo.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByEventId(Long eventId);

    List<Review> findByUserId(Long userId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    /**
     * Requête perso 1 : événements d'une catégorie triés par note moyenne (décroissante).
     * LEFT JOIN pour garder aussi les événements sans avis (moyenne null → en dernier).
     */
    @Query("""
           SELECT new com.example.demo.dto.EventRatingDTO(
                  e.id, e.title, AVG(r.rating), COUNT(r))
           FROM Event e
           LEFT JOIN Review r ON r.event = e
           WHERE e.category.id = :categoryId
           GROUP BY e.id, e.title
           ORDER BY AVG(r.rating) DESC NULLS LAST
           """)
    List<EventRatingDTO> findEventsByCategoryOrderByAverageRating(@Param("categoryId") Long categoryId);

    /** Requête perso 2 : note moyenne d'un événement. */
    @Query("""
           SELECT new com.example.demo.dto.EventRatingDTO(
                  e.id, e.title, AVG(r.rating), COUNT(r))
           FROM Event e
           LEFT JOIN Review r ON r.event = e
           WHERE e.id = :eventId
           GROUP BY e.id, e.title
           """)
    Optional<EventRatingDTO> findAverageRatingByEvent(@Param("eventId") Long eventId);

    /** Variante : note moyenne de tous les événements. */
    @Query("""
           SELECT new com.example.demo.dto.EventRatingDTO(
                  e.id, e.title, AVG(r.rating), COUNT(r))
           FROM Review r JOIN r.event e
           GROUP BY e.id, e.title
           ORDER BY AVG(r.rating) DESC
           """)
    List<EventRatingDTO> findAverageRatingForAllEvents();
}
