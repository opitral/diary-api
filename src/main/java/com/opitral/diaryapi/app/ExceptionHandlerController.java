package com.opitral.diaryapi.app;

import com.opitral.diaryapi.common.CommonResponse;
import com.opitral.diaryapi.exceptions.NoSuchEntityException;
import com.opitral.diaryapi.exceptions.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<CommonResponse<String>> handleNoSuchEntityException(NoSuchEntityException ex) {
        return new ResponseEntity<>(CommonResponse.error(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(CommonResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldErrors().getFirst();
        return new ResponseEntity<>(CommonResponse.error("Field '" + fieldError.getField() + "' " + fieldError.getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().iterator().next();
        return new ResponseEntity<>(CommonResponse.error("Constrains '" + constraintViolation.getPropertyPath() + "' " + constraintViolation.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // TODO: Uncomment this block after implementing security
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<CommonResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
//        return new ResponseEntity<>(CommonResponse.error("Access denied: " + ex.getMessage()), HttpStatus.FORBIDDEN);
//    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CommonResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(CommonResponse.error("Resource not found: " + ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<String>> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(CommonResponse.error("Method not allowed: " + ex.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<String>> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(CommonResponse.error("Malformed JSON request: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<String>> handleGenericException() {
        return new ResponseEntity<>(CommonResponse.error("Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}