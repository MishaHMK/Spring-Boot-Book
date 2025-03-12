package book.project.bookstore.service;

import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.dto.internal.user.UserResponseDto;
import book.project.bookstore.exception.RegistrationException;
import book.project.bookstore.mapper.UserMapper;
import book.project.bookstore.model.User;
import book.project.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("User with email "
                    + request.getEmail() + " already exists");
        }
        User user = userMapper.toUser(request);
        return userMapper.toResponse(userRepository.save(user));
    }
}
