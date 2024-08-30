package com.nagarro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nagarro.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	//Contains all the CRUD Database methods
}
