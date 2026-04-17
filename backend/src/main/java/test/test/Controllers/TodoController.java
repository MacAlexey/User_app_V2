package test.test.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import test.test.DTO.Todo.CreateTodoRequest;
import test.test.DTO.Todo.TodoResponse;
import test.test.DTO.Todo.UpdateTodoRequest;
import test.test.Services.TodoService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{userId}/todos")
@RequiredArgsConstructor
@PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<TodoResponse> getTodosByUser(@PathVariable Long userId) {
        return todoService.getTodosByUser(userId);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodoByUser(
            @PathVariable Long userId,
            @PathVariable Long todoId
    ) {
        return ResponseEntity.ok(todoService.getTodoByUser(userId, todoId));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @PathVariable Long userId,
            @Valid @RequestBody CreateTodoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(userId, request));
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long userId,
            @PathVariable Long todoId,
            @Valid @RequestBody UpdateTodoRequest request
    ) {
        return ResponseEntity.ok(todoService.updateTodo(userId, todoId, request));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long userId,
            @PathVariable Long todoId
    ) {
        todoService.deleteTodo(userId, todoId);
        return ResponseEntity.noContent().build();
    }
}
