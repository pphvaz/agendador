package ai.agendi.agendador.infra.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(String mensagem, HttpStatus httpStatus, LocalDateTime timestamp, String path, String errorCode) {
}
