package com.nagarro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "author")
public class Author {

	@Id
	@Column(name = "author_name")
	private String authorName;

	//Default constructor
	public Author() {
		super();
	}

	//Parameterized Constructor
	public Author(String authorName) {
		super();
		this.authorName = authorName;
	}

	//Getter and Setter
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

}
