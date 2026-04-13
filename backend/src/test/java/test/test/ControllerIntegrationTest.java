package test.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import test.test.Models.Roles;
import test.test.Models.User;
import test.test.Repositories.RoleRepository;
import test.test.Repositories.TodoRepository;
import test.test.Repositories.UserRepository;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void createUserReturnsCreatedWhenPayloadIsValid() throws Exception {
        String requestBody = """
                {
                  "name": "Alice",
                  "email": "alice@example.com",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void createUserReturnsBadRequestWhenEmailIsInvalid() throws Exception {
        String requestBody = """
                {
                  "name": "Alice",
                  "email": "wrong-email",
                  "password": "password123"
                }
                """;

        mockMvc.perform(post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email must be valid"));
    }

    @Test
    void assignRolesReturnsBadRequestWhenRolesAreEmpty() throws Exception {
        User user = userRepository.save(createUser("Alice", "alice@example.com"));

        String requestBody = """
                {
                  "roles": []
                }
                """;

        mockMvc.perform(put("/users/{id}/roles", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.roles").value("Roles must not be empty"));
    }

    @Test
    void getUserByIdReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id 999"));
    }

    @Test
    void getTodoByUserReturnsNotFoundWhenTodoDoesNotExist() throws Exception {
        User user = userRepository.save(createUser("Alice", "alice@example.com"));

        mockMvc.perform(get("/users/{userId}/todos/999", user.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Todo not found with id 999 for user " + user.getId()));
    }

    @Test
    void createTodoReturnsCreatedWhenPayloadIsValid() throws Exception {
        User user = userRepository.save(createUser("Alice", "alice@example.com"));

        String requestBody = """
                {
                  "title": "Finish API",
                  "details": "Write tests",
                  "dueDate": "2026-04-15"
                }
                """;

        mockMvc.perform(post("/users/{userId}/todos", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Finish API"))
                .andExpect(jsonPath("$.details").value("Write tests"))
                .andExpect(jsonPath("$.ownerId").value(user.getId()));
    }

    @Test
    void createTodoReturnsBadRequestWhenTitleIsBlank() throws Exception {
        User user = userRepository.save(createUser("Alice", "alice@example.com"));

        String requestBody = """
                {
                  "title": " ",
                  "details": "Write tests",
                  "dueDate": "2026-04-15"
                }
                """;

        mockMvc.perform(post("/users/{userId}/todos", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.title").value("Title is required"));
    }

    private User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("password123");
        user.setRoles(Set.of(roleRepository.save(createRole("ROLE_USER"))));
        return user;
    }

    private Roles createRole(String roleName) {
        Roles role = new Roles();
        role.setName(roleName);
        return role;
    }
}
