package com.example.demo.infra.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Trata erros de validação em @RequestBody (DTOs)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", messageSource.getMessage("validation.error.title", null, LocaleContextHolder.getLocale()));
        response.put("errors", errors);
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata erros de validação em @PathVariable e @RequestParam
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", messageSource.getMessage("validation.params.error.title", null, LocaleContextHolder.getLocale()));
        response.put("errors", errors);
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções de recurso não encontrado
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Trata RuntimeException (usado nos services quando recurso não é encontrado)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        
        // Verifica se é uma exceção de recurso não encontrado
        if (ex.getMessage() != null && (ex.getMessage().contains("não encontrado") || ex.getMessage().contains("not found"))) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", ex.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        // Outras RuntimeExceptions
        response.put("status", HttpStatus.BAD_REQUEST.value());
        String defaultMessage = messageSource.getMessage("error.processing.request", null, LocaleContextHolder.getLocale());
        response.put("message", ex.getMessage() != null ? ex.getMessage() : defaultMessage);
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata IllegalArgumentException (usado para erros de validação de parâmetros como paginação)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        
        // Tenta traduzir mensagens comuns de paginação
        String message = ex.getMessage();
        if (message != null) {
            if (message.contains("Page number")) {
                message = messageSource.getMessage("pagination.invalid.page", null, LocaleContextHolder.getLocale());
            } else if (message.contains("Page size")) {
                message = messageSource.getMessage("pagination.invalid.size", null, LocaleContextHolder.getLocale());
            }
        }
        
        response.put("message", message != null ? message : messageSource.getMessage("error.processing.request", null, LocaleContextHolder.getLocale()));
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções de autenticação (credenciais inválidas)
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("message", messageSource.getMessage("auth.invalid.credentials", null, LocaleContextHolder.getLocale()));
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Trata outras exceções de autenticação
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, Object> response = new HashMap<>();
        String authError = messageSource.getMessage("auth.error", null, LocaleContextHolder.getLocale());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("message", authError + ": " + ex.getMessage());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Trata todas as outras exceções não tratadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", messageSource.getMessage("error.internal.server", null, LocaleContextHolder.getLocale()));
        response.put("error", ex.getMessage());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        // Em produção, não exponha detalhes da exceção
        ex.printStackTrace(); // Log do erro
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

