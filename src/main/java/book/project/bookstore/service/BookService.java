package book.project.bookstore.service;

import book.project.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
