package book.project.bookstore.repository.cart;

import book.project.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = "cartItems")
    @Query("SELECT sc FROM ShoppingCart sc JOIN sc.user u WHERE u.id = :id")
    Optional<ShoppingCart> findByUserId(Long id);
}
