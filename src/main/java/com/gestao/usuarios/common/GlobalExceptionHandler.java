package com.gestao.usuarios.common;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> onIntegrity(DataIntegrityViolationException ex) {
        Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
        String rootMsg = (root != null ? String.valueOf(root.getMessage()) : "")
                .toLowerCase(Locale.ROOT);

        boolean looksLikeUnique = rootMsg.contains("unique")
                || rootMsg.contains("duplicate")
                || rootMsg.contains("uq_")
                || rootMsg.contains("uk_")
                || rootMsg.contains("constraint")
                || rootMsg.contains("duplicate key");

        boolean mentionsEmail = rootMsg.contains("email");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 409);

        if (looksLikeUnique && mentionsEmail) {
            body.put("code", "EMAIL_TAKEN");
            body.put("error", "EMAIL_TAKEN");
            body.put("message", "E-mail já está em uso.");
        } else {
            body.put("code", "CONSTRAINT_VIOLATION");
            body.put("error", "CONSTRAINT_VIOLATION");
            body.put("message", "Violação de integridade. Verifique campos únicos.");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> onValidation(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fe -> {
            Map<String, Object> e = new LinkedHashMap<>();
            e.put("field", fe.getField());
            e.put("message", String.valueOf(fe.getDefaultMessage()));
            errors.add(e);
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 400);
        body.put("code", "VALIDATION_ERROR");
        body.put("error", "VALIDATION_ERROR");
        body.put("fields", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> onNotFound(NoSuchElementException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 404);
        body.put("code", "NOT_FOUND");
        body.put("error", "NOT_FOUND");
        body.put("message", "Recurso não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> onUnexpected(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 500);
        body.put("code", "UNEXPECTED_ERROR");
        body.put("error", "UNEXPECTED_ERROR");
        body.put("message", "Erro inesperado. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
