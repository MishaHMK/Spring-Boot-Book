package book.project.bookstore.dto.internal.cartitem;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemDto(
        @Positive(message = "Quantity can't be less than 0")
        int quantity) {
}
