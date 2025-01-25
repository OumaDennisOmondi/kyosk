package com.kyosk.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.kyosk.backend.repository.BookRepository;
import com.kyosk.backend.model.Book;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(BookRepository bookRepository) {
        return args -> {
            if (bookRepository.count() == 0) {
                Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0743273565", 1925);
                bookRepository.save(book1);

                Book book2 = new Book("To Kill a Mockingbird", "Harper Lee", "978-0446310789", 1960);
                bookRepository.save(book2);

                Book book3 = new Book("1984", "George Orwell", "978-0451524935", 1949);
                bookRepository.save(book3);

                System.out.println("Sample books have been initialized");
            }
        };
    }
}