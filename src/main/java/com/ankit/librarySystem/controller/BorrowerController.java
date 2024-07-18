package com.ankit.librarySystem.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;
import com.ankit.librarySystem.payload.ErrorResponse;
import com.ankit.librarySystem.payload.RegisterBorrowerDTO;
import com.ankit.librarySystem.payload.UpdateBookDTO;
import com.ankit.librarySystem.service.BorrowerService;
import com.ankit.librarySystem.util.BorrowerSuccess;
import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;
import com.ankitkumar.library.exception.DuplicateEmailException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {
	
	@Autowired
	BorrowerService borrowerService;
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Internal server error occurred. Please try again later.");
	}

	@ExceptionHandler(value = { DuplicateEmailException.class, IllegalArgumentException.class,
			BookUnavailableException.class, BorrowerNotFoundException.class })
	protected ResponseEntity<UpdateBookDTO> handleConflict(RuntimeException ex, WebRequest request) {
		//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
		// ErrorResponse errorResponse = new ErrorResponse("Error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UpdateBookDTO(null, null, null, null, false, 0, null, ex.getMessage()));
	}
	
	@PostMapping(value ="/registerBorrower",produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponse(responseCode = "200", description = "Borrower added")
	@Operation(summary = "Add a new User")
	public ResponseEntity<String> registerBorrower(@RequestBody RegisterBorrowerDTO borrowerDTO) {
		try {
			Borrower borrower = new Borrower();
			borrower.setEmail(borrowerDTO.getEmail());
			borrower.setName(borrowerDTO.getName());
			borrower = borrowerService.registerBorrower(borrower);
			return ResponseEntity.ok(BorrowerSuccess.BORROWER_REGISTERED_SUCCESS.toString());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	
	@PutMapping(value = "/{bookId}/borrow/{borrowerId}")
	@ApiResponse(responseCode = "200", description = "Book borrowed successfully")
	@Operation(summary = "Borrow a book")
	public ResponseEntity<UpdateBookDTO> borrow(@PathVariable long bookId ,@PathVariable long borrowerId ) {
		try {
			Book book = borrowerService.borrowBook(bookId, borrowerId);
			UpdateBookDTO books = new UpdateBookDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.isAvailable(),
					Optional.ofNullable(book.getBorrower()).map(Borrower::getId).orElse(0L) ,
					Optional.ofNullable(book.getBorrower()).map(Borrower::getName).orElse(null),
					null);
			return ResponseEntity.ok(books);
		} catch (RuntimeException e) {
			return handleConflict(e, null);
			//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PutMapping(value = "/{bookId}/return")
	@ApiResponse(responseCode = "200", description = "Book returned successfully")
	@Operation(summary = "Return a book")
	public ResponseEntity<UpdateBookDTO> returnBook(@PathVariable long bookId) {
		try {
			Book book = borrowerService.returnBook(bookId);
			UpdateBookDTO books = new UpdateBookDTO(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(), book.isAvailable(),
					Optional.ofNullable(book.getBorrower()).map(Borrower::getId).orElse(0L) ,
					Optional.ofNullable(book.getBorrower()).map(Borrower::getName).orElse(null),
					null);
			return ResponseEntity.ok(books);
		} catch (RuntimeException e) {
			return handleConflict(e, null);
			//return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
