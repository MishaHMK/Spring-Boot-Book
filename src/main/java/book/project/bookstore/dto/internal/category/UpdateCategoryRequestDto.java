package book.project.bookstore.dto.internal.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequestDto {
    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 20, message = "Category name must be at least "
            + "3 characters long and not longer than 20 characters")
    private String name;
    private String description;
}
