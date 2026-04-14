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
import test.test.Exceptions.EmailAlreadyExistsException;
import test.test.Exceptions.UserNotFoundException;
import test.test.Mappers.UserMapper;
import test.test.Models.Roles;
import test.test.Models.User;
import test.test.Repositories.UserRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
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

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUserEntityById(id));
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findUserEntityById(id);
        boolean emailChanged = !user.getEmail().equals(request.getEmail());
        if (emailChanged && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }
        userMapper.updateEntity(user, request);
        UserResponse response = userMapper.toResponse(userRepository.save(user));
        if (emailChanged) {
            refreshTokenService.deleteByUser(user);
        }
        return response;
    }

    @Transactional
    public UserResponse assignRoles(Long id, AssignRolesRequest request) {
        User user = findUserEntityById(id);
        user.setRoles(roleService.assignRolesToUser(id, request.getRoles()));
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(findUserEntityById(id));
    }

    @Transactional(readOnly = true)
    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        String accessToken = jwtService.generateToken(extraClaims, userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();
        return new LoginResponse(accessToken, refreshToken, "Bearer", user.getId(), user.getEmail(), user.getName());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return userMapper.toResponse(user);
    }
}
