package com.edstem.fileUploadStorage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIO(IOException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IO Error: " + ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body("Validation Error: " + ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneral(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
	}
}
