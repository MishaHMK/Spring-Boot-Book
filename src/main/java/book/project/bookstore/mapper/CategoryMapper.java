package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import book.project.bookstore.model.Category;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    Category toEntity(CreateCategoryRequestDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateCategoryRequestDto dto, @MappingTarget Category category);

    @Named("categoriesByIds")
    default Set<Category> mapCategoriesIdsToCategories(Set<Long> categoryIds) {
        if (categoryIds == null) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
