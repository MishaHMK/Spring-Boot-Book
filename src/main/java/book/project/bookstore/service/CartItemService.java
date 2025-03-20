package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cartitem.CartItemDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;

public interface CartItemService {
    CartItemDto save(CreateCartItemDto createCartItemDto);

    CartItemDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto);

    void deleteById(Long id);
}
