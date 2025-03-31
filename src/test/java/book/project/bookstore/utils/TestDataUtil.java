package book.project.bookstore.utils;

import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.BookDtoWithoutCategoryIds;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import book.project.bookstore.model.Book;
import book.project.bookstore.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestDataUtil {
    private TestDataUtil() {

    }

    public static BookDto bookDto() {
        return new BookDto()
                .setId(2L)
                .setTitle("Sample Book 2")
                .setAuthor("Author B")
                .setIsbn("1236567163337")
                .setPrice(BigDecimal.valueOf(24.99))
                .setCategoryIds(Set.of(2L));
    }

    public static Book book(List<Category> categories) {
        return new Book()
                .setId(3L)
                .setTitle("Sample Book 3")
                .setAuthor("Author A")
                .setIsbn("1236567163338")
                .setPrice(BigDecimal.valueOf(15.99))
                .setCategories(Set.of(categories.get(0),
                        categories.get(1)));
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Title")
                .setAuthor("Author")
                .setIsbn("1234567890123")
                .setPrice(BigDecimal.valueOf(14.99))
                .setCategoryIds(Set.of(1L));
    }

    public static UpdateBookRequestDto updateBookRequestDto() {
        return new UpdateBookRequestDto()
                .setTitle("Updated Book Title")
                .setAuthor("Author B")
                .setIsbn("1236567163330")
                .setPrice(BigDecimal.valueOf(25.99))
                .setCategoryIds(Set.of(2L));
    }

    public static BookDto mapToBookDto(CreateBookRequestDto requestDto) {
        return new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    public static BookDto mapToBookDto(Long bookId, UpdateBookRequestDto requestDto) {
        return new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setCategoryIds(requestDto.getCategoryIds());
    }

    public static BookDto mapToBookDto(Book book) {
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setCategoryIds(book.getCategories().stream()
                        .map(Category::getId)
                        .collect(Collectors.toSet()));
    }

    public static Book mapToBook(CreateBookRequestDto createBookRequestDto,
                                    List<Category> categoryList) {
        return new Book()
                .setTitle(createBookRequestDto.getTitle())
                .setAuthor(createBookRequestDto.getAuthor())
                .setIsbn(createBookRequestDto.getIsbn())
                .setPrice(createBookRequestDto.getPrice())
                .setCategories(categoryList.stream()
                        .filter(c -> createBookRequestDto.getCategoryIds().contains(c.getId()))
                        .collect(Collectors.toSet()));
    }

    public static List<BookDto> bookDtoList() {
        List<BookDto> testList = new ArrayList<>();
        testList.add(new BookDto()
                .setId(1L)
                .setTitle("Sample Book 1")
                .setAuthor("Author A")
                .setIsbn("1236567163336")
                .setPrice(BigDecimal.valueOf(15.99))
                .setCategoryIds(Set.of(1L)));
        testList.add(new BookDto()
                .setId(2L)
                .setTitle("Sample Book 2")
                .setAuthor("Author B")
                .setIsbn("1236567163337")
                .setPrice(BigDecimal.valueOf(24.99))
                .setCategoryIds(Set.of(2L)));
        testList.add(new BookDto()
                .setId(3L)
                .setTitle("Sample Book 3")
                .setAuthor("Author A")
                .setIsbn("1236567163338")
                .setPrice(BigDecimal.valueOf(31.99))
                .setCategoryIds(Set.of(1L, 2L)));
        return testList;
    }

    public static List<CategoryDto> categoryDtoList() {
        List<CategoryDto> testList = new ArrayList<>();
        testList.add(new CategoryDto().setId(3L)
                .setName("cooking"));
        testList.add(new CategoryDto().setId(4L)
                .setName("IT guides"));
        testList.add(new CategoryDto().setId(5L)
                .setName("novel"));
        return testList;
    }

    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("language");
    }

    public static CategoryDto mapToCategoryDto(CreateCategoryRequestDto requestDto) {
        return new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
    }

    public static CategoryDto mapToCategoryDto(Long id, UpdateCategoryRequestDto requestDto) {
        return new CategoryDto()
                .setId(id)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    public static UpdateCategoryRequestDto updateCategoryRequestDto() {
        return new UpdateCategoryRequestDto()
                .setName("Updated Category")
                .setDescription("Updated Description");
    }

    public static CategoryDto categoryDto() {
        return new CategoryDto()
                .setId(3L)
                .setName("cooking");
    }
    
    public static List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIdsList() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(5L)
                .setTitle("Sample Book 5")
                .setAuthor("Author B")
                .setIsbn("1236567163330")
                .setPrice(BigDecimal.valueOf(34.99)));
        expected.add(new BookDtoWithoutCategoryIds()
                .setId(6L)
                .setTitle("Sample Book 6")
                .setAuthor("Author C")
                .setIsbn("1236567163332")
                .setPrice(BigDecimal.valueOf(12.99)));
        return expected;
    }
}
