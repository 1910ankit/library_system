package com.ankit.librarySystem.payload;

import com.ankit.librarySystem.model.Borrower;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateBookDTO {
	@JsonProperty("bookId")
	private Long bookId;
	@JsonProperty("isbn")
    private String isbn;
	@JsonProperty("title")
    private String title;
	@JsonProperty("author")
    private String author;
	@JsonProperty("available")
    private boolean available;
	@JsonProperty("borrowerId")
    private long borrowerId;
	@JsonProperty("borrowerName")
    private String borrowerName;
	
	private String error;
}
