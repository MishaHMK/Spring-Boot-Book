package book.project.bookstore.dto.internal.order;

import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record OrderDto(Long id, Long userId, Set<OrderItemDto> orderItems, Instant orderDate,
                        BigDecimal total, String status) {
}
