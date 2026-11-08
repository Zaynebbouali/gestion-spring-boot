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
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Speaker;
import com.example.demo.service.SpeakerService;

@RestController
@RequestMapping("/api/speakers")
public class SpeakerController {

	@Autowired
	private SpeakerService speakerService;

	@PostMapping
	public Speaker ajouterSpeaker(@RequestBody Speaker speaker) {
		return speakerService.creerSpeaker(speaker);
	}

	@GetMapping
	public List<Speaker> listerSpeakers() {
		return speakerService.listerSpeakers();
	}

	@GetMapping("/{id}")
	public Speaker afficherSpeaker(@PathVariable Long id) {
		return speakerService.trouverSpeakerParId(id);
	}

	@PutMapping("/{id}")
	public Speaker modifierSpeaker(@PathVariable Long id, @RequestBody Speaker speaker) {
		return speakerService.mettreAJourSpeaker(id, speaker);
	}

	@DeleteMapping("/{id}")
	public Long supprimerSpeaker(@PathVariable Long id) {
		return speakerService.supprimerSpeaker(id);
	}
}