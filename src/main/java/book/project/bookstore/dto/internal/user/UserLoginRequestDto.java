package book.project.bookstore.dto.internal.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotEmpty(message = "Email is required")
        @Length(min = 8, max = 30, message = "Email size must be between "
            + "8 and 30 symbols inclusively")
        @Email(message = "Invalid email format")
        String email,
        @NotEmpty
        @Length(min = 8, max = 20)
        String password) {

}
