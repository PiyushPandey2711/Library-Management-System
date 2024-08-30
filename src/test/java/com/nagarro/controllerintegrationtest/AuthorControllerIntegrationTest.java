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

import com.nagarro.repository.AuthorRepository;
import com.nagarro.entity.Author;

import org.springframework.http.ResponseEntity;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/author");
        
    }

    @Test
    public void testAddNewAuthor() {
        Author author = new Author("J.K. Rowling");
        Author response = restTemplate.postForObject(baseUrl, author, Author.class);
        assertEquals("J.K. Rowling", response.getAuthorName());
        assertEquals(1, authorRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO AUTHOR (author_name) VALUES ('George Orwell')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM AUTHOR WHERE author_name='George Orwell'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllAuthors() {
        List<Author> authors = restTemplate.getForObject(baseUrl, List.class);
        System.out.println(authors);
        assertEquals(1, authors.size());
        assertEquals(1, authorRepository.findAll().size());
    }
    
    @Test
    @Sql(statements = "INSERT INTO AUTHOR (author_name) VALUES ('john_smith')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM AUTHOR WHERE author_name='john_smith'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAuthorByAuthorname() {
        Author author = restTemplate.getForObject(baseUrl + "/{author_name}", Author.class, "john_smith");
        assertAll(
                () -> assertNotNull(author),
                () -> assertEquals("john_smith", author.getAuthorName())
        );
    }
}
