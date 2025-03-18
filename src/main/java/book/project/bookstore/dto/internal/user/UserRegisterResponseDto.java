package book.project.bookstore.dto.internal.user;

public record UserRegisterResponseDto(Long id, String email, String firstName, String lastName,
                                      String shippingAddress) {
}
