package com.ankit.librarySystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;
import com.ankit.librarySystem.payload.BookViewDTO;
import com.ankit.librarySystem.payload.RegisterBookDTO;
import com.ankit.librarySystem.service.BookService;
import com.ankit.librarySystem.util.BookSuccess;

public class BookControllerTest {

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookService bookService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testRegisterBookSuccess() {
		RegisterBookDTO registerBookDTO = new RegisterBookDTO(null, null, null);
		registerBookDTO.setAuthor("Author");
		registerBookDTO.setIsbn("123456789");
		registerBookDTO.setTitle("Title");

		Book book = new Book();
		book.setAuthor("Author");
		book.setIsbn("123456789");
		book.setTitle("Title");

		when(bookService.registerBook(any(Book.class))).thenReturn(book);

		ResponseEntity<String> response = bookController.registerBook(registerBookDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(BookSuccess.BOOK_REGISTERED_SUCCESS.toString(), response.getBody());
	}

	@Test
	public void testRegisterBookFailure() {
		RegisterBookDTO registerBookDTO = new RegisterBookDTO(null, null, null);
		registerBookDTO.setAuthor("Author");
		registerBookDTO.setIsbn("123456789");
		registerBookDTO.setTitle("Title");

		when(bookService.registerBook(any(Book.class))).thenThrow(new RuntimeException());

		ResponseEntity<String> response = bookController.registerBook(registerBookDTO);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetAllAvailableBooks() {
		List<Book> books = new ArrayList<>();
		Book book1 = new Book();
		book1.setId(1L);
		book1.setAuthor("Author 1");
		book1.setIsbn("123456789");
		book1.setTitle("Title 1");
		book1.setAvailable(true);

		Book book2 = new Book();
		book2.setId(2L);
		book2.setAuthor("Author 2");
		book2.setIsbn("987654321");
		book2.setTitle("Title 2");
		book2.setAvailable(false);

		Borrower borrower = new Borrower();
		borrower.setId(1L);
		borrower.setName("Borrower 1");
		book2.setBorrower(borrower);

		books.add(book1);
		books.add(book2);

		when(bookService.getAllBooks()).thenReturn(books);

		List<BookViewDTO> bookViewDTOs = bookController.getAllAvailableBooks();

		assertEquals(2, bookViewDTOs.size());

		BookViewDTO bookViewDTO1 = bookViewDTOs.get(0);
		assertEquals(1L, bookViewDTO1.getId());
		assertEquals("123456789", bookViewDTO1.getIsbn());
		assertEquals("Title 1", bookViewDTO1.getTitle());
		assertEquals("Author 1", bookViewDTO1.getAuthor());
		assertTrue(bookViewDTO1.isAvailable());
		assertEquals(0L, bookViewDTO1.getBorrowerId());
		assertNull(bookViewDTO1.getBorrowerName());

		BookViewDTO bookViewDTO2 = bookViewDTOs.get(1);
		assertEquals(2L, bookViewDTO2.getId());
		assertEquals("987654321", bookViewDTO2.getIsbn());
		assertEquals("Title 2", bookViewDTO2.getTitle());
		assertEquals("Author 2", bookViewDTO2.getAuthor());
		assertFalse(bookViewDTO2.isAvailable());
		assertEquals(1L, bookViewDTO2.getBorrowerId());
		assertEquals("Borrower 1", bookViewDTO2.getBorrowerName());
	}
}
