package test.test.Exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors
) {
    public static ApiErrorResponse of(int status, String error, String message) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, null);
    }

    public static ApiErrorResponse ofValidation(int status, String error, String message, Map<String, String> fieldErrors) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message, fieldErrors);
    }
}
