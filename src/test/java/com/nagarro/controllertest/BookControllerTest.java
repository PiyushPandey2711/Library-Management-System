package com.nagarro.controllertest;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.controller.BookController;
import com.nagarro.repository.BookRepository;
import com.nagarro.entity.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void getAllBooksTest() throws Exception {
        Book book1 = new Book(1L, "Maths", "RD Sharma", "2024-01-01");
        Book book2 = new Book(2L, "Physics", "HC Verma", "2024-02-01");

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookName").value("Maths"))
                .andExpect(jsonPath("$[1].bookName").value("Physics"));
    }

     @Test
    public void addNewBookTest() throws Exception {
        Book book = new Book(1L, "Chemistry", "Nagarjuna", "2024-03-03");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("Chemistry"));
    }


    @Test
    public void deleteBookByBookCodeTest() throws Exception {
        Book book = new Book(1L, "C++", "George", "2024-04-04");

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        verify(bookRepository, times(1)).delete(book);
    }
}
