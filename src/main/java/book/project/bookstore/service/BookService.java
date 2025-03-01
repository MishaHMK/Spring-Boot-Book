package book.project.bookstore.service;

import book.project.bookstore.dto.BookDto;
import book.project.bookstore.dto.CreateBookRequestDto;
import book.project.bookstore.dto.UpdateBookRequestDto;
import book.project.bookstore.repository.book.BookSearchParameters;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, UpdateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
