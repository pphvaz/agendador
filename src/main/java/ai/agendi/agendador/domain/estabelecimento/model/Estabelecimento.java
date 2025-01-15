package ai.agendi.agendador.domain.estabelecimento.model;

import ai.agendi.agendador.domain.agendamento.model.Combo;
import ai.agendi.agendador.domain.endereco.model.Endereco;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.model.Prestador;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String nome;
    private LocalDateTime dataCadastro;
    private boolean ativo;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Prestador proprietario;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estabelecimento")
    private Endereco endereco;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL)
    private List<Prestador> prestadores;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioFuncionamento> horariosDeFuncionamento;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Servico> servicos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Cliente> clientes = new HashSet<>();

    @OneToMany(mappedBy = "estabelecimento")
    private List<Combo> combosDisponiveis;

    public Estabelecimento() {}

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

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Prestador getProprietario() {
        return proprietario;
    }

    public void setProprietario(Prestador proprietario) {
        this.proprietario = proprietario;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Prestador> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(List<Prestador> prestadores) {
        this.prestadores = prestadores;
    }

    public List<HorarioFuncionamento> getHorariosDeFuncionamento() {
        return horariosDeFuncionamento;
    }

    public void setHorariosDeFuncionamento(List<HorarioFuncionamento> horariosDeFuncionamento) {
        this.horariosDeFuncionamento = horariosDeFuncionamento;
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public Set<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Combo> getCombosDisponiveis() {
        return combosDisponiveis;
    }

    public void setCombosDisponiveis(List<Combo> combosDisponiveis) {
        this.combosDisponiveis = combosDisponiveis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estabelecimento that = (Estabelecimento) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}