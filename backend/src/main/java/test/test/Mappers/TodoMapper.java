package test.test.Mappers;

import org.springframework.stereotype.Component;
import test.test.DTO.Todo.CreateTodoRequest;
import test.test.DTO.Todo.TodoResponse;
import test.test.DTO.Todo.UpdateTodoRequest;
import test.test.Models.Todo;

@Component
public class TodoMapper {

    public Todo toEntity(CreateTodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setDetails(request.getDetails());
        todo.setDueDate(request.getDueDate());
        return todo;
    }

    public void updateEntity(Todo todo, UpdateTodoRequest request) {
        todo.setTitle(request.getTitle());
        todo.setDetails(request.getDetails());
        todo.setCompleted(request.getCompleted());
        todo.setDueDate(request.getDueDate());
    }

    public TodoResponse toResponse(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDetails(),
                todo.getCompleted(),
                todo.getDueDate(),
                todo.getCreatedAt(),
                todo.getUpdatedAt(),
                todo.getOwner() != null ? todo.getOwner().getId() : null
        );
    }
}
