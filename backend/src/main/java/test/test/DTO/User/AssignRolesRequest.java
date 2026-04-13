package test.test.DTO.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AssignRolesRequest {
    @NotEmpty(message = "Roles must not be empty")
    private Set<String> roles;
}