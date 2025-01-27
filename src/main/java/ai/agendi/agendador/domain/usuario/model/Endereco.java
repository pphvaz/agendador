package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroEndereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="enderecos_residenciais")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String cep;

    @NotEmpty
    @Column(nullable = false)
    private String logradouro;

    @NotEmpty
    @Column(nullable = false)
    private String numero;

    @NotEmpty
    @Column(nullable = false)
    private String bairro;

    @NotEmpty
    @Column(nullable = false)
    private String cidade;

    @NotEmpty
    @Column(nullable = false)
    private String uf;
    private String complemento;
    private boolean ativo;
    private LocalDateTime created_at;
    private LocalDateTime deleted_at;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "endereco")
    private Cliente cliente;

    @OneToOne(fetch = FetchType.LAZY)
    private Estabelecimento estabelecimento;

    public Endereco(DadosCadastroEndereco dadosEndereco) {
        this.cep = dadosEndereco.cep();
        this.logradouro = dadosEndereco.logradouro();
        this.numero = dadosEndereco.numero();
        this.bairro = dadosEndereco.bairro();
        this.cidade = dadosEndereco.cidade();
        this.uf = dadosEndereco.uf();
        this.complemento = dadosEndereco.complemento();
        this.ativo = true;
        this.created_at = LocalDateTime.now();
        if (dadosEndereco.cliente() != null) {
            this.cliente = dadosEndereco.cliente();
        }
        else if (dadosEndereco.estabelecimento() != null) {
            this.estabelecimento = dadosEndereco.estabelecimento();
        }
    }

    public Endereco() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataExclusao() {
        return deleted_at;
    }

    public void setDataExclusao(LocalDateTime dataExclusao) {
        this.deleted_at = dataExclusao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    @Override
    public String toString() {
        StringBuilder enderecoFormatado = new StringBuilder();
        enderecoFormatado.append(logradouro)
                .append(", ")
                .append(numero);
        if (complemento != null && !complemento.isEmpty()) {
            enderecoFormatado.append(" - ").append(complemento);
        }
        enderecoFormatado.append(", ")
                .append(bairro)
                .append(", ")
                .append(cidade)
                .append(" - ")
                .append(uf)
                .append(", CEP: ")
                .append(cep);
        return enderecoFormatado.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(getId(), endereco.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
