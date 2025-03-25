package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.order.CreateOrderRequestDto;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.Order;
import book.project.bookstore.model.ShoppingCart;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "orderItems", source = "cart.cartItems")
    @Mapping(target = "shippingAddress", source = "orderDto.shippingAddress")
    @Mapping(target = "total", source = "cart.cartItems",
            qualifiedByName = "getTotalPriceForOrder")
    Order toCreateReadyOrderFromCart(ShoppingCart cart, CreateOrderRequestDto orderDto);

    @AfterMapping
    default void setDefaultStatus(@MappingTarget Order order) {
        order.setStatus(Order.StatusName.PENDING);
    }

    @AfterMapping
    default void setCurrentTime(@MappingTarget Order order) {
        order.setOrderDate(Instant.now());
    }

    @Named("getTotalPriceForOrder")
    default BigDecimal getTotalPriceForOrder(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(ci -> ci.getBook().getPrice()
                        .multiply(new BigDecimal(ci.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
