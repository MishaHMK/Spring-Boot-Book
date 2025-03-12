package book.project.bookstore.repository.user;

import book.project.bookstore.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<Object> findByEmail(
            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format") String email);
}
