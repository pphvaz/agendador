package ai.agendi.agendador.domain.estabelecimento.dto;

import jakarta.validation.constraints.NotEmpty;

public record DadosEstabelecimento(
        @NotEmpty String nome) {
}