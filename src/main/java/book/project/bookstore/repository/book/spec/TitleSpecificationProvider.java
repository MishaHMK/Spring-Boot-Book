package book.project.bookstore.repository.book.spec;

import book.project.bookstore.model.Book;
import book.project.bookstore.repository.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = Arrays.stream(params)
                    .map(p -> criteriaBuilder.like(root.get("title"), "%" + p + "%"))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }
}
