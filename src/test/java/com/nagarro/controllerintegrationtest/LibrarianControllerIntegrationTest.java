package com.nagarro.controllerintegrationtest;


import static org.junit.jupiter.api.Assertions.*;
import com.nagarro.entity.Librarian;
import com.nagarro.repository.LibrarianRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibrarianControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;

    @Autowired
    private LibrarianRepository librarianRepository;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl.concat(":").concat(port + "").concat("/user");
        //librarianRepository.deleteAll();
    }

    @Test
    public void testAddNewLibrarian() {
        Librarian librarian = new Librarian("john_doe", "password123");
        Librarian response = restTemplate.postForObject(baseUrl, librarian, Librarian.class);
        assertEquals("john_doe", response.getUsername());
        assertEquals(1, librarianRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO LIBRARIAN (username, password) VALUES ('jane_doe', 'password456')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM LIBRARIAN WHERE username='jane_doe'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetAllUsers() {
        List<Librarian> librarians = restTemplate.getForObject(baseUrl, List.class);
        System.out.println(librarians);
        assertEquals(2, librarians.size());
        assertEquals(2, librarianRepository.findAll().size());
    }

    @Test
    @Sql(statements = "INSERT INTO LIBRARIAN (username, password) VALUES ('john_smith', 'password789')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM LIBRARIAN WHERE username='john_smith'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetLibrarianByUsername() {
        Librarian librarian = restTemplate.getForObject(baseUrl + "/{username}", Librarian.class, "john_smith");
        
        assertAll(
                () -> assertNotNull(librarian),
                () -> assertEquals("john_smith", librarian.getUsername())
        );
    }

    @Test
    @Sql(statements = "INSERT INTO LIBRARIAN (username, password) VALUES ('alex_jones', 'password321')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM LIBRARIAN WHERE username='alex_jones'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testUpdateLibrarian() {
        Librarian librarian = new Librarian("alex_jones", "newpassword123");
        restTemplate.postForObject(baseUrl + "/{username}", librarian, Librarian.class, "alex_jones");
        Librarian updatedLibrarian = librarianRepository.findById("alex_jones").get();
        assertAll(
                () -> assertNotNull(updatedLibrarian),
                () -> assertEquals("newpassword123", updatedLibrarian.getPassword())
        );
    }
    
    @Test
    @Sql(statements = "INSERT INTO LIBRARIAN (username, password) VALUES ('mike_johnson', 'password654')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteLibrarianByUsername() {
    	List<Librarian> librarians = restTemplate.getForObject(baseUrl, List.class);
        System.out.println(librarians);
        int recordCount = librarianRepository.findAll().size();
        assertEquals(1, recordCount);
        restTemplate.delete(baseUrl + "/{username}", "mike_johnson");
        assertEquals(0, librarianRepository.findAll().size());
    }
}
