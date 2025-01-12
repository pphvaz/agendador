package ai.agendi.agendador.infra.exceptions;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> tratarErro404(EntityNotFoundException ex) {
        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ResponseError> tratarErroDeProjeto(ValidacaoException ex) {
        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseError> tratarErro400(MethodArgumentNotValidException ex) {
        ResponseError response = new ResponseError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseError> tratarErroAuthentication(AuthenticationException ex) {
        ResponseError response = new ResponseError(
                "Não autorizado: " + ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> tratarErroAcessoNegado(AccessDeniedException ex) {
        ResponseError response = new ResponseError(
                "Acesso negado:" + ex.getMessage(),
                HttpStatus.FORBIDDEN,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseError> tratarErroIntegridadeNosDados(DataIntegrityViolationException ex) {
        ResponseError response = new ResponseError(
                "Erro de integridade nos dados." + ex.getMostSpecificCause().getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> tratarErro500(Exception ex) {
        ex.printStackTrace();
        ResponseError response = new ResponseError(
                "Erro: " +ex.getLocalizedMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
