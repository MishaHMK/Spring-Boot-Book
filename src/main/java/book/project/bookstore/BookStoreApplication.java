package book.project.bookstore;

import book.project.bookstore.model.Book;
import book.project.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    private final BookService bookService;

    public BookStoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Book 1");
            book.setAuthor("Author 1");
            book.setIsbn("ISBN 1");
            book.setPrice(new BigDecimal("100.0"));

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }

}
