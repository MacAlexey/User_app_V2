package test.test.DTO.Auth;

import lombok.Getter;

@Getter
public class RefreshTokenResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";

    public RefreshTokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
