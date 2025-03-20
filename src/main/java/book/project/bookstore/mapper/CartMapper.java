package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class, UserMapper.class})
public interface CartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart cart);

    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    ShoppingCart toEntity(ShoppingCartDto dto);
}
