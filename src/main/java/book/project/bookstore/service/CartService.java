package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;

public interface CartService {
    ShoppingCartDto findByUserId(Long userId);
}
