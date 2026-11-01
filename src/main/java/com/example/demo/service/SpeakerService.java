package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Entity.Event;
import com.example.demo.Entity.Speaker;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.SpeakerRepository;

@Service
public class SpeakerService {

	@Autowired
	private SpeakerRepository speakerRepository;

	@Autowired
	private EventRepository eventRepository;

	// CREATE
	public Speaker creerSpeaker(Speaker speaker) {
		return speakerRepository.save(speaker);
	}

	// READ ALL
	public List<Speaker> listerSpeakers() {
		return speakerRepository.findAll();
	}

	// READ BY ID
	public Speaker trouverSpeakerParId(Long id) {
		return speakerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Speaker non trouvé"));
	}

	// UPDATE
	public Speaker mettreAJourSpeaker(Long id, Speaker details) {
		Speaker speaker = trouverSpeakerParId(id);
		speaker.setName(details.getName());
		speaker.setEmail(details.getEmail());
		speaker.setBio(details.getBio());
		return speakerRepository.save(speaker);
	}

	// DELETE : on détache d'abord le speaker de ses événements (table event_speakers)
	@Transactional
	public Long supprimerSpeaker(Long id) {
		Speaker speaker = trouverSpeakerParId(id);
		for (Event event : speaker.getEvents()) {
			event.getSpeakers().remove(speaker);
			eventRepository.save(event);
		}
		speakerRepository.delete(speaker);
		return id;
	}
}
