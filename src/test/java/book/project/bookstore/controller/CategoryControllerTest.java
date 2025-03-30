package book.project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.project.bookstore.dto.internal.book.BookDtoWithoutCategoryIds;
import book.project.bookstore.dto.internal.category.CategoryDto;
import book.project.bookstore.dto.internal.category.CreateCategoryRequestDto;
import book.project.bookstore.dto.internal.category.UpdateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/delete-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/init-for-category-tests.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/delete-for-category-tests.sql")
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/categories/delete-language-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        //Given (Arrange)
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("language");

        CategoryDto categoryDto = new CategoryDto()
                .setName(requestDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When (Act)
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //Then (Assert)
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(categoryDto, actual, "id");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all categories")
    void getAll_GivenCategories_ShouldReturnAllBooks() throws Exception {
        //Given (Arrange)
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto().setId(3L)
                .setName("cooking"));
        expected.add(new CategoryDto().setId(4L)
                .setName("IT guides"));
        expected.add(new CategoryDto().setId(5L)
                .setName("novel"));

        //When (Act)
        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books by category id")
    void getBooksByCategoryId_GivenCategoryId_ShouldReturnBooksWithoutCategoryIds()
            throws Exception {
        //Given (Arrange)
        long categoryId = 4L;
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

        //When (Act)
        MvcResult result = mockMvc.perform(get("/categories/"
                        + categoryId + "/books"))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update category by id")
    void updateBookById_WithValidInput_ReturnsUpdatedBook() throws Exception {
        //Given (Arrange)
        Long categoryId = 4L;
        UpdateCategoryRequestDto updateRequestDto = new UpdateCategoryRequestDto()
                .setName("Updated Category");

        CategoryDto expected = new CategoryDto()
                .setId(categoryId)
                .setName(updateRequestDto.getName());

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        //When (Act)
        MvcResult result = mockMvc.perform(put("/categories/" + expected.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        //Then (Assert)
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get category by id")
    void getById_ValidId_ShouldReturnCategoryDto() throws Exception {
        //Given (Arrange)
        CategoryDto expected = new CategoryDto();
        expected.setId(3L)
                .setName("cooking");

        //When (Act)
        MvcResult result = mockMvc.perform(get("/categories/" + expected.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Delete category by id")
    void deleteCategoryById_WithValidId_NoContent() throws Exception {
        //Given (Arrange)
        long categoryId = 8L;

        //When (Act)
        MvcResult result = mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isNoContent())
                .andReturn();

        //Then (Assert)
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
}
