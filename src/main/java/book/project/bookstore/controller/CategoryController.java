package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.book.BookDtoWithoutCategoryIds;
import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import book.project.bookstore.service.BookService;
import book.project.bookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category Management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all categories",
            description = "Get all categories with possible pagination/sorting")
    public List<CategoryDto> getAll(@ParameterObject Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id",
            description = "Get specific category by given unique id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id",
            description = "Get all books which belong for category by given category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, @ParameterObject Pageable pageable) {
        return bookService.findByCategoryId(id, pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create category",
            description = "Create new category with given data")
    public CategoryDto createCategory(
            @Valid @RequestBody CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category by id",
            description = "Get specific category by given id and new category data")
    public CategoryDto updateCategoryById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by id",
            description = "Mark specific category by given id as removed")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}
