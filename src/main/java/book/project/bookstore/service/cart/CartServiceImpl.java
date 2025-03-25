package book.project.bookstore.service.cart;

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
import book.project.bookstore.service.book.BookService;
import jakarta.transaction.Transactional;
import java.util.Optional;
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
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public ShoppingCartDto findByCartItemsId(Long cartItemId) {
        ShoppingCart shoppingCart = cartRepository.findByCartItemsId(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Cart of cart item with id " + cartItemId
                        + " wasn't found"));
        return cartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addItemToCart(CreateCartItemDto createCartItemDto, Long userId) {
        ShoppingCartDto cart = findByUserId(userId);
        Long bookId = createCartItemDto.getBookId();
        Optional<CartItem> cartItem = cartItemRepository.findByBookIdAndCartId(bookId, userId);
        if (cartItem.isEmpty()) {
            CartItem newItem = cartItemMapper.createToEntity(createCartItemDto);
            newItem.setBook(bookMapper.dtoToEntity(bookService.findById(bookId)));
            newItem.setCart(cartMapper.toEntity(cart));
            cartItemRepository.save(newItem);
            cart.cartItems().add(cartItemMapper.toDto(newItem));
            return cart;
        } else {
            CartItem existingItem = cartItem.get();
            cartItemMapper.updateFromDto(
                    new UpdateCartItemDto(createCartItemDto.getQuantity()),
                    existingItem);
            cartItemRepository.save(existingItem);
            return cartMapper.toDto(existingItem.getCart());
        }
    }

    @Override
    public ShoppingCartDto update(Long cartItemId, UpdateCartItemDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cartItem by id "
                        + cartItemId));
        cartItemMapper.updateFromDto(updateCartItemDto, cartItem);
        cartItemRepository.save(cartItem);
        System.out.println(cartItem);
        return findByCartItemsId(cartItem.getId());
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
