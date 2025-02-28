package book.project.bookstore.repository;

import org.springframework.stereotype.Component;

@Component
public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
