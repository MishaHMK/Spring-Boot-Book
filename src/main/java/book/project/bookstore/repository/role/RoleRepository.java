package book.project.bookstore.repository.role;

import book.project.bookstore.model.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByRole(Role.RoleName role);
}
