package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.user.UserLoginRequestDto;
import book.project.bookstore.dto.internal.user.UserLoginResponseDto;
import book.project.bookstore.dto.internal.user.UserRegisterResponseDto;
import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.security.AuthenticationService;
import book.project.bookstore.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Management", description = "Endpoints for managing user data")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationService authService;

    @PostMapping("/registration")
    @Operation(summary = "Register user",
            description = "Create a new user with provided data")
    public UserRegisterResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user",
            description = "Login with provided data and receive token")
    public UserLoginResponseDto login(
            @Valid @RequestBody UserLoginRequestDto requestDto) {
        return authService.login(requestDto);
    }
}
