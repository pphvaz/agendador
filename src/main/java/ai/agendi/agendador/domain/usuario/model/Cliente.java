package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name="idUsuario")
public class Cliente extends Usuario {

    private String nome;
    private LocalDate dataNascimento;

    @Pattern(regexp = "\\d{11}")
    @Column(unique = true)
    private String cpf;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal saldo;

    public Cliente() {}

    public Cliente (DadosCadastroCliente dadosCliente) {
        super(dadosCliente.dadosCadastroUsuario());
        this.nome = dadosCliente.nome();
        this.dataNascimento = dadosCliente.dataNascimento();
        this.cpf = dadosCliente.cpf();
        this.saldo = BigDecimal.ZERO;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public @Pattern(regexp = "\\d{11}") String getCpf() {
        return cpf;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}