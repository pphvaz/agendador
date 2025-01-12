package ai.agendi.agendador.domain.usuario.validacao;

import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.infra.exceptions.ValidacaoException;

import java.time.LocalDateTime;

public class CpfUpdateValidator implements ClienteValidator {

    @Override
    public void validar(Cliente cliente) {
        if (isRecentChange(cliente.getDataAtualizacaoCpf())) {
            throw new ValidacaoException("Não é permitido alterar o CPF em um curto período de tempo.");
        }
    }

    private boolean isRecentChange(LocalDateTime dataAtualizacao) {
        return dataAtualizacao != null && dataAtualizacao.isAfter(LocalDateTime.now().minusDays(30));
    }
}