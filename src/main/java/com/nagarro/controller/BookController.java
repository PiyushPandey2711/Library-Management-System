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

import com.nagarro.entity.Book;
import com.nagarro.exception.ResourceNotFoundException;
import com.nagarro.repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	
	@GetMapping
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	@PostMapping
	public Book addNewBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}

	@GetMapping("{bookCode}")
	public ResponseEntity<Book> getBookByBookCode(@PathVariable Long bookCode){
		Book book = bookRepository.findById(bookCode)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookCode));
		return ResponseEntity.ok(book);
	}

	
	@PostMapping("{bookCode}")
	public ResponseEntity<Book> updateBook(@PathVariable Long bookCode, @RequestBody Book bookDetails) {
		Book updateBook = bookRepository.findById(bookCode)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookCode));

		updateBook.setBookName(bookDetails.getBookName());
		updateBook.setAuthor(bookDetails.getAuthor());
		updateBook.setDataAddedOn(bookDetails.getDataAddedOn());

		bookRepository.save(updateBook);
		return ResponseEntity.ok(updateBook);
	}

	@DeleteMapping("{bookCode}")
	public ResponseEntity<Book> deleteBookByBookCode(@PathVariable Long bookCode) {
		Book book = bookRepository.findById(bookCode)
				.orElseThrow(() -> new ResourceNotFoundException("Book not found:  " + bookCode));
		bookRepository.delete(book);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
