package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.order.UpdateOrderStatusRequestDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import book.project.bookstore.service.CartService;
import book.project.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final CartService cartService;
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order",
            description = "Create new order with given date")
    public OrderDto createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
        return orderService.save(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all user orders",
            description = "Get all user orders of currently authorized user")
    public List<OrderDto> getAllOrders() {
        return orderService.getOrders();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items")
    @Operation(summary = "Get all items of user order",
            description = "Get all items for selected order "
                    + "of currently authorized user")
    public List<OrderItemDto> getAllOrders(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get item by id of user order",
            description = "Select item by id for selected order "
                    + "of currently authorized user")
    public OrderItemDto getOrderItem(@PathVariable Long orderId,
                                           @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("{orderId}")
    @Operation(summary = "Change item status",
            description = "Change status for any selected order")
    public OrderDto getOrderItem(@PathVariable Long orderId,
                                 @Valid @RequestBody
                                 UpdateOrderStatusRequestDto orderItemDto) {
        return orderService.updateItemStatus(orderId, orderItemDto);
    }
}
