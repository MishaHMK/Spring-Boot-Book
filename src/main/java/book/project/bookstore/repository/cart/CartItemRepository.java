package book.project.bookstore.repository.cart;

import book.project.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBookIdAndCartId(Long bookId, Long cartId);
}
