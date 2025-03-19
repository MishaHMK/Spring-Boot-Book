package book.project.bookstore.repository.book;

import book.project.bookstore.model.Book;
import book.project.bookstore.model.Category;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "categories")
    @Override
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    @Override
    Optional<Book> findById(Long id);

    Page<Book> findByCategoriesContaining(Category category, Pageable pageable);
}
