package ai.agendi.agendador.domain.usuario.validacao;

import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.infra.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NascimentoUpdateValidator implements ClienteValidator {

    @Override
    public void validar(Cliente cliente) {
        if (isRecentChange(cliente.getDataAtualizacaoNascimento())) {
            throw new ValidacaoException("Não é permitido alterar a data de nascimento em um curto período de tempo.");
        }
    }

    private boolean isRecentChange(LocalDateTime dataAtualizacao) {
        return dataAtualizacao != null && dataAtualizacao.isAfter(LocalDateTime.now().minusDays(30));
    }
}