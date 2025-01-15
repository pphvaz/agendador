package ai.agendi.agendador.infra.exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> tratarErro404(EntityNotFoundException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now(),
                request.getRequestURI(), // Retrieve the path of the request
                "ENTITY_NOT_FOUND" // Custom error code
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ResponseError> tratarErroDeProjeto(ValidacaoException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                request.getRequestURI(),
                "VALIDATION_ERROR"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseError> tratarErro400(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        String message = ex instanceof MethodArgumentNotValidException
                ? "Validation error: invalid or missing fields."
                : "Malformed request body or unreadable content.";

        ResponseError response = new ResponseError(
                message,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                request.getRequestURI(), // Retrieve the path of the request
                ex instanceof MethodArgumentNotValidException ? "VALIDATION_ERROR" : "MALFORMED_REQUEST" // Custom error code
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseError> tratarErroAuthentication(AuthenticationException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                "Não autorizado: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now(),
                request.getRequestURI(),
                "UNAUTHORIZED"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> tratarErroAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                "Acesso negado: " + ex.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now(),
                request.getRequestURI(),
                "ACCESS_DENIED"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseError> tratarErroIntegridadeNosDados(DataIntegrityViolationException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                "Erro de integridade nos dados: " + ex.getMostSpecificCause().getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now(),
                request.getRequestURI(),
                "DATA_INTEGRITY_VIOLATION"
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> tratarErroBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ResponseError response = new ResponseError(
                "Usuário ou senha inválidos.",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                request.getRequestURI(),
                "BAD_CREDENTIALS"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseError> tratarErro500(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        ResponseError response = new ResponseError(
                "Erro: " + ex.getLocalizedMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now(),
                request.getRequestURI(),
                "INTERNAL_SERVER_ERROR"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
