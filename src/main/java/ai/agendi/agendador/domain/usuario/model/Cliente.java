package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.endereco.model.Endereco;
import ai.agendi.agendador.domain.usuario.dto.DadosAtualizacaoCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
}