package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Venue;
import com.example.demo.repository.VenueRepository;

@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    // CREATE
    public Venue createVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    // READ ALL
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    // READ BY ID
    public Venue getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));
    }

    public Venue updateVenue(Long id, Venue venueDetails) {

        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));

        venue.setName(venueDetails.getName());
        venue.setCity(venueDetails.getCity());
        venue.setCapacity(venueDetails.getCapacity());
        venue.setCategory(venueDetails.getCategory());

        return venueRepository.save(venue);
    }
    // DELETE
    public Long deleteVenue(Long id) {

        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found"));

        venueRepository.delete(venue);

        return id;
    }

    // RECHERCHE
    public List<Venue> searchVenues(String city, Integer capacity) {
        return venueRepository.findByCityAndCapacityGreaterThanEqual(city, capacity);
    }
}