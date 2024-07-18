package com.ankit.librarySystem.service;

import com.ankit.librarySystem.model.Book;
import com.ankit.librarySystem.model.Borrower;

public interface BorrowerServiceInterface {

	 public Borrower registerBorrower(Borrower borrower);
	 
	 public Book borrowBook(Long bookId, Long borrowerId) ;
	 
	 public Book returnBook(Long bookId) ;
}
