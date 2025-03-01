package book.project.bookstore.repository.book;

import book.project.bookstore.exception.EntityNotFoundException;
import book.project.bookstore.model.Book;
import book.project.bookstore.repository.SpecificationProvider;
import book.project.bookstore.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream().filter(provider ->
                provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("No specification"
                                + " provider found for key " + key));
    }
}
