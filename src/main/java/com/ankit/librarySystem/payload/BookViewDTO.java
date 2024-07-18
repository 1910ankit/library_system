package com.ankit.librarySystem.payload;

import com.ankit.librarySystem.model.Borrower;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookViewDTO {


	private Long id;
    private String isbn;
    private String title;
    private String author;
    private boolean available;
    private long borrowerId;
    private String borrowerName;
   // private Borrower borrower;
}
