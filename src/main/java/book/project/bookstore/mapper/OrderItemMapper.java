package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.order.OrderDto;
import book.project.bookstore.dto.internal.orderitem.OrderItemDto;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "item.book", target = "book")
    @Mapping(source = "item.book.price", target = "price")
    @Mapping(source = "order", target = "order")
    OrderItem toOrderItemFromCartItem(CartItem item, OrderDto order);
}
