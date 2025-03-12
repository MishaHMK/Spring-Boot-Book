package book.project.bookstore.service;

import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
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
