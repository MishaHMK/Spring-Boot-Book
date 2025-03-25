package book.project.bookstore.service;

import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.order.UpdateOrderStatusRequestDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getOrders();

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    OrderDto save(CreateOrderRequestDto orderDto);

    OrderDto updateItemStatus(Long orderId,
                     UpdateOrderStatusRequestDto updateRequestDto);
}
