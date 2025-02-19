package book.project.bookstore;

import book.project.bookstore.model.Book;
import book.project.bookstore.service.BookServiceImpl;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    private final BookServiceImpl bookServiceImpl;

    public BookStoreApplication(BookServiceImpl bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book();
            book1.setTitle("Book 1");
            book1.setAuthor("Author 1");
            book1.setIsbn("ISBN 1");
            book1.setPrice(new BigDecimal("100.0"));

            bookServiceImpl.save(book1);

            System.out.println(bookServiceImpl.findAll());
        };
    }

}
