package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.*;

public record DadosCadastroUsuario(

        @NotEmpty
        @Size(min = 8)
        String senha,

        @Email
        @NotEmpty
        String email,

        @NotEmpty
        @Pattern(regexp = "^\\d{10,11}$", message = "O celular deve conter entre 10 e 11 dígitos.")
        String celular) {
}