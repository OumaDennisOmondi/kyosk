package com.kyosk.backend.controller;

import com.kyosk.backend.model.Book;
import com.kyosk.backend.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void healthCheck_ShouldReturnHealthy() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is healthy"));
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        // Arrange
        List<Book> books = Arrays.asList(
                new Book("Test Book 1", "Author 1", "ISBN1", 2021),
                new Book("Test Book 2", "Author 2", "ISBN2", 2022));
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Book 1"))
                .andExpect(jsonPath("$[0].author").value("Author 1"))
                .andExpect(jsonPath("$[1].title").value("Test Book 2"))
                .andExpect(jsonPath("$[1].author").value("Author 2"));

        verify(bookService).getAllBooks();
    }
}