package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.enums.Perfil;

import java.time.LocalDate;
import java.util.Set;

public record DadosCadastroUsuario(
        String email,
        String senha,
        String celular,
        String nome,
        LocalDate dataNascimento,
        Set<Perfil> perfis) {
}