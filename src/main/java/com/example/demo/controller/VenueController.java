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

    // CREATE
    @PostMapping
    public Venue createVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }

    // READ ALL
    @GetMapping
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Venue getVenueById(@PathVariable Long id) {
        return venueService.getVenueById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Venue updateVenue(@PathVariable Long id, @RequestBody Venue venueDetails) {
        return venueService.updateVenue(id, venueDetails);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public Long deleteVenue(@PathVariable Long id) {
        return venueService.deleteVenue(id);
    }

    // SEARCH BY CITY AND MINIMUM CAPACITY
    @GetMapping("/search")
    public List<Venue> searchVenues(
            @RequestParam String city,
            @RequestParam Integer capacity) {
        return venueService.searchVenues(city, capacity);
    }
}