package com.ameen.chat_ai.exception;

import com.ameen.chat_ai.response.ApiResponse;
import com.ameen.chat_ai.util.CommonUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResponse> handleAll(Exception ex){
        log.error("Global Exception {}", ex.getMessage());
        return CommonUtil.getResponseForError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());
    }

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiResponse> customValidationException(Exception ex){
        return CommonUtil.getResponseForError(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex){
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(constraintViolation -> constraintViolation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,(v1, v2)->v1));
        return CommonUtil.getResponseForError(HttpStatus.BAD_REQUEST.value(),
                "Validation Failed: " + errors);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""),(v1, v2)->v1));
        return CommonUtil.getResponseForError(HttpStatus.BAD_REQUEST.value(),
                "Validation Failed: " + errors);
    }

}
