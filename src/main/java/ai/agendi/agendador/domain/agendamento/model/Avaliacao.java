package ai.agendi.agendador.domain.agendamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataAvaliacao;
    private String descricao;
    private boolean ativo;

    @NotNull
    @DecimalMin(value = "0.0", message = "A nota não pode ser menor que 0.")
    @DecimalMax(value = "5.0", message = "A nota não pode ser maior que 5.")
    private double notaEstabelecimento;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicoAvaliado> servicosAvaliados;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    @NotNull
    private Agendamento agendamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public double getNotaEstabelecimento() {
        return notaEstabelecimento;
    }

    public void setNotaEstabelecimento(double notaEstabelecimento) {
        this.notaEstabelecimento = notaEstabelecimento;
    }

    public List<ServicoAvaliado> getServicosAvaliados() {
        return servicosAvaliados;
    }

    public void setServicosAvaliados(List<ServicoAvaliado> servicosAvaliados) {
        this.servicosAvaliados = servicosAvaliados;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(getId(), avaliacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
