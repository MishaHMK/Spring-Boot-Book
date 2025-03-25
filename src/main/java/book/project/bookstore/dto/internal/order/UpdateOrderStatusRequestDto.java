package book.project.bookstore.dto.internal.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull(message = "New order status is required")
    private String status;
}
