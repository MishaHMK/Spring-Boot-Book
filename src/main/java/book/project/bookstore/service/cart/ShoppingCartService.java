package book.project.bookstore.service.cart;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto findByCartItemsId(Long cartItemId);

    ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId);

    ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto);

    void deleteById(Long id);

    void createShoppingCartForUser(User user);
}
