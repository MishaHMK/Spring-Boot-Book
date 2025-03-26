package book.project.bookstore.repository.order;

import book.project.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = "order.user")
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long id, Long orderId, Long userId);

    @EntityGraph(attributePaths = "order.user")
    List<OrderItem> findAllByOrderIdAndOrderUserId(Long orderId, Long userId, Pageable pageable);
}
