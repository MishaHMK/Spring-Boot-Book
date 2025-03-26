package book.project.bookstore.service.order;

import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.order.UpdateOrderStatusRequestDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderDto> getOrders(Pageable pageable);

    List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    OrderDto save(CreateOrderRequestDto orderDto);

    OrderDto updateItemStatus(Long orderId,
                     UpdateOrderStatusRequestDto updateRequestDto);
}
