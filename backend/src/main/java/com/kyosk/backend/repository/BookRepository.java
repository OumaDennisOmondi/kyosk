package com.kyosk.backend.repository;

import com.kyosk.backend.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}