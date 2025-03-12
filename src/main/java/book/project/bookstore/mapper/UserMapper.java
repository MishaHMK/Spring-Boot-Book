package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.dto.internal.user.UserResponseDto;
import book.project.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto dto);

    UserResponseDto toResponse(User user);
}
