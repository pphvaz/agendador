package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty
        String email,
        @NotEmpty
        String senha) {
}
