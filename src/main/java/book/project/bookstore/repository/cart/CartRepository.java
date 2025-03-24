package book.project.bookstore.repository.cart;

import book.project.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book", "user"})
    Optional<ShoppingCart> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findByCartItemsId(Long cartItemId);
}
