package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.model.User;

public interface CartService {
    ShoppingCartDto findByUserId(Long userId);

    ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId);

    ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto);

    void deleteById(Long id);

    void createShoppingCartForUser(User user);
}
