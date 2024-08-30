package com.nagarro.controllertest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.repository.LibrarianRepository;
import com.nagarro.controller.LibrarianController;
import com.nagarro.entity.Librarian;

@WebMvcTest(LibrarianController.class)
public class LibrarianControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibrarianRepository librarianRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Librarian librarian;

    @BeforeEach
    void setUp() {
        librarian = new Librarian();
        librarian.setUsername("testUser");
        librarian.setPassword("testPassword");
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<Librarian> librarians = Arrays.asList(librarian);
        when(librarianRepository.findAll()).thenReturn(librarians);

        mockMvc.perform(get("/user"))
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(librarians)));
    }

    @Test
    void testAddNewLibrarian() throws Exception {
        when(librarianRepository.save(any(Librarian.class))).thenReturn(librarian);

        mockMvc.perform(post("/user")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(librarian)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value(librarian.getUsername()));
    }

    @Test
    void testGetLibrarianByUsername() throws Exception {
        when(librarianRepository.findById("testUser")).thenReturn(Optional.of(librarian));

        mockMvc.perform(get("/user/testUser"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value(librarian.getUsername()));
    }

    @Test
    void testGetLibrarianByUsername_NotFound() throws Exception {
        when(librarianRepository.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/nonexistent"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateLibrarian() throws Exception {
        Librarian updatedLibrarian = new Librarian();
        updatedLibrarian.setUsername("testUser");
        updatedLibrarian.setPassword("newPassword");

        when(librarianRepository.findById("testUser")).thenReturn(Optional.of(librarian));
        when(librarianRepository.save(any(Librarian.class))).thenReturn(updatedLibrarian);

        mockMvc.perform(post("/user/testUser")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updatedLibrarian)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").value("newPassword"));
    }

    @Test
    void testDeleteLibrarianByUsername() throws Exception {
        when(librarianRepository.findById("testUser")).thenReturn(Optional.of(librarian));

        mockMvc.perform(delete("/user/testUser"))
               .andExpect(status().isNoContent());

        verify(librarianRepository, times(1)).delete(librarian);
    }

    @Test
    void testDeleteLibrarianByUsername_NotFound() throws Exception {
        when(librarianRepository.findById("nonexistent")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/user/nonexistent"))
               .andExpect(status().isNotFound());
    }
}
