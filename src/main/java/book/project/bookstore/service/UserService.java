package book.project.bookstore.service;

import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.dto.internal.user.UserResponseDto;
import book.project.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;

    void deleteById(Long id);
}
