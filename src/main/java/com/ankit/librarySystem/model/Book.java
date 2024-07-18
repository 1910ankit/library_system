package com.ankit.librarySystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private boolean available;

    @ManyToOne
    @JoinColumn(columnDefinition = "borrower_id",referencedColumnName = "id",nullable = true)
    private Borrower borrower;
}
