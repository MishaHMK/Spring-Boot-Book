package book.project.bookstore.repository.cart;

import book.project.bookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci "
            + "JOIN FETCH ci.book b "
            + "JOIN FETCH ci.cart c "
            + "WHERE ci.id = :id")
    @Override
    Optional<CartItem> findById(Long id);
}
