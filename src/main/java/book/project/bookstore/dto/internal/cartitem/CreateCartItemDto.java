package book.project.bookstore.dto.internal.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemDto {
    @NotNull(message = "BookId is required")
    private Long bookId;
    @Positive(message = "Quantity can't be less than 0")
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}
