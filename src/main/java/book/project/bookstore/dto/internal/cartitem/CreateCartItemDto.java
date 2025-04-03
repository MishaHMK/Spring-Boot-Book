package book.project.bookstore.dto.internal.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CreateCartItemDto {
    @NotNull(message = "BookId is required")
    @Positive
    private Long bookId;
    @Positive(message = "Quantity can't be less than 0")
    private int quantity;
}
