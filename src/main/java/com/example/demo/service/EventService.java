package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Category;
import com.example.demo.Entity.Event;
import com.example.demo.Entity.Speaker;
import com.example.demo.Entity.User;
import com.example.demo.Entity.Venue;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.SpeakerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VenueRepository;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private SpeakerRepository speakerRepository;

	@Autowired
	private VenueRepository venueRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	// CREATE
	public Event creerEvent(Event event) {
		remplirRelations(event);
		verifierReglesMetier(event);
		return eventRepository.save(event);
	}

	// READ ALL
	public List<Event> listerEvents() {
		return eventRepository.findAll();
	}

	// READ BY ID
	public Event trouverEventParId(Long id) {
		return eventRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Event non trouvé"));
	}

	// UPDATE
	public Event mettreAJourEvent(Long id, Event details) {
		Event event = trouverEventParId(id);
		event.setTitle(details.getTitle());
		event.setDescription(details.getDescription());
		event.setStartDate(details.getStartDate());
		event.setEndDate(details.getEndDate());
		event.setCapacity(details.getCapacity());
		event.setCategory(details.getCategory());
		event.setVenue(details.getVenue());
		event.setOrganizer(details.getOrganizer());
		event.setSpeakers(details.getSpeakers());

		remplirRelations(event);
		verifierReglesMetier(event);
		return eventRepository.save(event);
	}

	// DELETE
	public Long supprimerEvent(Long id) {
		Event event = trouverEventParId(id);
		eventRepository.delete(event);
		return id;
	}

	// Ajouter un speaker à un event
	public Event ajouterSpeakerAEvent(Long eventId, Long speakerId) {
		Event event = trouverEventParId(eventId);
		Speaker speaker = speakerRepository.findById(speakerId)
				.orElseThrow(() -> new IllegalArgumentException("Speaker non trouvé"));
		boolean dejaAjoute = event.getSpeakers().stream()
				.anyMatch(s -> s.getId().equals(speakerId));
		if (!dejaAjoute) {
			event.getSpeakers().add(speaker);
		}
		return eventRepository.save(event);
	}

	// Requête perso : événements à venir par ville
	public List<Event> listerEvenementsAVenirParVille(String city) {
		return eventRepository.trouverEvenementsAVenirParVille(city, LocalDateTime.now());
	}

	// ---------- Méthodes privées ----------

	// Le JSON ne contient que {"id": X} pour category / venue / organizer / speakers :
	// on recharge les vrais objets depuis la base.
	private void remplirRelations(Event event) {
		if (event.getVenue() == null || event.getVenue().getId() == null) {
			throw new IllegalStateException("La salle (venue) est obligatoire");
		}
		Venue venue = venueRepository.findById(event.getVenue().getId())
				.orElseThrow(() -> new IllegalArgumentException("Venue non trouvée"));
		event.setVenue(venue);

		if (event.getCategory() != null && event.getCategory().getId() != null) {
			Category category = categoryRepository.findById(event.getCategory().getId())
					.orElseThrow(() -> new IllegalArgumentException("Category non trouvée"));
			event.setCategory(category);
		}

		if (event.getOrganizer() != null && event.getOrganizer().getId() != null) {
			User organizer = userRepository.findById(event.getOrganizer().getId())
					.orElseThrow(() -> new IllegalArgumentException("Organizer (User) non trouvé"));
			event.setOrganizer(organizer);
		}

		List<Speaker> speakers = new ArrayList<>();
		if (event.getSpeakers() != null) {
			for (Speaker s : event.getSpeakers()) {
				Speaker speaker = speakerRepository.findById(s.getId())
						.orElseThrow(() -> new IllegalArgumentException("Speaker non trouvé : " + s.getId()));
				speakers.add(speaker);
			}
		}
		event.setSpeakers(speakers);
	}

	// Règles métier
	private void verifierReglesMetier(Event event) {
		// Règle 1 : date de fin après date de début
		if (event.getStartDate() == null || event.getEndDate() == null) {
			throw new IllegalStateException("Les dates de début et de fin sont obligatoires");
		}
		if (!event.getEndDate().isAfter(event.getStartDate())) {
			throw new IllegalStateException("La date de fin doit être après la date de début");
		}

		// Règle 2 : capacité de la salle respectée
		if (event.getCapacity() == null || event.getCapacity() <= 0) {
			throw new IllegalStateException("La capacité de l'événement doit être supérieure à 0");
		}
		if (event.getCapacity() > event.getVenue().getCapacity()) {
			throw new IllegalStateException("La capacité de l'événement (" + event.getCapacity()
					+ ") dépasse la capacité de la salle (" + event.getVenue().getCapacity() + ")");
		}
	}
}
