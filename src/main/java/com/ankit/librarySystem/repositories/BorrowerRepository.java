package com.ankit.librarySystem.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankit.librarySystem.model.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long>  {

	Optional<Borrower> findByEmail(String email);
}
