package com.ankit.librarySystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.repositories.BookRepository;
import com.ankitkumar.library.exception.BookRegistrationException;
import com.ankitkumar.library.exception.InconsistentBookException;


@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public Optional<Book> isAvailableBooks(){
		
		return bookRepository.findByAvailableTrue();
	} 
	
	public Book registerBook(Book book) {

		try {
			validateBookData(book);
			Optional<Book> existingBooks = bookRepository.findByIsbn(book.getIsbn());   
			{
				// Ensure consistency for books with the same ISBN
				boolean isConsistent = existingBooks.stream()
						.allMatch(existingBook -> existingBook.getTitle().equals(book.getTitle())
								&& existingBook.getAuthor().equals(book.getAuthor()));
				if (!isConsistent) {
					throw new InconsistentBookException("Books with the same ISBN must have the same title and author");
				}
			}
			return bookRepository.save(book);
		} 
		catch(IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		catch(InconsistentBookException e) {
			throw new InconsistentBookException("Books with the same ISBN must have the same title and author");
		}
		catch (Exception e) {
			throw new BookRegistrationException("Failed to register book", e);
		}
		
	}
	
	public List<Book> getAllBooks() {
		try {
			return bookRepository.findAll();
		} catch (Exception e) {
			throw new RuntimeException(e.getCause());
		}
	}
	
	// Validate book data
		private void validateBookData(Book book) {
			if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
				throw new IllegalArgumentException("ISBN is required");
			}
			if (book.getTitle() == null || book.getTitle().isEmpty()) {
				throw new IllegalArgumentException("Title is required");
			}
			if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
				throw new IllegalArgumentException("Author is required");
			}
		}
	
}
