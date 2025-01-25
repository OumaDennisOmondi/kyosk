package com.kyosk.backend.service;

import com.kyosk.backend.model.Book;
import com.kyosk.backend.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(
                new Book("Test Book 1", "Author 1", "ISBN1", 2021),
                new Book("Test Book 2", "Author 2", "ISBN2", 2022));
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookService.getAllBooks();

        // Assert
        assertNotNull(actualBooks);
        assertEquals(expectedBooks.size(), actualBooks.size());
        assertEquals(expectedBooks.get(0).getTitle(), actualBooks.get(0).getTitle());
        assertEquals(expectedBooks.get(1).getTitle(), actualBooks.get(1).getTitle());
        verify(bookRepository).findAll();
    }
}