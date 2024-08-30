package com.nagarro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.entity.Librarian;

public interface LibrarianRepository extends JpaRepository<Librarian, String>{
	
	//Contains all the CRUD Database methods

}
