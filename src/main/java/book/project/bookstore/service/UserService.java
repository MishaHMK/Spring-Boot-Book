package book.project.bookstore.service;

import book.project.bookstore.dto.internal.user.UserDto;
import book.project.bookstore.dto.internal.user.UserRegisterResponseDto;
import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.exception.RegistrationException;

public interface UserService {
    UserRegisterResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;

    UserDto findByUsername(String email);
}
