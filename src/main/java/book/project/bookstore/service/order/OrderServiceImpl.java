package book.project.bookstore.service.order;

import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.order.UpdateOrderStatusRequestDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.OrderItemMapper;
import book.project.bookstore.mapper.OrderMapper;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.Order;
import book.project.bookstore.model.OrderItem;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.repository.cart.CartRepository;
import book.project.bookstore.repository.order.OrderItemRepository;
import book.project.bookstore.repository.order.OrderRepository;
import book.project.bookstore.security.SecurityUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderDto> getOrders() {
        Long userId = SecurityUtil.getLoggedInUserId();
        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        Long userId = SecurityUtil.getLoggedInUserId();
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order № + "
                        + orderId + " for current user wasn't found"));

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        Long userId = SecurityUtil.getLoggedInUserId();
        Order order = findByIdAndUserId(orderId, userId);
        return order.getOrderItems().stream()
                .filter(o -> Objects.equals(o.getId(), itemId))
                .map(orderItemMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item with id + "
                        + itemId + " in order №" + orderId
                        + " for current user wasn't found"));
    }

    @Override
    public OrderDto updateItemStatus(Long orderId,
                              UpdateOrderStatusRequestDto updateRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        Order order = findByIdAndUserId(orderId, userId);
        order.setStatus(updateRequestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto save(CreateOrderRequestDto createRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart for user"
                        + "with id " + userId + " not found"));

        Order order = orderMapper.toCreateReadyOrderFromCart(cart, createRequestDto);
        orderRepository.save(order);

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map((CartItem item) ->
                        orderItemMapper
                                .toOrderItemFromCartItem(item,
                                        orderMapper.toDto(order)))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);

        orderItemRepository.saveAll(orderItems);
        return orderMapper.toDto(order);
    }

    private Order findByIdAndUserId(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order №"
                        + orderId + " for current user wasn't found"));
    }
}
