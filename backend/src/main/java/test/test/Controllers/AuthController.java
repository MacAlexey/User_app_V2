package test.test.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.test.DTO.Auth.RefreshTokenRequest;
import test.test.DTO.Auth.RefreshTokenResponse;
import test.test.DTO.User.CreateUserRequest;
import test.test.DTO.User.LoginRequest;
import test.test.DTO.User.LoginResponse;
import test.test.DTO.User.UserResponse;
import test.test.Models.RefreshToken;
import test.test.Models.User;
import test.test.Services.JwtService;
import test.test.Services.RefreshTokenService;
import test.test.Services.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        String newAccessToken = jwtService.generateToken(extraClaims, user.getEmail());
        String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();
        return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, newRefreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        User user = userService.findUserEntityById(userId);
        refreshTokenService.deleteByUser(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.ok(userService.getUserById(userId));
    }
}
