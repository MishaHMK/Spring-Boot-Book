package book.project.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.mapper.BookMapper;
import book.project.bookstore.mapper.CartItemMapper;
import book.project.bookstore.mapper.CartMapper;
import book.project.bookstore.model.Book;
import book.project.bookstore.model.CartItem;
import book.project.bookstore.model.Category;
import book.project.bookstore.model.Role;
import book.project.bookstore.model.ShoppingCart;
import book.project.bookstore.model.User;
import book.project.bookstore.repository.cart.CartItemRepository;
import book.project.bookstore.repository.cart.ShoppingCartRepository;
import book.project.bookstore.service.book.BookService;
import book.project.bookstore.service.cart.ShoppingCartServiceImpl;
import book.project.bookstore.utils.TestDataUtil;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository cartRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookService bookService;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private ShoppingCartServiceImpl cartService;

    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        Category sciFiCategory = new Category()
                .setId(1L)
                .setName("Science Fiction");

        Category fantasyCategory = new Category()
                .setId(2L)
                .setName("Fantasy");

        Book firstBook = new Book().setId(1L)
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("1234567890123")
                .setPrice(BigDecimal.valueOf(15))
                .setCategories(Set.of(sciFiCategory));

        Book secondBook = new Book()
                .setId(2L)
                .setTitle("Game of Thrones")
                .setAuthor("George Martyn")
                .setIsbn("1234567890124")
                .setPrice(BigDecimal.valueOf(20))
                .setCategories(Set.of(fantasyCategory));

        Role role = new Role()
                .setId(1L)
                .setRole(Role.RoleName.USER);

        User user = new User()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@doe.com")
                .setPassword("password")
                .setRoles(Set.of(role));

        shoppingCart = new ShoppingCart()
                .setId(user.getId())
                .setUser(user);

        CartItem firstItem = new CartItem()
                .setId(1L)
                .setQuantity(3)
                .setBook(firstBook)
                .setCart(shoppingCart);

        CartItem secondItem = new CartItem()
                .setId(2L)
                .setQuantity(5)
                .setBook(secondBook)
                .setCart(shoppingCart);

        shoppingCart.setCartItems(Set.of(firstItem, secondItem));
    }

    @Test
    @DisplayName("Verify correct shopping cart dto using correct user id")
    public void findByUserId_WithCorrectUserId_ShouldReturnCartDto() {
        //Given (Arrange)
        Long userId = shoppingCart.getUser().getId();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto expected = TestDataUtil.mapToCartDto(shoppingCart);
        when(cartMapper.toDto(any(ShoppingCart.class))).thenReturn(expected);

        //When (Act)
        ShoppingCartDto actual = cartService.findByUserId(userId);

        //Then (Assert)
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).findByUserId(userId);
        verify(cartMapper).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Verify exception thrown using incorrect user id")
    public void findByUserId_WithInvalidUserId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidUserId = 2L;
        when(cartRepository.findByUserId(invalidUserId))
                .thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cartService.findByUserId(invalidUserId)
        );

        //Then (Assert)
        String expectedMessage = "Cart of user with id " + invalidUserId
                + " wasn't found";
        assertEquals(exception.getMessage(), expectedMessage);
        verify(cartRepository).findByUserId(invalidUserId);
    }

    @Test
    @DisplayName("Verify saving new cart with using user")
    public void createShoppingCartForUser_WithGivenUser_ShouldCallSave() {
        //Given (Arrange)
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = shoppingCart.getUser();
        when(cartRepository.save(any(ShoppingCart.class)))
                .thenReturn(shoppingCart);

        //When (Act)
        cartService.createShoppingCartForUser(user);

        //Then (Assert)
        verify(cartRepository).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Verify correct shopping cart dto using correct cart item id")
    public void findByCartItemsId_WithValidCartItemId_ShouldReturnCartDto() {
        //Given (Arrange)
        Long cartItemId = 1L;
        when(cartRepository.findByCartItemsId(cartItemId))
                .thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto expected = TestDataUtil.mapToCartDto(shoppingCart);
        when(cartMapper.toDto(any(ShoppingCart.class))).thenReturn(expected);

        //When (Act)
        ShoppingCartDto actual = cartService.findByCartItemsId(cartItemId);

        //Then (Assert)
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(cartRepository).findByCartItemsId(cartItemId);
        verify(cartMapper).toDto(shoppingCart);
    }

    @Test
    @DisplayName("Verify exception thrown using incorrect cart item id")
    public void findByCartItemsId_WithValidCartItemId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidItemId = 3L;
        when(cartRepository.findByCartItemsId(invalidItemId))
                .thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cartService.findByCartItemsId(invalidItemId)
        );

        //Then (Assert)
        String expectedMessage = "Cart of cart item with id " + invalidItemId
                + " wasn't found";
        assertEquals(exception.getMessage(), expectedMessage);
        verify(cartRepository).findByCartItemsId(invalidItemId);
    }

    @Test
    @DisplayName("Verify deleting cart item with using user")
    public void deleteById_WithValidCartItemId_ShouldCallDeleteById() {
        //Given (Arrange)
        Long cartItemId = 1L;

        //When (Act)
        cartService.deleteById(cartItemId);

        //Then (Assert)
        verify(cartItemRepository).deleteById(cartItemId);
    }

    @Test
    @DisplayName("Verify updating cart item returns updated cart")
    public void update_WithValidIdAndUpdateRequestDto_ShouldReturnCartDto() {
        //Given (Arrange)
        Long cartItemId = 1L;
        UpdateCartItemDto updateRequestDto = new UpdateCartItemDto(5);

        CartItem itemToUpdate = shoppingCart.getCartItems().stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow();

        when(cartItemRepository.findById(cartItemId))
                .thenReturn(Optional.of(itemToUpdate));

        doAnswer(invocation -> {
            CartItem arg = invocation.getArgument(1);
            arg.setQuantity(updateRequestDto.quantity());
            arg.setId(cartItemId);
            arg.setBook(itemToUpdate.getBook());
            arg.setCart(itemToUpdate.getCart());
            return arg;
        }).when(cartItemMapper).updateFromDto(eq(updateRequestDto), any(CartItem.class));

        when(cartItemRepository.save(itemToUpdate)).thenReturn(itemToUpdate);

        ShoppingCartDto expectedDto = TestDataUtil.mapToCartDto(shoppingCart);

        when(cartRepository.findByCartItemsId(cartItemId)).thenReturn(Optional.of(shoppingCart));

        when(cartMapper.toDto(any(ShoppingCart.class))).thenReturn(expectedDto);

        when(cartService.findByCartItemsId(cartItemId)).thenReturn(expectedDto);

        //When (Act)
        ShoppingCartDto actualDto = cartService.update(cartItemId, updateRequestDto);

        //Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartItemMapper).updateFromDto(eq(updateRequestDto), any(CartItem.class));
        verify(cartItemRepository).findById(cartItemId);
        verify(cartRepository, times(2)).findByCartItemsId(cartItemId);
        verify(cartMapper).toDto(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Verify updating cart item throws exceptions")
    public void update_WithInvalidId_ShouldThrowException() {
        //Given (Arrange)
        Long invalidId = 5L;
        when(cartItemRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        //When (Act)
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cartService.update(invalidId, any(UpdateCartItemDto.class))
        );

        //Then (Assert)
        String expectedMessage = "Can't find cartItem by id " + invalidId;
        assertEquals(expectedMessage, exception.getMessage());
        verify(cartItemRepository).findById(invalidId);
    }

    @Test
    @DisplayName("Verify updating existing cart returns updated cart")
    public void addItemToCart_WithExistingCart_ShouldReturnCartDto() {
        //Given (Arrange)
        Long cartItemId = 1L;
        Long cartId = 1L;
        Long bookId = 1L;
        Long userId = 1L;

        CreateCartItemDto createRequestDto = new CreateCartItemDto()
                .setBookId(bookId)
                .setQuantity(4);

        CartItem itemToAdd = shoppingCart.getCartItems().stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow();

        when(cartRepository.findByUserId(cartId))
                .thenReturn(Optional.of(shoppingCart));

        when(cartService.findByUserId(userId))
                .thenReturn(TestDataUtil.mapToCartDto(shoppingCart));

        when(cartItemRepository.findByBookIdAndCartId(bookId, userId))
                .thenReturn(Optional.empty());

        when(cartItemMapper.createToEntity(createRequestDto)).thenReturn(itemToAdd);

        when(bookMapper.dtoToEntity(TestDataUtil.bookDto())).thenReturn(itemToAdd.getBook());

        when(bookService.findById(bookId)).thenReturn(TestDataUtil.bookDto());

        ShoppingCartDto cartDto = TestDataUtil.mapToCartDto(shoppingCart);

        when(cartMapper.toEntity(cartDto)).thenReturn(shoppingCart);

        when(cartItemRepository.save(itemToAdd)).thenReturn(itemToAdd);

        when(cartItemMapper.toDto(itemToAdd)).thenReturn(TestDataUtil.mapToCartItemDto(itemToAdd));

        ShoppingCartDto expectedDto = TestDataUtil.mapToCartDto(shoppingCart);

        //When (Act)
        ShoppingCartDto actualDto = cartService.addItemToCart(createRequestDto, userId);

        //Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Verify adding new item returns updated cart")
    public void addItemToCart_AsNewCart_ShouldReturnCartDto() {
        //Given (Arrange)
        Long cartItemId = 1L;
        Long cartId = 1L;
        Long bookId = 1L;
        Long userId = 1L;
        CreateCartItemDto createRequestDto = new CreateCartItemDto()
                .setBookId(bookId)
                .setQuantity(4);

        CartItem itemToUpdate = shoppingCart.getCartItems().stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow();

        when(cartRepository.findByUserId(cartId))
                .thenReturn(Optional.of(shoppingCart));

        when(cartService.findByUserId(userId))
                .thenReturn(TestDataUtil.mapToCartDto(shoppingCart));

        when(cartItemRepository.findByBookIdAndCartId(bookId, userId))
                .thenReturn(Optional.of(itemToUpdate));

        when(cartRepository.findByUserId(cartId))
                .thenReturn(Optional.of(shoppingCart));

        UpdateCartItemDto updateRequestDto = new UpdateCartItemDto(createRequestDto.getQuantity());

        doAnswer(invocation -> {
            CartItem arg = invocation.getArgument(1);
            arg.setQuantity(updateRequestDto.quantity());
            arg.setId(cartItemId);
            arg.setBook(itemToUpdate.getBook());
            arg.setCart(itemToUpdate.getCart());
            return arg;
        }).when(cartItemMapper).updateFromDto(eq(updateRequestDto), any(CartItem.class));

        when(cartItemRepository.save(itemToUpdate)).thenReturn(itemToUpdate);

        ShoppingCartDto expectedDto = TestDataUtil.mapToCartDto(shoppingCart);

        when(cartMapper.toDto(shoppingCart)).thenReturn(expectedDto);

        //When (Act)
        ShoppingCartDto actualDto = cartService.addItemToCart(createRequestDto, userId);

        //Then (Assert)
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(cartItemMapper).updateFromDto(eq(updateRequestDto), any(CartItem.class));
        verify(cartItemRepository).findByBookIdAndCartId(any(Long.class), any(Long.class));
        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartRepository, times(2)).findByUserId(userId);
        verify(cartMapper, times(2)).toDto(any(ShoppingCart.class));
    }
}
