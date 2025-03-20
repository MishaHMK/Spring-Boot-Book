package book.project.bookstore.service;

import book.project.bookstore.dto.internal.cartitem.CartItemDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.dto.internal.user.UserDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.CartItemMapper;
import book.project.bookstore.mapper.CartMapper;
import book.project.bookstore.model.Book;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.repository.book.BookRepository;
import book.project.bookstore.repository.cart.CartItemRepository;
import book.project.bookstore.security.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final UserService userService;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;

    @Override
    public CartItemDto save(CreateCartItemDto createCartItemDto) {
        String currentUserName = SecurityUtil.getLoggedInUsername();
        UserDto user = userService.findByUsername(currentUserName);
        ShoppingCart cart = cartMapper
                .toEntity(cartService.findByUserId(user.id()));
        Long bookId = createCartItemDto.bookId();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book by id "
                        + bookId));
        CartItem cartItem = cartItemMapper.toEntity(createCartItemDto);
        cartItem.setCart(cart);
        cartItem.setBook(book);

        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cartItem by id "
                        + cartItemId));
        cartItemMapper.updateFromDto(updateCartItemDto, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));

    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
