package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.model.Cliente;

public record DadosGeraisRespostaCliente(
        String nome,
        String email,
        String cpf,
        String celular) {
    public DadosGeraisRespostaCliente(Cliente cliente) {
        this(cliente.getNome(), cliente.getEmail(), cliente.getCpf(), cliente.getCelular());
    }
}