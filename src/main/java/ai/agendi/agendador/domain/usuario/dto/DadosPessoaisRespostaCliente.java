package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.model.Cliente;

import java.math.BigDecimal;

public record DadosPessoaisRespostaCliente(
        Long id,
        String nome,
        String email,
        String cpf,
        String celular,
        BigDecimal saldo
        ) {
    public DadosPessoaisRespostaCliente(Cliente cliente) {
        this(cliente.getId() ,cliente.getNome(), cliente.getEmail(), cliente.getCpf(), cliente.getCelular(), cliente.getSaldo());
    }
}