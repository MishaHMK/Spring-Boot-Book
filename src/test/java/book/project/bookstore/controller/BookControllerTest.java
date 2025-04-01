package book.project.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.project.bookstore.dto.internal.book.BookDto;
import book.project.bookstore.dto.internal.book.CreateBookRequestDto;
import book.project.bookstore.dto.internal.book.UpdateBookRequestDto;
import book.project.bookstore.utils.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {
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
                    new ClassPathResource("database/categories/delete-for-category-tests.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-three-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books/add-two-categories.sql")
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
                    new ClassPathResource("database/books/delete-all-books.sql")
            );
        }
    }

    @Test
    @Sql(
            scripts = "classpath:database/books/delete-title-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        //Given (Arrange)
        CreateBookRequestDto requestDto = TestDataUtil.createBookRequestDto();

        BookDto bookDto = TestDataUtil.mapToBookDto(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When (Act)
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //Then (Assert)
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get all books")
    void getAll_GivenBooks_ShouldReturnAllBooks() throws Exception {
        //Given (Arrange)
        List<BookDto> expected = TestDataUtil.bookDtoList();

        //When (Act)
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto[].class);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.length);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get book by id")
    void getById_ValidId_ShouldReturnBookDto() throws Exception {
        //Given (Arrange)
        BookDto expected = TestDataUtil.bookDto();

        //When (Act)
        MvcResult result = mockMvc.perform(get("/books/" + expected.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), BookDto.class);
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Delete book by id")
    void deleteBookById_WithValidId_NoContent() throws Exception {
        //Given (Arrange)
        long bookId = 1L;

        //When (Act)
        MvcResult result = mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent())
                .andReturn();

        //Then (Assert)
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update book by id")
    void updateBookById_WithValidInput_ReturnsUpdatedBook() throws Exception {
        //Given (Arrange)
        Long bookId = 3L;
        UpdateBookRequestDto updateRequestDto = TestDataUtil.updateBookRequestDto();

        BookDto expected = TestDataUtil.mapToBookDto(bookId, updateRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        //When (Act)
        MvcResult result = mockMvc.perform(put("/books/" + expected.getId())
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Get books by parameters")
    void search_WithValidInput_ReturnsSelectedList() throws Exception {
        //Given (Arrange)
        List<BookDto> list = TestDataUtil.bookDtoList();
        String authorFilter = "Author A";

        List<BookDto> expected = list.stream()
                .filter(b -> b.getAuthor().equals(authorFilter))
                .toList();

        //When (Act)
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("authors",authorFilter)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then (Assert)
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto[].class);

        assertNotNull(actual);
        assertEquals(expected, Arrays.asList(actual));
    }
}
