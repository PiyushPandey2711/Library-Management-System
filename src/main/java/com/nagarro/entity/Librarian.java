package com.nagarro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "librarian")
public class Librarian {

	@Id
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;

	//Default Constructor
	public Librarian() {
		super();
	}
	
	//Parameterized Constructor
	public Librarian(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	//Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
