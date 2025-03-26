package book.project.bookstore.dto.internal.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotBlank(message = "New order status is required")
    private String status;
}
