package book.project.bookstore.service.order;

import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.order.UpdateOrderStatusRequestDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.exception.OrderProcessingException;
import book.project.bookstore.mapper.OrderItemMapper;
import book.project.bookstore.mapper.OrderMapper;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.Order;
import book.project.bookstore.model.OrderItem;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.repository.cart.CartItemRepository;
import book.project.bookstore.repository.cart.ShoppingCartRepository;
import book.project.bookstore.repository.order.OrderItemRepository;
import book.project.bookstore.repository.order.OrderRepository;
import book.project.bookstore.security.SecurityUtil;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<OrderDto> getOrders(Pageable pageable) {
        Long userId = SecurityUtil.getLoggedInUserId();
        return orderRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        Long userId = SecurityUtil.getLoggedInUserId();
        List<OrderItem> orderItems = orderItemRepository
                .findAllByOrderIdAndOrderUserId(orderId, userId, pageable);

        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        Long userId = SecurityUtil.getLoggedInUserId();
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Item with id + "
                        + itemId + " in order №" + orderId
                        + " for current user wasn't found"));

        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderDto updateItemStatus(Long orderId,
                              UpdateOrderStatusRequestDto updateRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        Order order = findByIdAndUserId(orderId, userId);
        order.setStatus(Order.StatusName.valueOf(updateRequestDto.getStatus()));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto save(CreateOrderRequestDto createRequestDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        ShoppingCart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart for user"
                        + "with id " + userId + " not found"));
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Cart is empty. Add some item first.");
        }
        Order order = orderMapper.toCreateReadyOrderFromCart(cart, createRequestDto);
        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map((CartItem item) ->
                        orderItemMapper.toOrderItemFromCartItem(item, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        orderRepository.save(order);

        cartItemRepository.deleteAllByCartId(cart.getId());

        return orderMapper.toDto(order);
    }

    private Order findByIdAndUserId(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Order №"
                        + orderId + " for current user wasn't found"));
    }
}
