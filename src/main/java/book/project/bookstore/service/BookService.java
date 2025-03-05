package book.project.bookstore.service;

import book.project.bookstore.dto.BookDto;
import book.project.bookstore.dto.CreateBookRequestDto;
import book.project.bookstore.dto.UpdateBookRequestDto;
import book.project.bookstore.repository.book.BookSearchParameters;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, UpdateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);
}
