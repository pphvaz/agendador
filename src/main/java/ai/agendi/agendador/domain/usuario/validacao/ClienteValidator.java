package ai.agendi.agendador.domain.usuario.validacao;

import ai.agendi.agendador.domain.usuario.model.Cliente;

public interface ClienteValidator {
    void validar(Cliente cliente);
}