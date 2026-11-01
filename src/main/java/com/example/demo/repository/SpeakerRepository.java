package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {

}
