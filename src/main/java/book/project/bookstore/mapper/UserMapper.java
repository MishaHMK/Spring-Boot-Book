package book.project.bookstore.mapper;

import book.project.bookstore.config.MapperConfig;
import book.project.bookstore.dto.internal.user.UserDto;
import book.project.bookstore.dto.internal.user.UserRegisterResponseDto;
import book.project.bookstore.dto.internal.user.UserRegistrationRequestDto;
import book.project.bookstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRegistrationRequestDto dto);

    UserDto toUserDto(User user);

    UserRegisterResponseDto toResponse(User user);

    @Named("userFromId")
    default User userFromId(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}
