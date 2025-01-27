package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.usuario.dto.DadosAtualizacaoCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name="idUsuario")
public class Cliente extends Usuario {

    @Pattern(regexp = "\\d{11}")
    @Column(unique = true, nullable = false, updatable = false)
    private String cpf;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;

    public Cliente() {}

    public Cliente(DadosCadastroUsuario dadosUsuario, DadosCadastroCliente dadosCliente) {
        super(dadosUsuario);
        this.cpf = dadosCliente.cpf();
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public @Pattern(regexp = "\\d{11}") String getCpf() {
        return cpf;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void atualizarDadosCliente(DadosAtualizacaoCliente atualizados) {
        if (atualizados.getNovoNome() != null) {
            setNome(atualizados.getNovoNome());
        }
        if (atualizados.getNovoEmail() != null) {
            setEmail(atualizados.getNovoEmail());
        }
        if (atualizados.getNovoCelular() != null) {
            setCelular(atualizados.getNovoCelular());
        }
        if (atualizados.getNovaDataNascimento() != null) {
            setDataNascimento(atualizados.getNovaDataNascimento());
        }
    }
}