package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record DadosCadastroCliente(
        @Email
        @NotEmpty
        String email,

        @NotEmpty
        @Size(min = 8)
        String senha,

        @NotEmpty
        @Pattern(regexp = "^\\d{10,11}$", message = "O celular deve conter entre 10 e 11 dígitos.")
        String celular,

        @NotEmpty
        String nome,

        @NotNull
        LocalDate dataNascimento,

        @NotEmpty
        @CPF
        String cpf) {
}