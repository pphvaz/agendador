package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.model.Cliente;

import java.math.BigDecimal;

public record DadosRespostaCliente(Long id, String nome, String email, String cpf, BigDecimal saldo) {
    public DadosRespostaCliente(Cliente cliente) {
        this(cliente.getIdUsuario(), cliente.getNome(), cliente.getEmail(), cliente.getCpf(), cliente.getSaldo());
    }
}