package test.test.Exceptions;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long userId, Long todoId) {
        super("Todo not found with id " + todoId + " for user " + userId);
    }
}
