package ai.agendi.agendador.domain.usuario.model;


import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String celular;

    private boolean ativo;

    private LocalDateTime diaDeCadastro;

    public Usuario() {}

    public Usuario(DadosCadastroUsuario dadosCadastroUsuario) {
        this.login = dadosCadastroUsuario.login();
        this.senha = dadosCadastroUsuario.senha();
        this.email = dadosCadastroUsuario.email();
        this.celular = dadosCadastroUsuario.celular();
        this.ativo = true;
        this.diaDeCadastro = LocalDateTime.now();
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getCelular() {
        return celular;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public LocalDateTime getDiaDeCadastro() {
        return diaDeCadastro;
    }
}