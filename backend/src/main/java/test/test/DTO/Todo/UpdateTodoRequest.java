package test.test.DTO.Todo;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTodoRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String details;
    private Boolean completed;
    private LocalDate dueDate;
}