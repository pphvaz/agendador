package ai.agendi.agendador.domain.usuario.model;


import ai.agendi.agendador.domain.usuario.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String senha;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String celular;

    @NotEmpty
    @Column(nullable = false)
    private String nome;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;

    private LocalDateTime dataAtualizacaoNascimento;

    private LocalDateTime diaDeCadastro;
    private LocalDateTime diaExclusao;
    private boolean ativo;

    @ElementCollection(targetClass = Perfil.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "usuarios_perfil",
            joinColumns = @JoinColumn(name = "usuario_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "perfis"})
    )
    @Enumerated(EnumType.STRING)
    private Set<Perfil> perfis = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDateTime getDataAtualizacaoNascimento() {
        return dataAtualizacaoNascimento;
    }

    public void setDataAtualizacaoNascimento(LocalDateTime dataAtualizacaoNascimento) {
        this.dataAtualizacaoNascimento = dataAtualizacaoNascimento;
    }

    public LocalDateTime getDiaDeCadastro() {
        return diaDeCadastro;
    }

    public void setDiaDeCadastro(LocalDateTime diaDeCadastro) {
        this.diaDeCadastro = diaDeCadastro;
    }

    public LocalDateTime getDiaExclusao() {
        return diaExclusao;
    }

    public void setDiaExclusao(LocalDateTime diaExclusao) {
        this.diaExclusao = diaExclusao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<Perfil> perfis) {
        this.perfis = perfis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}