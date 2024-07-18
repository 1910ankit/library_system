package com.ankit.librarySystem.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;
import com.ankit.librarySystem.repositories.BookRepository;
import com.ankit.librarySystem.repositories.BorrowerRepository;
import com.ankitkumar.library.exception.BookNotFoundException;
import com.ankitkumar.library.exception.BookReturnException;
import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;
import com.ankitkumar.library.exception.BorrowerRegistrationException;
import com.ankitkumar.library.exception.DuplicateEmailException;

@Service
public class BorrowerService implements BorrowerServiceInterface {

	@Autowired
	private BorrowerRepository borrowerRepository;

	@Autowired
	BookRepository bookRepository;

	public Borrower registerBorrower(Borrower borrower) {
		try {
			validateBorrowerData(borrower);
			Optional<Borrower> existingBorrower = borrowerRepository.findByEmail(borrower.getEmail());
			if (existingBorrower.isPresent()) {
				throw new DuplicateEmailException("Email already exists");
			}
		} catch (DuplicateEmailException e) {
			throw new DuplicateEmailException("Email already exists");
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		} catch (Exception e) {
			throw new BorrowerRegistrationException("Failed to register borrower", e);
		}
		return borrowerRepository.save(borrower);
	}

	// Validate book data
	private void validateBorrowerData(Borrower borrower) {
		if (borrower.getEmail() == null || borrower.getEmail().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		} else if (!isValidEmail(borrower.getEmail())) {
			throw new IllegalArgumentException("Invalid email format");
		}
		if (borrower.getName() == null || borrower.getName().isEmpty()) {
			throw new IllegalArgumentException("Name is required");
		}
	}

	// Check if email is valid using regex
	private boolean isValidEmail(String email) {
		// Regular expression for email validation
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		// Compile the regex pattern
		Pattern pattern = Pattern.compile(emailRegex);
		// Validate email against the regex pattern
		return pattern.matcher(email).matches();
	}

	public Book borrowBook(Long bookId, Long borrowerId) {
		Borrower borrower = borrowerRepository.findById(borrowerId)
				.orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with ID :" + borrowerId));
		try {
			return bookRepository.findById(bookId).map((book -> {
				if (book.isAvailable()) {
					book.setBorrower(borrower);
					book.setAvailable(false);
					return bookRepository.save(book);
				} else {
					throw new BookUnavailableException("Book is not available for borrowing");
				}
			})).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
		} catch (BorrowerNotFoundException e) {
			throw new BorrowerNotFoundException(e.getMessage());
		} catch (BookUnavailableException e) {
			throw new BookUnavailableException(e.getMessage());
		} catch (BookNotFoundException e) {
			throw new BookNotFoundException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to borrow book", e);
		}
	}

	public Book returnBook(Long bookId) {
		// Try to return the book
		try {
			return bookRepository.findById(bookId).map(book -> {
				if (!book.isAvailable()) {
					book.setBorrower(null);
					book.setAvailable(true);
					return bookRepository.save(book);
				} else {
					throw new BookReturnException("Book is already available");
				}
			}).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + bookId));
		} catch (BookReturnException e) {
			throw new BookReturnException(e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Failed to return book", e);
		}
	}
}
