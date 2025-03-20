package book.project.bookstore.dto.internal.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemDto(
        @Min(value = 1, message = "Quantity can't be less than 0")
        @NotNull(message = "Quantity is required")
        Integer quantity
) {
}
