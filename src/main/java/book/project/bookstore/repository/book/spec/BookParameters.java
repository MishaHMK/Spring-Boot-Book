package book.project.bookstore.repository.book.spec;

import lombok.Getter;

@Getter
public enum BookParameters {
    TITLE("title"),
    AUTHOR("author");

    private final String value;

    BookParameters(String value) {
        this.value = value;
    }
}
