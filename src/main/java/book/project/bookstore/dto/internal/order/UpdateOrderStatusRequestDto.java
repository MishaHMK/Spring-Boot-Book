package book.project.bookstore.dto.internal.order;

import book.project.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Order.StatusName status;
}
