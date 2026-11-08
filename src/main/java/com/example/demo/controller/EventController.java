package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Event;
import com.example.demo.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

	@Autowired
	private EventService eventService;

	@PostMapping
	public Event ajouterEvent(@RequestBody Event event) {
		return eventService.creerEvent(event);
	}

	@GetMapping
	public List<Event> listerEvents() {
		return eventService.listerEvents();
	}

	// Requête perso : événements à venir par ville
	@GetMapping("/a-venir")
	public List<Event> listerEvenementsProchains(@RequestParam String city) {
		return eventService.listerEvenementsAVenirParVille(city);
	}

	@GetMapping("/{id}")
	public Event afficherEvent(@PathVariable Long id) {
		return eventService.trouverEventParId(id);
	}

	@PutMapping("/{id}")
	public Event modifierEvent(@PathVariable Long id, @RequestBody Event event) {
		return eventService.mettreAJourEvent(id, event);
	}

	@DeleteMapping("/{id}")
	public Long supprimerEvent(@PathVariable Long id) {
		return eventService.supprimerEvent(id);
	}

	@PostMapping("/{eventId}/speakers/{speakerId}")
	public Event ajouterSpeakerAEvent(@PathVariable Long eventId, @PathVariable Long speakerId) {
		return eventService.ajouterSpeakerAEvent(eventId, speakerId);
	}
}