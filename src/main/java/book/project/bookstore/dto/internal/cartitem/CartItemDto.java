package book.project.bookstore.dto.internal.cartitem;

public record CartItemDto(Long id, Long bookId,
                          String bookTitle, int quantity) {
}
