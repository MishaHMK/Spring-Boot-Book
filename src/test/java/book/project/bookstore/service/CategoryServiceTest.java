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

import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.CategoryMapper;
import book.project.bookstore.model.Category;
import book.project.bookstore.repository.category.CategoryRepository;
import book.project.bookstore.service.category.CategoryServiceImpl;
import book.project.bookstore.utils.TestDataUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        Category sciFi = new Category()
                .setId(1L)
                .setName("Science Fiction");

        Category fantasy = new Category()
                .setId(2L)
                .setName("Fantasy");

        categoryList = List.of(fantasy, sciFi);
    }

    @Test
    @DisplayName("Verify correct category dto list returned using any Pageable arg")
    public void findAll_WithAnyPageable_ShouldReturnCategoryDtoList() {
        //Given (Arrange)
        Page<Category> page = new PageImpl<>(categoryList);

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<CategoryDto> expectedDtoList = categoryList.stream()
                .map(categoryMapper::toDto)
                .toList();

        //When (Act)
        List<CategoryDto> actualDtoList = categoryService.findAll(Pageable.unpaged());

        //Then (Assert)
        assertFalse(actualDtoList.isEmpty());
        assertEquals(expectedDtoList.size(), actualDtoList.size());
        assertEquals(expectedDtoList, actualDtoList);

        verify(categoryRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Verify correct category returned using valid id")
    public void findById_WithValidId_ShouldReturnCategoryDtoList() {
        //Given (Arrange)
        Long validId = 2L;
        Category category = categoryList.get(1);

        CategoryDto expectedDto = TestDataUtil.mapToCategoryDto(category);

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        //When (Act)
        CategoryDto actualDto = categoryService.findById(validId);

        //Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);

        verify(categoryRepository, times(1)).findById(validId);
    }

    @Test
    @DisplayName("Throw exception using incorrect category id")
    public void findById_WithInvalidId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidId = 5L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(invalidId)
        );

        //Then (Assert)
        String expectedMessage = "Can't find category by id " + invalidId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

        verify(categoryRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Throw exception using incorrect category id")
    public void save_WithValidCreateBookRequestDto_ShouldReturnCategoryDto() {
        //Given (Arrange)
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto()
                .setName("Science Fiction")
                .setDescription("Science Fiction Description");

        Category category = new Category()
                .setName(createCategoryRequestDto.getName())
                .setDescription(createCategoryRequestDto.getDescription());

        CategoryDto expectedDto = new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());

        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        //When (Act)
        CategoryDto actualDto = categoryService.save(createCategoryRequestDto);

        //Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(categoryMapper, times(1)).toEntity(createCategoryRequestDto);
        verify(categoryMapper, times(1)).toDto(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Verify deleteById calls repository method")
    public void deleteById_WithValidId_ShouldCallRepositoryDeleteById() {
        // Given (Arrange)
        Long validId = 1L;

        // When (Act)
        categoryService.deleteById(validId);

        // Then (Assert)
        verify(categoryRepository, times(1)).deleteById(validId);
    }

    @Test
    @DisplayName("Verify update returns CategoryDto")
    public void update_WithValidIdAndUpdateRequestDto_ShouldReturnBookDto() {
        // Given (Arrange)
        Long validId = 1L;
        UpdateCategoryRequestDto updateCategoryRequestDto = TestDataUtil.updateCategoryRequestDto();

        Category categoryToUpdate = categoryList.stream()
                .filter(b -> b.getId().equals(validId))
                .findFirst()
                .orElseThrow();

        when(categoryRepository.findById(validId)).thenReturn(Optional.of(categoryToUpdate));

        doAnswer(invocation -> {
            Category categoryArg = invocation.getArgument(1);
            categoryArg.setName(categoryToUpdate.getName());
            categoryArg.setDescription(categoryToUpdate.getDescription());
            return null;
        }).when(categoryMapper).updateFromDto(eq(updateCategoryRequestDto), any(Category.class));

        when(categoryRepository.save(categoryToUpdate)).thenReturn(categoryToUpdate);

        CategoryDto expectedDto = new CategoryDto()
                .setId(validId)
                .setName(updateCategoryRequestDto.getName())
                .setDescription(updateCategoryRequestDto.getDescription());

        when(categoryMapper.toDto(categoryToUpdate)).thenReturn(expectedDto);

        // When (Act)
        CategoryDto actualDto = categoryService.update(validId, updateCategoryRequestDto);

        // Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(categoryRepository, times(1)).findById(validId);
        verify(categoryMapper, times(1)).updateFromDto(eq(updateCategoryRequestDto),
                any(Category.class));
        verify(categoryRepository, times(1)).save(categoryToUpdate);
        verify(categoryMapper, times(1)).toDto(categoryToUpdate);
    }

    @Test
    @DisplayName("Verify update returns CategoryDto")
    public void update_WithInvalidValidId_ShouldThrowException() {
        // Given (Arrange)
        Long invalidId = 5L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(invalidId, any(UpdateCategoryRequestDto.class))
        );

        // Then (Assert)
        String expectedMessage = "Can't find category by id " + invalidId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
        verify(categoryRepository, times(1)).findById(invalidId);
    }
}
