package com.nagarro.controllerintegrationtest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import com.nagarro.repository.BookRepository;
import com.nagarro.entity.Book;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/books");
        
    }

    @Test
    public void testAddNewBook() {
        Book book = new Book(1L, "Book Name", "Author Name", "2023-01-01");
        Book response = restTemplate.postForObject(baseUrl, book, Book.class);
        assertEquals("Book Name", response.getBookName());
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO book (book_code, book_name, author, data_added_on) VALUES (2, 'Existing Book', 'Existing Author', '2023-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE book_code=2", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllBooks() {
        List<Book> books = restTemplate.getForObject(baseUrl, List.class);
        assertEquals(2, books.size());
        assertEquals(2, bookRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO book (book_code, book_name, author, data_added_on) VALUES (1, 'Existing Book', 'Existing Author', '2023-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE book_code=1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetBookByBookCode() {
        Book book = restTemplate.getForObject(baseUrl + "/{bookCode}", Book.class, 1L);
        assertAll(
                () -> assertNotNull(book),
                () -> assertEquals("Existing Book", book.getBookName())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO book (book_code, book_name, author, data_added_on) VALUES (3, 'Old Book', 'Old Author', '2023-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM book WHERE book_code=1", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateBook() {
        Book updatedBookDetails = new Book(3L, "Updated Book", "Updated Author", "2024-01-01");
        restTemplate.postForObject(baseUrl + "/{bookCode}", updatedBookDetails, Book.class, 1L);
        List<Book> books = restTemplate.getForObject(baseUrl, List.class);
        System.out.print(books);
        Book updatedBook = bookRepository.findById(1L).get();
        assertAll(
                () -> assertNotNull(updatedBook),
                () -> assertEquals("Updated Book", updatedBook.getBookName()),
                () -> assertEquals("Updated Author", updatedBook.getAuthor())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO book (book_code, book_name, author, data_added_on) VALUES (1, 'To Be Deleted', 'Author', '2023-01-01')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteBookByBookCode() {
        int initialCount = bookRepository.findAll().size();
        assertEquals(2, initialCount);
        restTemplate.delete(baseUrl + "/{bookCode}", 1L);
        assertEquals(1, bookRepository.findAll().size());
    }
    }
