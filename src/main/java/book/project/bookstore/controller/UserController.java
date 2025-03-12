package book.project.bookstore.controller;

import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.dto.internal.user.UserResponseDto;
import book.project.bookstore.service.UserService;
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
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register user",
            description = "Create a new user with provided data")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
