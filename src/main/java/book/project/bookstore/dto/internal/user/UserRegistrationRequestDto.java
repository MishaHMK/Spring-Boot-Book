package book.project.bookstore.dto.internal.user;

import book.project.bookstore.annotation.FieldMatch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(first = "password", second = "confirmPassword",
        message = "Passwords must match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Password is required")
    @Length(min = 8, max = 20,
            message = "Password size must be between "
                    + "8 and 20 symbols inclusively")
    private String password;
    @NotBlank(message = "Password confirm is required")
    @Length(min = 8, max = 20,
            message = "Password size must be between "
                    + "8 and 20 symbols inclusively")
    private String confirmPassword;
    private String shippingAddress;
}
