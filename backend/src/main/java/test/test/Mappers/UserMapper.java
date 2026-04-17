package test.test.Mappers;

import org.springframework.stereotype.Component;
import test.test.DTO.User.CreateUserRequest;
import test.test.DTO.User.UserResponse;
import test.test.Models.Roles;
import test.test.Models.User;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    public UserResponse toResponse(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Roles::getName)
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames
        );
    }
}
