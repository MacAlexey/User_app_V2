package test.test.Mappers;

import org.springframework.stereotype.Component;
import test.test.DTO.Todo.CreateTodoRequest;
import test.test.DTO.Todo.TodoResponse;
import test.test.DTO.Todo.UpdateTodoRequest;
import test.test.Models.ToDo;

@Component
public class TodoMapper {

    public ToDo toEntity(CreateTodoRequest request) {
        ToDo todo = new ToDo();
        todo.setTitle(request.getTitle());
        todo.setDetails(request.getDetails());
        todo.setDueDate(request.getDueDate());
        return todo;
    }

    public void updateEntity(ToDo todo, UpdateTodoRequest request) {
        todo.setTitle(request.getTitle());
        todo.setDetails(request.getDetails());
        todo.setCompleted(request.getCompleted());
        todo.setDueDate(request.getDueDate());
    }

    public TodoResponse toResponse(ToDo todo) {
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
