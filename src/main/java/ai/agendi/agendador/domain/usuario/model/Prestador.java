package ai.agendi.agendador.domain.usuario.model;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroPrestador;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="prestadores")
@PrimaryKeyJoinColumn(name = "idUsuario")
public class Prestador extends Usuario {

    private String nome;
    private LocalDate dataNascimento;
    private Integer anosDeExperiencia;

    @ManyToOne(fetch = FetchType.LAZY)
    private Estabelecimento estabelecimento;

    public Prestador () {}

    public Prestador(DadosCadastroPrestador dados) {
        super(dados.dadosCadastroUsuario());
        this.nome = dados.nome();
        this.dataNascimento = dados.dataNascimento();
        this.anosDeExperiencia = dados.anosDeExperiencia();
    }
}
