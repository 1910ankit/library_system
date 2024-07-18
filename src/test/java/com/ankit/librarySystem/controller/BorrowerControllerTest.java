package com.ankit.librarySystem.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;
import com.ankit.librarySystem.payload.RegisterBorrowerDTO;
import com.ankit.librarySystem.payload.UpdateBookDTO;
import com.ankit.librarySystem.service.BorrowerService;
import com.ankit.librarySystem.util.BorrowerSuccess;
import com.ankitkumar.library.exception.BookUnavailableException;
import com.ankitkumar.library.exception.BorrowerNotFoundException;

public class BorrowerControllerTest {
	

    @InjectMocks
    private BorrowerController borrowerController;

    @Mock
    private BorrowerService borrowerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterBorrowerSuccess() {
        RegisterBorrowerDTO registerBorrowerDTO = new RegisterBorrowerDTO(null, null);
        registerBorrowerDTO.setEmail("test@example.com");
        registerBorrowerDTO.setName("Test User");

        Borrower borrower = new Borrower();
        borrower.setEmail("test@example.com");
        borrower.setName("Test User");

        when(borrowerService.registerBorrower(any(Borrower.class))).thenReturn(borrower);

        ResponseEntity<String> response = borrowerController.registerBorrower(registerBorrowerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(BorrowerSuccess.BORROWER_REGISTERED_SUCCESS.toString(), response.getBody());

        verify(borrowerService, times(1)).registerBorrower(any(Borrower.class));
    }

    @Test
    public void testRegisterBorrowerFailure() {
        RegisterBorrowerDTO registerBorrowerDTO = new RegisterBorrowerDTO(null, null);
        registerBorrowerDTO.setEmail("test@example.com");
        registerBorrowerDTO.setName("Test User");

        when(borrowerService.registerBorrower(any(Borrower.class))).thenThrow(new RuntimeException());

        ResponseEntity<String> response = borrowerController.registerBorrower(registerBorrowerDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testBorrowBookSuccess() {
        long bookId = 1L;
        long borrowerId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setIsbn("123456789");
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setAvailable(false);

        Borrower borrower = new Borrower();
        borrower.setId(borrowerId);
        borrower.setName("Test User");

        book.setBorrower(borrower);

        when(borrowerService.borrowBook(bookId, borrowerId)).thenReturn(book);

        ResponseEntity<UpdateBookDTO> response = borrowerController.borrow(bookId, borrowerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UpdateBookDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(bookId, responseBody.getBookId());
        assertEquals("123456789", responseBody.getIsbn());
        assertEquals("Title", responseBody.getTitle());
        assertEquals("Author", responseBody.getAuthor());
        assertFalse(responseBody.isAvailable());
        assertEquals(borrowerId, responseBody.getBorrowerId());
        assertEquals("Test User", responseBody.getBorrowerName());

        verify(borrowerService, times(1)).borrowBook(bookId, borrowerId);
    }

    @Test
    public void testReturnBookSuccess() {
        long bookId = 1L;

        Book book = new Book();
        book.setId(bookId);
        book.setIsbn("123456789");
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setAvailable(true);

        when(borrowerService.returnBook(bookId)).thenReturn(book);

        ResponseEntity<UpdateBookDTO> response = borrowerController.returnBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UpdateBookDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(bookId, responseBody.getBookId());
        assertEquals("123456789", responseBody.getIsbn());
        assertEquals("Title", responseBody.getTitle());
        assertEquals("Author", responseBody.getAuthor());
        assertTrue(responseBody.isAvailable());
        assertEquals(0L, responseBody.getBorrowerId());
        assertNull(responseBody.getBorrowerName());

        verify(borrowerService, times(1)).returnBook(bookId);
    }

   
}
