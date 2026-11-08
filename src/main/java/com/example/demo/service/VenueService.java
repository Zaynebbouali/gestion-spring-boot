package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Venue;
import com.example.demo.repository.VenueRepository;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;

    public Venue ajouterVenue(Venue venue) {
        return venueRepository.save(venue);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue trouverVenueParId(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue non trouvée"));
    }

    public Venue modifierVenue(Long id, Venue venueDetails) {
        Venue venue = trouverVenueParId(id);

        venue.setName(venueDetails.getName());
        venue.setCity(venueDetails.getCity());
        venue.setCapacity(venueDetails.getCapacity());
        venue.setCategory(venueDetails.getCategory());

        return venueRepository.save(venue);
    }

    public Long supprimerVenue(Long id) {
        Venue venue = trouverVenueParId(id);
        venueRepository.delete(venue);
        return id;
    }

    public List<Venue> searchVenues(String city, Integer capacity) {
        return venueRepository.findByCityAndCapacityGreaterThanEqual(city, capacity);
    }
}