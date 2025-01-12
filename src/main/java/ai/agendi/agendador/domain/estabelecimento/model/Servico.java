package ai.agendi.agendador.domain.estabelecimento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String nome;

    @NotEmpty
    @Column(nullable = false)
    private String descricao;

    @NotNull
    private BigDecimal preco ;
    @NotNull
    @Column(nullable = false)
    private Integer duracaoEmMinutos;
    private boolean ativo;

    private BigDecimal precoNaPromocao;
    private LocalDateTime promocaoInicio;
    private LocalDateTime promocaoFim;

    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoPrecos> historicoPreco;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id", nullable = false)
    private Estabelecimento estabelecimento;

    public boolean estaEmPromocao() {
        return promocaoInicio != null &&
                promocaoFim != null &&
                LocalDateTime.now().isAfter(promocaoInicio) &&
                LocalDateTime.now().isBefore(promocaoFim);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getPrecoNaPromocao() {
        return precoNaPromocao;
    }

    public void setPrecoNaPromocao(BigDecimal precoNaPromocao) {
        this.precoNaPromocao = precoNaPromocao;
    }

    public Integer getDuracaoEmMinutos() {
        return duracaoEmMinutos;
    }

    public void setDuracaoEmMinutos(Integer duracaoEmMinutos) {
        this.duracaoEmMinutos = duracaoEmMinutos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getPromocaoInicio() {
        return promocaoInicio;
    }

    public void setPromocaoInicio(LocalDateTime promocaoInicio) {
        this.promocaoInicio = promocaoInicio;
    }

    public LocalDateTime getPromocaoFim() {
        return promocaoFim;
    }

    public void setPromocaoFim(LocalDateTime promocaoFim) {
        this.promocaoFim = promocaoFim;
    }

    public List<HistoricoPrecos> getHistoricoPreco() {
        return historicoPreco;
    }

    public void setHistoricoPreco(List<HistoricoPrecos> historicoPreco) {
        this.historicoPreco = historicoPreco;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;
        return Objects.equals(getId(), servico.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
