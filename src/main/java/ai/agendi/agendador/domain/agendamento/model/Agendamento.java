package ai.agendi.agendador.domain.agendamento.model;

import ai.agendi.agendador.domain.agendamento.enums.MotivosCancelamento;
import ai.agendi.agendador.domain.agendamento.enums.Status;
import ai.agendi.agendador.domain.endereco.model.Endereco;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.model.Prestador;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    @OneToOne(fetch = FetchType.LAZY)
    private Prestador prestador;

    @OneToOne(fetch = FetchType.LAZY)
    private Endereco endereco;

    @NotNull
    private boolean atendimentoDomicilio;

    @NotNull
    private LocalDateTime dataDoAgendamento;

    @ElementCollection
    @CollectionTable(name = "agendamento_servicos", joinColumns = @JoinColumn(name = "agendamento_id"))
    @Column(name = "servico_id")
    private List<Long> servicosIds;

    private BigDecimal valorTotal;

    @NotEmpty
    @Min(0)
    private Integer tempoTotal;

    @Enumerated(EnumType.STRING)
    private MotivosCancelamento motivosCancelamento;

    private LocalDateTime dataDoCancelamento;

    private String motivoRemarcacao;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Avaliacao avaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remarcado_de_id")
    private Agendamento remarcadoDe; // agendamento original, se for essa entidade for remarcação

    @OneToMany(mappedBy = "remarcadoDe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agendamento> remarcacoes = new ArrayList<>();

    @NotNull
    public boolean isAtendimentoDomicilio() {
        return atendimentoDomicilio;
    }

    public void setAtendimentoDomicilio(@NotNull boolean atendimentoDomicilio) {
        this.atendimentoDomicilio = atendimentoDomicilio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(@NotNull Cliente cliente) {
        this.cliente = cliente;
    }

    public Prestador getPrestador() {
        return prestador;
    }

    public void setPrestador(@NotNull Prestador prestador) {
        this.prestador = prestador;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(@NotNull Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Long> getServicosIds() {
        return servicosIds;
    }

    public void setServicosIds(List<Long> servicosIds) {
        this.servicosIds = servicosIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(Integer tempoTotal) {
        this.tempoTotal = tempoTotal;
    }

    public LocalDateTime getDataDoAgendamento() {
        return dataDoAgendamento;
    }

    public void setDataDoAgendamento(LocalDateTime dataDoAgendamento) {
        this.dataDoAgendamento = dataDoAgendamento;
    }

    public LocalDateTime getDataDoCancelamento() {
        return dataDoCancelamento;
    }

    public void setDataDoCancelamento(LocalDateTime dataDoCancelamento) {
        this.dataDoCancelamento = dataDoCancelamento;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getMotivoRemarcacao() {
        return motivoRemarcacao;
    }

    public void setMotivoRemarcacao(String motivoRemarcacao) {
        this.motivoRemarcacao = motivoRemarcacao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MotivosCancelamento getMotivosCancelamento() {
        return motivosCancelamento;
    }

    public void setMotivosCancelamento(MotivosCancelamento motivosCancelamento) {
        this.motivosCancelamento = motivosCancelamento;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Agendamento getRemarcadoDe() {
        return remarcadoDe;
    }

    public void setRemarcadoDe(Agendamento remarcadoDe) {
        this.remarcadoDe = remarcadoDe;
    }

    public List<Agendamento> getRemarcacoes() {
        return remarcacoes;
    }

    public void setRemarcacoes(List<Agendamento> remarcacoes) {
        this.remarcacoes = remarcacoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
