package com.ankit.librarySystem.service;

import java.util.List;

import com.ankit.librarySystem.model.Book;

public interface BookServiceInterface  {

	public Book registerBook(Book book) ;
	
	public List<Book> getAllBooks();
	
}
