package book.project.bookstore.repository.book;

import book.project.bookstore.model.Book;
import book.project.bookstore.repository.SpecificationBuilder;
import book.project.bookstore.repository.SpecificationProviderManager;
import book.project.bookstore.repository.book.spec.BookParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book, BookSearchParameters> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookParameters.TITLE.getValue())
                    .getSpecification(searchParameters.titles()));
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookParameters.AUTHOR.getValue())
                    .getSpecification(searchParameters.authors()));
        }

        return spec;
    }
}
