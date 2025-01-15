package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.estabelecimento.model.Servico;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Entity
@Table(name ="prestadores")
@PrimaryKeyJoinColumn(name = "idUsuario")
public class Prestador extends Usuario {

    private Integer anosDeExperiencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(nullable = false, updatable = false)
    private Estabelecimento estabelecimento;

    private boolean isManager;
    private boolean isOwner;

    @NotNull
    private boolean atendeDomicilio;

    @ManyToMany
    @JoinTable(
            name = "prestador_servicos",
            joinColumns = @JoinColumn(name = "prestador_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"prestador_id", "servico_id"})
    )
    private Set<Servico> servicosOfereceridos;

    public Prestador () {}

    public Integer getAnosDeExperiencia() {
        return anosDeExperiencia;
    }

    public void setAnosDeExperiencia(Integer anosDeExperiencia) {
        this.anosDeExperiencia = anosDeExperiencia;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isAtendeDomicilio() {
        return atendeDomicilio;
    }

    public void setAtendeDomicilio(boolean atendeDomicilio) {
        this.atendeDomicilio = atendeDomicilio;
    }

    public Set<Servico> getServicosOfereceridos() {
        return servicosOfereceridos;
    }

    public void setServicosOfereceridos(Set<Servico> servicosOfereceridos) {
        this.servicosOfereceridos = servicosOfereceridos;
    }
}