package book.project.bookstore.dto.internal.order;

import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(Long id,
                       Long userId,
                       Set<OrderItemDto> orderItems,
                       LocalDateTime orderDate,
                       BigDecimal total,
                       String status) {
}
