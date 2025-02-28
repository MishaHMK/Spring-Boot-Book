package book.project.bookstore.repository;

import book.project.bookstore.repository.book.SearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T, P extends SearchParameters> {
    Specification<T> build(P parameters);
}
