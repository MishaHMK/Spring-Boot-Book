package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class, UserMapper.class})
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart cart);

    @Mapping(target = "cartItems", source = "cartItems",
            qualifiedByName = "cartItemSetToEntity")
    @Mapping(source = "userId", target = "user.id")
    ShoppingCart toEntity(ShoppingCartDto dto);

    void updateFromDto(ShoppingCartDto dto, @MappingTarget ShoppingCart cart);
}
