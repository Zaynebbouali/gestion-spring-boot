package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.Entity.Venue;
import com.example.demo.service.VenueService;

@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // AJOUTER
    @PostMapping
    public Venue ajouterVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }

    // LISTER
    @GetMapping
    public List<Venue> listerVenues() {
        return venueService.getAllVenues();
    }

    // AFFICHER UNE SALLE
    @GetMapping("/{id}")
    public Venue afficherVenue(@PathVariable Long id) {
        return venueService.getVenueById(id);
    }

    // MODIFIER
    @PutMapping("/{id}")
    public Venue modifierVenue(@PathVariable Long id, @RequestBody Venue venueDetails) {
        return venueService.updateVenue(id, venueDetails);
    }

    // SUPPRIMER
    @DeleteMapping("/{id}")
    public Long supprimerVenue(@PathVariable Long id) {
        return venueService.deleteVenue(id);
    }

    // RECHERCHER PAR VILLE ET CAPACITE MINIMALE
    @GetMapping("/search")
    public List<Venue> rechercherVenues(
            @RequestParam String city,
            @RequestParam Integer capacity) {
        return venueService.searchVenues(city, capacity);
    }
}