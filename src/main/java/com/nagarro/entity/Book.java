package com.nagarro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class Book {

	@Id
	@Column(name = "book_code")
	private long bookCode;
	
	@Column(name = "book_name")
	private String bookName;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "data_added_on")
	private String dataAddedOn;

	
	//Default constructor
	public Book() {
		super();
	}

	//Parameterized Constructor
	public Book(long bookCode, String bookName, String author, String dataAddedOn) {
		super();
		this.bookCode = bookCode;
		this.bookName = bookName;
		this.author = author;
		this.dataAddedOn = dataAddedOn;
	}

	//Getters and Setters
	public long getBookCode() {
		return bookCode;
	}

	public void setBookCode(long bookCode) {
		this.bookCode = bookCode;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDataAddedOn() {
		return dataAddedOn;
	}

	public void setDataAddedOn(String dataAddedOn) {
		this.dataAddedOn = dataAddedOn;
	}

	@Override
	public String toString() {
		return "Book [bookCode=" + bookCode + ", bookName=" + bookName + ", author=" + author + ", dataAddedOn="
				+ dataAddedOn + "]";
	}
	
}
