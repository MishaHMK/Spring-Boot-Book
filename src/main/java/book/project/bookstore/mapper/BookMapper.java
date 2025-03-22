package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.BookDtoWithoutCategoryIds;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
import book.project.bookstore.model.Book;
import book.project.bookstore.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class}, config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "categoriesByIds")
    Book toEntity(CreateBookRequestDto dto);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "categoriesByIds")
    Book dtoToEntity(BookDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "categoriesByIds")
    void updateFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("bookFromId")
    default Book bookFromId(Long bookId) {
        if (bookId == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
