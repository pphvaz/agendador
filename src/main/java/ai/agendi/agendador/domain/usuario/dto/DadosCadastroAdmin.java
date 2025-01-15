package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosCadastroAdmin(
        @NotNull
        String email,
        @NotNull
        String senha,
        @NotNull
        String celular,
        @NotNull
        String nome,
        @NotNull
        LocalDate dataNascimento) {
}
