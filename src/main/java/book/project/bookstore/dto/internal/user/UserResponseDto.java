package book.project.bookstore.dto.internal.user;

public record UserResponseDto(Long id, String email, String firstName, String lastName,
                              String shippingAddress) {
}
