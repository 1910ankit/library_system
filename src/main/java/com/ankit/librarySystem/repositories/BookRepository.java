package com.ankit.librarySystem.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankit.librarySystem.model.Book;

public interface BookRepository extends JpaRepository<Book, Long > {
	
	Optional<Book> findByAvailableTrue();
	
	Optional<Book> findByIsbn(String isbn);
	

}
