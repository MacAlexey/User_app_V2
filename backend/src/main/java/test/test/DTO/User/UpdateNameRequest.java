package test.test.DTO.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNameRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
