package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.estabelecimento.model.Estabelecimento;
import ai.agendi.agendador.domain.usuario.model.Prestador;

import java.time.LocalDate;

public record DadosRespostaPrestador(
        Long id,
        String nome,
        String telefone,
        String email,
        LocalDate dataNascimento,
        Integer anosDeExperiencia,
        boolean isManager,
        boolean isOwner,
        boolean atendeDomicilio,
        Long idEstabelecimento,
        String nomeEstabelecimento,
        String donoEstabelecimento
) {
    public DadosRespostaPrestador(Prestador prestador) {
        this(prestador.getId(),
                prestador.getNome(),
                prestador.getCelular(),
                prestador.getEmail(),
                prestador.getDataNascimento(),
                prestador.getAnosDeExperiencia(),
                prestador.isManager(),
                prestador.isOwner(),
                prestador.isAtendeDomicilio(),
                prestador.getEstabelecimento().getId(),
                prestador.getEstabelecimento().getNome(),
                prestador.getEstabelecimento().getProprietario().getNome());
    }
}