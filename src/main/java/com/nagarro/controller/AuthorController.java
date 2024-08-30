package com.nagarro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.entity.Author;
import com.nagarro.exception.ResourceNotFoundException;
import com.nagarro.repository.AuthorRepository;


@RestController
@RequestMapping("/author")
public class AuthorController {

	@Autowired
	AuthorRepository authorRepository;

	
	@GetMapping
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	@GetMapping("/{authorname}")
	public ResponseEntity<Author> getAuthorByAuthorname(@PathVariable String authorname) {
		Author author = authorRepository.findById(authorname)
				.orElseThrow(() -> new ResourceNotFoundException("Author not exist by name: " + authorname));
		return ResponseEntity.ok(author);
	}

	@PostMapping
	public Author addNewAuthor(@RequestBody Author author) {
		return authorRepository.save(author);
	}
}
