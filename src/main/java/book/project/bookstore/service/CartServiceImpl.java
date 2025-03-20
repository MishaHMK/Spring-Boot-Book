package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.CartMapper;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.repository.cart.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public ShoppingCartDto findByUserId(Long userId) {
        ShoppingCart shoppingCart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Cart of user with id " + userId
                        + " wasn't found"));

        return cartMapper.toDto(shoppingCart);
    }
}
