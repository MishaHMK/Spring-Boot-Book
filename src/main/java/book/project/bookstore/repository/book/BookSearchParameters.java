package book.project.bookstore.repository.book;

public record BookSearchParameters(String[] titles, String[] authors)
        implements SearchParameters {}
