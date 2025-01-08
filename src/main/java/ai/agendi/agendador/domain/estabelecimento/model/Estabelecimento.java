package ai.agendi.agendador.domain.estabelecimento.model;

import ai.agendi.agendador.domain.endereco.model.Endereco;
import ai.agendi.agendador.domain.estabelecimento.dto.DadosEstabelecimento;
import ai.agendi.agendador.domain.usuario.model.Prestador;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "estabelecimentos")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String proprietario;
    private LocalDateTime dataCadastro;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "estabelecimento")
    private List<Endereco> enderecos;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL)
    private List<Prestador> prestadores;

    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioFuncionamento> horariosDeFuncionamento;

    public Estabelecimento(DadosEstabelecimento dadosEstabelecimento) {
        this.nome = dadosEstabelecimento.nome();
        this.dataCadastro = LocalDateTime.now();
    }

    public Estabelecimento() {}
}
