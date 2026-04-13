package test.test.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.test.Exceptions.UserNotFoundException;
import test.test.Models.Roles;
import test.test.Models.User;
import test.test.Repositories.RoleRepository;
import test.test.Repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Transactional
    public Set<Roles> assignRolesToUser(Long userId, Set<String> roleNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Set<Roles> resolvedRoles = new HashSet<>();
        for (String roleName : roleNames) {
            resolvedRoles.add(getOrCreateRole(roleName));
        }

        user.setRoles(resolvedRoles);
        userRepository.save(user);
        return user.getRoles();
    }

    public Roles getDefaultRole() {
        return getOrCreateRole(DEFAULT_ROLE_NAME);
    }

    private Roles getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(createRole(roleName)));
    }

    private Roles createRole(String roleName) {
        Roles role = new Roles();
        role.setName(roleName);
        return role;
    }
}
