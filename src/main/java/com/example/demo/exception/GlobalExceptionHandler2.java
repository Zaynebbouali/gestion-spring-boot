package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler2 {

	// Ressource introuvable -> 404
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgument(
	        IllegalArgumentException ex) {

	    return new ResponseEntity<>(
	        ex.getMessage(),
	        HttpStatus.NOT_FOUND
	    );
	}

	// Règle métier non respectée -> 400
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleIllegalState(
	        IllegalStateException ex) {

	    return new ResponseEntity<>(
	        ex.getMessage(),
	        HttpStatus.BAD_REQUEST
	    );
	}
}
