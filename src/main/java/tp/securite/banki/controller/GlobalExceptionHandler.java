package tp.securite.banki.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tp.securite.banki.exceptions.BusinessException;
import tp.securite.banki.exceptions.ErrorCode;
import tp.securite.banki.model.ErrorResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        log.error("Caught BusinessException");
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(exception.getMessage())
                .code(exception.getErrorCode().getCode())
                .build();
        return ResponseEntity.status(
                        exception.getErrorCode().getStatus() != null ? exception.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST
                )
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.info("Constraint violation occurred: {}", ex.getMessage());
        List<ErrorResponse.ValidationError> validationErrors = ex.getConstraintViolations().stream()
                .map(violation -> ErrorResponse.ValidationError.builder()
                        .field(violation.getPropertyPath().toString())
                        .code(violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName())
                        .message(violation.getMessage())
                        .build())
                .toList();
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Constraint validation failed")
                .code(ErrorCode.CONSTRAINT_VIOLATION.getCode())
                .validationErrors(validationErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.info("Method argument type mismatch occurred: {}", ex.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                        ex.getValue(), ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"))
                .code(ErrorCode.METHOD_ARGUMENT_TYPE_MISMATCH.getCode())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.info("Validation exception occurred: {}", ex.getMessage());
        List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> ErrorResponse.ValidationError.builder()
                        .field(fieldError.getField())
                        .code(fieldError.getCode())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .code(ErrorCode.VALIDATION_FAILED.getCode())
                .validationErrors(validationErrors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.debug("Username not found exception occurred: {}", ex.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .build();
        return ResponseEntity.status(ErrorCode.USERNAME_NOT_FOUND.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .code(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
