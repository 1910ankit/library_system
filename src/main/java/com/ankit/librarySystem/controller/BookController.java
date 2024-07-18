package com.ankit.librarySystem.controller;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;
import com.ankit.librarySystem.payload.BookViewDTO;
import com.ankit.librarySystem.payload.RegisterBookDTO;
import com.ankit.librarySystem.service.BookService;
import com.ankit.librarySystem.util.BookSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Controller", description = "Controller for Book management")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping(value = "/registerBook", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponse(responseCode = "200", description = "Book added")
	@Operation(summary = "Add a new Book")
	public ResponseEntity<String> registerBook(@RequestBody RegisterBookDTO bookDTO) {
		try {
			Book book = new Book();
			book.setAuthor(bookDTO.getAuthor());
			book.setIsbn(bookDTO.getIsbn());
			book.setTitle(bookDTO.getTitle());
			book = bookService.registerBook(book);
			return ResponseEntity.ok(BookSuccess.BOOK_REGISTERED_SUCCESS.toString());

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping(value = "/books", produces = "application/json")
	@ApiResponse(responseCode = "200", description = "List of Books")
	public List<BookViewDTO> getAllAvailableBooks() {
		//List<Book> book = bookService.getAllBooks();
		
		List<BookViewDTO> books = new ArrayList<>();
		List<Book> allBooks = bookService.getAllBooks();
		for(Book book : allBooks ) {
			books.add(new BookViewDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(),book.isAvailable(),
					Optional.ofNullable(book.getBorrower()).map(Borrower::getId).orElse(0L) ,
					Optional.ofNullable(book.getBorrower()).map(Borrower::getName).orElse(null)));
		}
		
		return books;
	}
}
