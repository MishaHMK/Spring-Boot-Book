package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.cartitem.CartItemDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.model.CartItem;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem item);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem createToEntity(CreateCartItemDto createDto);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem dtoToEntity(CartItemDto dto);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateCartItemDto dto, @MappingTarget CartItem cartItem);

    @Named("cartItemSetToEntity")
    default Set<CartItem> cartItemSetToEntity(Set<CartItemDto> dtoSet) {
        if (dtoSet == null) {
            return new HashSet<>();
        }
        return dtoSet.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toSet());
    }
}
