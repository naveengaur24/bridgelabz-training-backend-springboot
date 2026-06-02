package com.fundoonotes.fundoo_notes.exception;
import com.fundoonotes.fundoo_notes.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// Kisi bhi controller mein error aaye ye handle karega
@RestControllerAdvice
public class GlobalExceptionHandler {

    // User nahi mila
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleUserNotFound(
            UserNotFoundException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Note nahi mili
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleNoteNotFound(
            NoteNotFoundException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Wrong URL hit karo — 404
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleNoResourceFound(
            NoResourceFoundException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                "URL not found: " + ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // Client ne invalid data bheja
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalArgument(
            IllegalArgumentException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Null object pe method call kiya
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleNullPointer(
            NullPointerException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                "Something went wrong!",
                null
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    // Token nahi hai ya invalid hai — 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAuthException(
            AuthenticationException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                "Unauthorized! Please login first.",
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // Token hai lekin access nahi — 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAccessDenied(
            AccessDeniedException ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                "Access Denied! You don't have permission.",
                null
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    // Validation fail
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Validation ka result
        // Saari field errors ki list
        // List ka pehla error
        // Error ka message
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                errorMessage,
                null
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    // Network issue, DB down, koi bhi unexpected error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAllExceptions(
            Exception ex) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>(
                false,
                "Internal server error: " + ex.getMessage(),
                null
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}