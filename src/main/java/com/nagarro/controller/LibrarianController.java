package com.nagarro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nagarro.entity.Librarian;
import com.nagarro.exception.ResourceNotFoundException;
import com.nagarro.repository.LibrarianRepository;

@RestController
@RequestMapping("/user")
public class LibrarianController {

	@Autowired
	private LibrarianRepository librarianRepository;

	@GetMapping
	public List<Librarian> getAllUsers() {
		return librarianRepository.findAll();
	}

	@PostMapping
	public Librarian addNewLibrarian(@RequestBody Librarian librarian) {
		return librarianRepository.save(librarian);
	}

	@GetMapping("/{username}")
	public ResponseEntity<Librarian> getLibrarianByUsername(@PathVariable String username) {
		Librarian librarian = librarianRepository.findById(username)
				.orElseThrow(() -> new ResourceNotFoundException("Librarian not exist with username: " + username));
		return ResponseEntity.ok(librarian);
	}

	@PostMapping("{username}")
	public ResponseEntity<Librarian> updateLibrarian(@PathVariable String username,
			@RequestBody Librarian librarianDetails) {
		Librarian updateLibrarian = librarianRepository.findById(username)
				.orElseThrow(() -> new ResourceNotFoundException("Librarian not exist with username: " + username));

		updateLibrarian.setPassword(librarianDetails.getPassword());

		librarianRepository.save(updateLibrarian);
		return ResponseEntity.ok(updateLibrarian);
	}

	@DeleteMapping("{username}")

	public ResponseEntity<Librarian> deleteLibrarianByUsername(@PathVariable String username) {
		Librarian librarian = librarianRepository.findById(username)
				.orElseThrow(() -> new ResourceNotFoundException("Librarian not exist with username: " + username));
		librarianRepository.delete(librarian);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}