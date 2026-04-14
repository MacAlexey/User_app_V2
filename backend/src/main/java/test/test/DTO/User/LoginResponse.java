package test.test.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private String type;
    private Long id;
    private String email;
    private String name;
}
