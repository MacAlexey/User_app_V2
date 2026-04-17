package test.test.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import test.test.DTO.User.AssignRolesRequest;
import test.test.DTO.User.UpdateEmailRequest;
import test.test.DTO.User.UpdateNameRequest;
import test.test.DTO.User.UpdatePasswordRequest;
import test.test.DTO.User.UserResponse;
import test.test.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}/email")
    public ResponseEntity<UserResponse> updateEmail(@PathVariable Long id, @Valid @RequestBody UpdateEmailRequest request) {
        return ResponseEntity.ok(userService.updateEmail(id, request));
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}/name")
    public ResponseEntity<UserResponse> updateName(@PathVariable Long id, @Valid @RequestBody UpdateNameRequest request) {
        return ResponseEntity.ok(userService.updateName(id, request));
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserResponse> assignRoles(@PathVariable Long id, @Valid @RequestBody AssignRolesRequest request) {
        return ResponseEntity.ok(userService.assignRoles(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}