package book.project.bookstore.dto.internal.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemDto(
        @Positive(message = "Quantity can't be less than 0")
        @NotNull(message = "Quantity is required")
        Integer quantity
) {
}
