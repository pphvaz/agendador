package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DadosAtualizacaoCliente {

    @NotNull
    private Long id;
    private String novoNome;
    private String novoEmail;
    private String novoCelular;
    private LocalDate novaDataNascimento;

    public DadosAtualizacaoCliente() {
    }

    public DadosAtualizacaoCliente(Long id, String novoNome, String novoEmail, String novoCelular, LocalDate novaDataNascimento) {
        this.id = id;
        this.novoNome = novoNome;
        this.novoEmail = novoEmail;
        this.novoCelular = novoCelular;
        this.novaDataNascimento = novaDataNascimento;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public void setNovoNome(String novoNome) {
        this.novoNome = novoNome;
    }

    public @NotNull Long getId() {
        return id;
    }

    public String getNovoNome() {
        return novoNome;
    }

    public LocalDate getNovaDataNascimento() {
        return novaDataNascimento;
    }

    public String getNovoEmail() {
        return novoEmail;
    }

    public String getNovoCelular() {
        return novoCelular;
    }
}