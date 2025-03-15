package book.project.bookstore.service;

import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.dto.internal.user.UserResponseDto;
import book.project.bookstore.exception.RegistrationException;
import book.project.bookstore.mapper.UserMapper;
import book.project.bookstore.model.Role;
import book.project.bookstore.model.User;
import book.project.bookstore.repository.role.RoleRepository;
import book.project.bookstore.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with email "
                    + request.getEmail() + " already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findByRole(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        return userMapper.toResponse(userRepository.save(user));
    }
}
