package com.nagarro.controllertest;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.controller.AuthorController;
import com.nagarro.entity.Author;
import com.nagarro.exception.ResourceNotFoundException;
import com.nagarro.repository.AuthorRepository;

@WebMvcTest(AuthorController.class)
@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorController authorController;

    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {
        author1 = new Author("Rs Agarwal");
        author2 = new Author("Rd Sharma");

        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(new ResourceNotFoundException("Not done"))
                .build();
    }

    @Test
    public void testGetAllAuthors() throws Exception {
        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        mockMvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorName", is("Rs Agarwal")))
                .andExpect(jsonPath("$[1].authorName", is("Rd Sharma")));
    }

    @Test
    public void testGetAuthorByAuthorname() throws Exception {
        when(authorRepository.findById("Rs Agarwal")).thenReturn(Optional.of(author1));

        mockMvc.perform(get("/author/Rs Agarwal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName", is("Rs Agarwal")));
    }

    @Test
    public void testGetAuthorByAuthornameNotFound() throws Exception {
        when(authorRepository.findById("Deepak Kumar")).thenReturn(Optional.empty());

        mockMvc.perform(get("/author/Deepak Kumar"))
                .andExpect(status().isNotFound());
    }

}
