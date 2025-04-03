package book.project.bookstore.dto.internal.cart;

import book.project.bookstore.dto.internal.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long id, Long userId, Set<CartItemDto> cartItems){

}
