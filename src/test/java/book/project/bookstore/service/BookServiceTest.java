package book.project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.BookDtoWithoutCategoryIds;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.BookMapper;
import book.project.bookstore.model.Book;
import book.project.bookstore.model.Category;
import book.project.bookstore.repository.book.BookRepository;
import book.project.bookstore.repository.book.BookSearchParameters;
import book.project.bookstore.repository.book.BookSpecificationBuilder;
import book.project.bookstore.repository.category.CategoryRepository;
import book.project.bookstore.service.book.BookServiceImpl;
import book.project.bookstore.utils.TestDataUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private List<Book> bookList;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        Category sciFiCategory = new Category()
                .setId(1L)
                .setName("Science Fiction");

        Category fantasyCategory = new Category()
                .setId(2L)
                .setName("Fantasy");

        Book firstBook = new Book().setId(1L)
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("1234567890123")
                .setPrice(BigDecimal.valueOf(15))
                .setCategories(Set.of(sciFiCategory));

        Book secondBook = new Book()
                .setId(2L)
                .setTitle("Game of Thrones")
                .setAuthor("George Martyn")
                .setIsbn("1234567890124")
                .setPrice(BigDecimal.valueOf(20))
                .setCategories(Set.of(fantasyCategory));

        Book thirdBook = new Book()
                .setId(3L)
                .setTitle("Star Wars")
                .setAuthor("Timothy Zan")
                .setIsbn("1234567890125")
                .setPrice(BigDecimal.valueOf(25))
                .setCategories((Set.of(sciFiCategory, fantasyCategory)));

        bookList = List.of(firstBook, secondBook, thirdBook);
        categoryList = List.of(fantasyCategory, sciFiCategory);
    }

    @Test
    @DisplayName("Verify correct book dto list returned using any Pageable arg")
    public void findAll_WithAnyPageable_ShouldReturnBooksDtoList() {
        //Given (Arrange)
        Page<Book> bookPage = new PageImpl<>(bookList);

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        List<BookDto> expectedBookDtoList = bookList.stream()
                .map(bookMapper::toDto)
                .toList();

        //When (Act)
        List<BookDto> actualList = bookService.findAll(PageRequest.of(0, 10));

        //Then (Assert)
        assertFalse(actualList.isEmpty());
        assertEquals(expectedBookDtoList.size(), actualList.size());
        assertEquals(expectedBookDtoList, actualList);

        verify(bookRepository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Verify correct book dto list returned using any Pageable arg "
            + "and specific search parameters")
    public void search_WithValidBookSearchParametersAndAnyPageable_ShouldReturnBooksDtoList() {
        //Given (Arrange)
        String searchBy = "s";

        List<Book> filteredByCharInNameList = bookList.stream()
                .filter(b -> b.getTitle().contains(searchBy))
                .toList();

        Page<Book> filteredBooksPage = new PageImpl<>(filteredByCharInNameList);

        BookSearchParameters searchParameters = new BookSearchParameters(new String[]{searchBy},
                new String[]{});

        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(searchParameters);

        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);

        when(bookRepository.findAll(eq(bookSpecification), any(Pageable.class)))
                .thenReturn(filteredBooksPage);

        List<BookDto> expectedFilteredBookDtoList = filteredByCharInNameList.stream()
                .map(bookMapper::toDto)
                .toList();

        //When (Act)
        List<BookDto> actualList = bookService.search(searchParameters,
                PageRequest.of(0, 10));

        //Then (Assert)
        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilteredBookDtoList.size(), actualList.size());
        assertEquals(expectedFilteredBookDtoList, actualList);

        verify(bookRepository, times(1)).findAll(eq(bookSpecification), any(Pageable.class));
        verify(bookSpecificationBuilder, times(2)).build(searchParameters);
    }

    @Test
    @DisplayName("Verify correct book dto without categories list returned using any Pageable arg "
            + "and valid category id")
    public void findByCategoryId_WithValidCategoryId_ShouldReturnBookDtoWithoutCategoryIdsList() {
        //Given (Arrange)
        Long categoryId = 1L;
        List<Book> filteredByCategoryId = bookList.stream()
                .filter(b -> b.getCategories().stream()
                        .anyMatch(c -> c.getId().equals(categoryId)))
                .toList();

        Page<Book> filteredBooksPage = new PageImpl<>(filteredByCategoryId);

        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        when(bookRepository.findByCategoryId(eq(categoryId), any(Pageable.class)))
                .thenReturn(filteredBooksPage);

        List<BookDtoWithoutCategoryIds> expectedFilteredBookDtoList = filteredByCategoryId.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();

        //When (Act)
        List<BookDtoWithoutCategoryIds> actualList = bookService.findByCategoryId(categoryId,
                PageRequest.of(0, 10));

        //Then (Assert)
        assertFalse(actualList.isEmpty());
        assertEquals(expectedFilteredBookDtoList.size(), actualList.size());
        assertEquals(expectedFilteredBookDtoList, actualList);

        verify(bookRepository, times(1)).findByCategoryId(eq(categoryId), any(Pageable.class));
        verify(categoryRepository, times(1)).existsById(categoryId);
    }

    @Test
    @DisplayName("Throw exception using any Pageable arg and incorrect category id")
    public void findByCategoryId_WithInvalidCategoryIdAndAnyPageable_ShouldThrowException() {
        //Given (Arrange)
        Long invalidCategoryId = 10L;

        when(categoryRepository.existsById(invalidCategoryId)).thenReturn(false);

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findByCategoryId(invalidCategoryId,
                        PageRequest.of(0, 10))
        );

        //Then (Assert)
        String expectedMessage = "Category with id " + invalidCategoryId + " not found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(categoryRepository, times(1)).existsById(invalidCategoryId);
    }

    @Test
    @DisplayName("Verify correct book dto returned using valid book id")
    public void findById_WithValidBookId_ShouldReturnBookDto() {
        //Given (Arrange)
        Long validBookId = 3L;
        Book book = TestDataUtil.book(categoryList);
        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        //When (Act)
        BookDto actualBookDto = bookService.findById(validBookId);

        //Then (Assert)
        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository, times(1)).findById(validBookId);
    }

    @Test
    @DisplayName("Throw exception using incorrect book id")
    public void findById_WithInvalidBookId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidBookId = 5L;
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidBookId)
        );

        //Then (Assert)
        String expectedMessage = "Can't find book by id " + invalidBookId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(bookRepository, times(1)).findById(invalidBookId);
    }

    @Test
    @DisplayName("Verify deleteById calls repository method")
    public void deleteById_WithValidId_ShouldCallRepositoryDeleteById() {
        // Given (Arrange)
        Long validBookId = 1L;

        // When (Act)
        bookService.deleteById(validBookId);

        // Then (Assert)
        verify(bookRepository, times(1)).deleteById(validBookId);
    }

    @Test
    @DisplayName("Verify update returns BookDto")
    public void update_WithValidIdAndUpdateRequestDto_ShouldReturnBookDto() {
        // Given (Arrange)
        Long validBookId = 2L;
        UpdateBookRequestDto updateBookRequestDto = TestDataUtil.updateBookRequestDto();

        Book bookToUpdate = bookList.stream()
                .filter(b -> b.getId().equals(validBookId))
                .findFirst()
                .orElseThrow();
        when(bookRepository.findById(validBookId)).thenReturn(Optional.of(bookToUpdate));

        doAnswer(invocation -> {
            Book bookArg = invocation.getArgument(1);
            bookArg.setTitle(updateBookRequestDto.getTitle());
            bookArg.setAuthor(updateBookRequestDto.getAuthor());
            bookArg.setIsbn(updateBookRequestDto.getIsbn());
            bookArg.setPrice(updateBookRequestDto.getPrice());
            bookArg.setCategories(categoryList.stream()
                    .filter(c -> updateBookRequestDto.getCategoryIds().contains(c.getId()))
                    .collect(Collectors.toSet()));
            return null;
        }).when(bookMapper).updateFromDto(eq(updateBookRequestDto), any(Book.class));

        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);

        BookDto expectedBookDto = TestDataUtil.mapToBookDto(validBookId, updateBookRequestDto);

        when(bookMapper.toDto(bookToUpdate)).thenReturn(expectedBookDto);

        // When (Act)
        BookDto actualBookDto = bookService.update(validBookId, updateBookRequestDto);

        // Then (Assert)
        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookRepository, times(1)).findById(validBookId);
        verify(bookMapper, times(1)).updateFromDto(eq(updateBookRequestDto), any(Book.class));
        verify(bookRepository, times(1)).save(bookToUpdate);
        verify(bookMapper, times(1)).toDto(bookToUpdate);
    }

    @Test
    @DisplayName("Throw exception using incorrect book id")
    public void update_WithInvalidBookId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidBookId = 5L;
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(invalidBookId, any(UpdateBookRequestDto.class))
        );

        //Then (Assert)
        String expectedMessage = "Can't find book by id " + invalidBookId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(bookRepository, times(1)).findById(invalidBookId);
    }

    @Test
    @DisplayName("Verify correct book dto returned using valid book id")
    public void save_WithValidCreateBookRequestDto_ShouldReturnBookDto() {
        //Given (Arrange)
        CreateBookRequestDto createBookRequestDto = TestDataUtil.createBookRequestDto();

        Book book = TestDataUtil.mapToBook(createBookRequestDto, categoryList);

        BookDto expectedBookDto = TestDataUtil.mapToBookDto(book);

        when(bookMapper.toEntity(createBookRequestDto)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);
        when(bookRepository.save(book)).thenReturn(book);

        //When (Act)
        BookDto actualBookDto = bookService.save(createBookRequestDto);

        //Then (Assert)
        assertNotNull(actualBookDto);
        assertEquals(expectedBookDto, actualBookDto);
        verify(bookMapper, times(1)).toEntity(createBookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookRepository, times(1)).save(book);
    }
}
