package com.parking.parking_system.support.configuration.exception;

import com.parking.parking_system.support.eccezioni.OperazioneNonConsentitaException;
import com.parking.parking_system.support.eccezioni.RisorsaDuplicataException;
import com.parking.parking_system.support.eccezioni.RisorsaNonTrovataException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return build(HttpStatus.BAD_REQUEST, "Errore di validazione", "Controlla i campi inviati", errors);
    }

    @ExceptionHandler(RisorsaNonTrovataException.class)
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getClass().getSimpleName(), ex.getMessage(), Map.of());
    }

    @ExceptionHandler(RisorsaDuplicataException.class)
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return build(HttpStatus.CONFLICT, ex.getClass().getSimpleName(), ex.getMessage(), Map.of());
    }

    @ExceptionHandler(OperazioneNonConsentitaException.class)
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), ex.getMessage(), Map.of());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(
                HttpStatus.CONFLICT,
                "Vincolo database",
                "I dati inseriti violano un vincolo del database. Controlla che email, codice fiscale e targa non siano già presenti.",
                Map.of()
        );
    }

    @ExceptionHandler({
            OptimisticLockingFailureException.class,
            PessimisticLockingFailureException.class,
            CannotAcquireLockException.class
    })
    public ResponseEntity<ApiError> handleConcurrency(RuntimeException ex) {
        return build(
                HttpStatus.CONFLICT,
                "Conflitto di concorrenza",
                "Un'altra operazione sta modificando la stessa risorsa. Riprova.",
                Map.of()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Accesso negato", "Non hai i permessi per questa operazione", Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getClass().getSimpleName(), ex.getMessage(), Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Errore interno",
                "Si è verificato un errore imprevisto sul server",
                Map.of()
        );
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String error, String message, Map<String, String> details) {
        return ResponseEntity.status(status).body(new ApiError(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                details
        ));
    }
}
