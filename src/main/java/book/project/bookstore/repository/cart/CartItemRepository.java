package book.project.bookstore.repository.cart;

import book.project.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"book", "cart", "cart.user"})
    Optional<CartItem> findByBookIdAndCartId(Long bookId, Long cartId);

    @EntityGraph(attributePaths = {"book", "cart"})
    @Override
    Optional<CartItem> findById(Long id);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.id = :cartId")
    void deleteAllByCartId(Long cartId);
}
