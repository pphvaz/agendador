package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DadosCadastroPrestador(

        @NotNull
        DadosCadastroUsuario dadosCadastroUsuario,

        @NotEmpty
        String nome,

        @NotNull
        LocalDate dataNascimento,

        Integer anosDeExperiencia,

        Long idEstabelecimento) {
}