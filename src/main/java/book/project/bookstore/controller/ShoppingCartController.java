package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.cart.ShoppingCartDto;
import book.project.bookstore.dto.internal.cartitem.CreateCartItemDto;
import book.project.bookstore.dto.internal.cartitem.UpdateCartItemDto;
import book.project.bookstore.security.SecurityUtil;
import book.project.bookstore.service.cart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart Management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get user cart",
            description = "Get cart of currently authorized user")
    public ShoppingCartDto getCart() {
        Long userId = SecurityUtil.getLoggedInUserId();
        return cartService.findByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Add item to cart",
            description = "Add book to the shopping cart")
    public ShoppingCartDto addItem(@Valid @RequestBody CreateCartItemDto itemDto) {
        Long userId = SecurityUtil.getLoggedInUserId();
        return cartService.addItemToCart(itemDto, userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("cart-items/{cartItemId}")
    @Operation(summary = "Change cart item quantity",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartDto updateItem(@PathVariable Long cartItemId,
                                  @Valid @RequestBody UpdateCartItemDto updateCartItemDto) {
        return cartService.update(cartItemId, updateCartItemDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item by id",
            description = "Remove an item from the shopping cart")
    public void deleteItemById(@PathVariable Long cartItemId) {
        cartService.deleteById(cartItemId);
    }
}
