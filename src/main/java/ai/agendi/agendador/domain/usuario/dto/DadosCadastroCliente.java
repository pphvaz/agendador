package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record DadosCadastroCliente(
        @NotNull
        @Valid
        DadosCadastroUsuario dadosCadastroUsuario,

        @NotEmpty
        String nome,

        @NotNull
        LocalDate dataNascimento,

        @NotEmpty
        @CPF
        String cpf) {
}