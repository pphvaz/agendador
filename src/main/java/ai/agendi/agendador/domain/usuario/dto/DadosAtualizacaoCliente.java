package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotNull;

public class DadosAtualizacaoCliente {

    @NotNull
    private Long id;
    private String novoNome;
    private String novaSenha;
    private String novoEmail;
    private String novoCelular;
    private String novaDataNascimento;

    public DadosAtualizacaoCliente() {
    }

    public DadosAtualizacaoCliente(Long id, String novoNome, String novaSenha, String novoEmail, String novoCelular, String novaDataNascimento) {
        this.id = id;
        this.novoNome = novoNome;
        this.novaSenha = novaSenha;
        this.novoEmail = novoEmail;
        this.novoCelular = novoCelular;
        this.novaDataNascimento = novaDataNascimento;
    }

    public @NotNull Long getId() {
        return id;
    }

    public void setId(@NotNull Long id) {
        this.id = id;
    }

    public String getNovoNome() {
        return novoNome;
    }

    public void setNovoNome(String novoNome) {
        this.novoNome = novoNome;
    }

    public String getNovaDataNascimento() {
        return novaDataNascimento;
    }

    public void setNovaDataNascimento(String novaDataNascimento) {
        this.novaDataNascimento = novaDataNascimento;
    }
}