package ai.agendi.agendador.domain.agendamento.model;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.estabelecimento.model.Servico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "combos")
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nome;

    @NotNull
    private BigDecimal precoPromocional;

    private LocalDateTime dataInicioPromocao;

    private LocalDateTime dataFimPromocao;

    private boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estabelecimento_id")
    @NotNull
    private Estabelecimento estabelecimento;

    @OneToMany
    @JoinTable(
            name = "combo_servico",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id")
    )
    @NotNull
    private List<Servico> servicosIncluidos;

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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

    public BigDecimal getPrecoPromocional() {
        return precoPromocional;
    }

    public void setPrecoPromocional(BigDecimal precoPromocional) {
        this.precoPromocional = precoPromocional;
    }

    public LocalDateTime getDataInicioPromocao() {
        return dataInicioPromocao;
    }

    public void setDataInicioPromocao(LocalDateTime dataInicioPromocao) {
        this.dataInicioPromocao = dataInicioPromocao;
    }

    public LocalDateTime getDataFimPromocao() {
        return dataFimPromocao;
    }

    public void setDataFimPromocao(LocalDateTime dataFimPromocao) {
        this.dataFimPromocao = dataFimPromocao;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public List<Servico> getServicosIncluidos() {
        return servicosIncluidos;
    }

    public void setServicosIncluidos(List<Servico> servicosIncluidos) {
        this.servicosIncluidos = servicosIncluidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Combo combo = (Combo) o;
        return Objects.equals(getId(), combo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
