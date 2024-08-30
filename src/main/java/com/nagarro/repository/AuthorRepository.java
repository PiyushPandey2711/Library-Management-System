package com.nagarro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nagarro.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, String> {

}
