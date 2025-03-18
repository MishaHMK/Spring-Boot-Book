package book.project.bookstore.security;

import book.project.bookstore.dto.internal.user.UserLoginRequestDto;
import book.project.bookstore.dto.internal.user.UserLoginResponseDto;

public interface AuthenticationService {
    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto);
}
