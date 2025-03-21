package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.BookMapper;
import book.project.bookstore.mapper.CartItemMapper;
import book.project.bookstore.mapper.CartMapper;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.model.User;
import book.project.bookstore.repository.cart.CartItemRepository;
import book.project.bookstore.repository.cart.CartRepository;
import book.project.bookstore.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public ShoppingCartDto findByUserId(Long userId) {
        ShoppingCart shoppingCart = cartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Cart of user with id " + userId
                        + " wasn't found"));

        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        if (user == null) {
            throw new EntityNotFoundException("User cannot be null");
        }
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        ShoppingCart cart = cartMapper.toEntity(findByUserId(userId));
        Long bookId = createCartItemDto.getBookId();

        CartItem cartItem = cartItemRepository.findByBookIdAndCartId(bookId, userId)
                .map(existingItem -> {
                    cartItemMapper.updateFromDto(
                            new UpdateCartItemDto(createCartItemDto.getQuantity()),
                            existingItem);
                    return existingItem;
                })
                .orElseGet(() -> {
                    CartItem newItem = cartItemMapper.toEntity(createCartItemDto);
                    newItem.setBook(bookMapper.dtoToEntity(bookService.findById(bookId)));
                    newItem.setCart(cart);
                    return newItem;
                });

        cartItemRepository.save(cartItem);

        return findByUserId(userId);
    }

    @Override
    public ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cartItem by id "
                        + cartItemId));
        cartItemMapper.updateFromDto(updateCartItemDto, cartItem);
        cartItemRepository.save(cartItem);
        return findByUserId(cartItem.getCart().getId());
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
