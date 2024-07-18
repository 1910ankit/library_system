package com.ankit.librarySystem.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterBookDTO {
	
	private String isbn;
    private String title;
    private String author;
    
}
