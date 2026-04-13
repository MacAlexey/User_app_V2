package test.test.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.test.DTO.User.CreateUserRequest;
import test.test.DTO.User.AssignRolesRequest;
import test.test.DTO.User.UpdateUserRequest;
import test.test.DTO.User.UserResponse;
import test.test.DTO.User.LoginRequest;
import test.test.DTO.User.LoginResponse;
import test.test.Exceptions.UserNotFoundException;
import test.test.Mappers.UserMapper;
import test.test.Models.Roles;
import test.test.Models.User;
import test.test.Repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor   // генерирует конструктор для final полей
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Roles> defaultRoles = new HashSet<>();
        defaultRoles.add(roleService.getDefaultRole());
        user.setRoles(defaultRoles);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUserById(id));
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUserById(id);
        userMapper.updateEntity(user, request);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse assignRoles(Long id, AssignRolesRequest request) {
        User user = findUserById(id);
        user.setRoles(roleService.assignRolesToUser(id, request.getRoles()));
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        userRepository.delete(findUserById(id));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Authentication authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return authentication;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticateUser(request.getEmail(), request.getPassword());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return new LoginResponse(token, "Bearer", user.getId(), user.getEmail(), user.getName());
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }
}
