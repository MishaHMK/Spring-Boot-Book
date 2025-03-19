package book.project.bookstore.service;

import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, UpdateCategoryRequestDto categoryDto);

    void deleteById(Long id);

}
