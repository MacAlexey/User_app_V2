package test.test.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import test.test.DTO.Todo.CreateTodoRequest;
import test.test.DTO.Todo.TodoResponse;
import test.test.DTO.Todo.UpdateTodoRequest;
import test.test.Exceptions.TodoNotFoundException;
import test.test.Exceptions.UserNotFoundException;
import test.test.Mappers.TodoMapper;
import test.test.Models.Todo;
import test.test.Models.User;
import test.test.Repositories.TodoRepository;
import test.test.Repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TodoMapper todoMapper;

    public List<TodoResponse> getTodosByUser(Long userId) {
        ensureUserExists(userId);
        return todoRepository.findByOwnerId(userId).stream()
                .map(todoMapper::toResponse)
                .toList();
    }

    public TodoResponse getTodoByUser(Long userId, Long todoId) {
        return todoMapper.toResponse(findOwnedTodo(userId, todoId));
    }

    public TodoResponse createTodo(Long userId, CreateTodoRequest request) {
        User owner = ensureUserExists(userId);
        Todo todo = todoMapper.toEntity(request);
        todo.setOwner(owner);
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(Long userId, Long todoId, UpdateTodoRequest request) {
        Todo todo = findOwnedTodo(userId, todoId);
        todoMapper.updateEntity(todo, request);
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    public void deleteTodo(Long userId, Long todoId) {
        Todo todo = findOwnedTodo(userId, todoId);
        todoRepository.delete(todo);
    }

    private User ensureUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Todo findOwnedTodo(Long userId, Long todoId) {
        ensureUserExists(userId);
        return todoRepository.findByIdAndOwnerId(todoId, userId)
                .orElseThrow(() -> new TodoNotFoundException(userId, todoId));
    }
}
