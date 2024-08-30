package com.nagarro.repositorytest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nagarro.repository.BookRepository;
import com.nagarro.entity.Book;

import java.util.List;
import java.util.Optional;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testSaveBook() {
        Book book = new Book();
        book.setBookCode(1L);
        book.setBookName("Maths");
        book.setAuthor("RD Sharma");
        book.setDataAddedOn("2024-01-01");

        book = bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(book.getBookCode());
        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getBookName()).isEqualTo("Maths");
    }

    @Test
    public void testFindAllBooks() {
        Book book1 = new Book();
        book1.setBookCode(1L);
        book1.setBookName("Maths");
        book1.setAuthor("RD Sharma");
        book1.setDataAddedOn("2024-01-01");

        Book book2 = new Book();
        book2.setBookCode(2L);
        book2.setBookName("Physics");
        book2.setAuthor("HC Verma");
        book2.setDataAddedOn("2024-02-01");

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getBookName).contains("Maths", "Physics");
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book();
        book.setBookCode(1L);
        book.setBookName("Maths");
        book.setAuthor("RD Sharma");
        book.setDataAddedOn("2024-01-01");

        book = bookRepository.save(book);
        bookRepository.delete(book);

        Optional<Book> foundBook = bookRepository.findById(book.getBookCode());
        assertThat(foundBook.isPresent()).isFalse();
    }


}
