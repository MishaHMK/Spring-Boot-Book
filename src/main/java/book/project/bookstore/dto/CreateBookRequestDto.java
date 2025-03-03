package book.project.bookstore.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 30, message = "Title must be at least 3 characters long "
            + "and not longer than 30 characters")
    private String title;
    @NotBlank(message = "Author is required")
    private String author;
    @Size(min = 13, max = 13, message = "ISBN must be 13 characters long")
    @NotBlank(message = "ISBN is required")
    private String isbn;
    @Positive(message = "Price can't have negative value")
    @NotNull(message = "Price is required")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
