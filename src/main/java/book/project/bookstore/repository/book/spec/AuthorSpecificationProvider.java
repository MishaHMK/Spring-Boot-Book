package book.project.bookstore.repository.book.spec;

import book.project.bookstore.model.Book;
import book.project.bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String key = BookParameters.AUTHOR.getValue();

    @Override
    public String getKey() {
        return key;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(key)
                .in(Arrays.stream(params).toArray());
    }
}
