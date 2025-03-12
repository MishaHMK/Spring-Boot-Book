package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
import book.project.bookstore.repository.book.BookSearchParameters;
import book.project.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books",
            description = "Get all books with possible pagination/sorting")
    public List<BookDto> getAll(@ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id",
            description = "Get specific book by given unique id")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create book",
            description = "Create new book with given book data")
    public BookDto createBook(@Valid @RequestBody CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book by id",
            description = "Get specific book by given id and new book data")
    public BookDto updateBookById(@PathVariable Long id,
                                  @Valid @RequestBody UpdateBookRequestDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book by id",
            description = "Mark specific book by given id as removed")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by parameters",
            description = "Get books which correspond with given parameters: "
                    + "titles, authors and pagination/sorting options")
    public List<BookDto> search(@ParameterObject BookSearchParameters searchParameters,
                                @ParameterObject Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
