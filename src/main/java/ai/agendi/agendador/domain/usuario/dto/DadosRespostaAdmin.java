package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DadosRespostaAdmin(
        String nome,
        String email,
        String celular,
        LocalDate dataNascimento,
        LocalDateTime cadastradoDia
) {
   public DadosRespostaAdmin (Usuario usuario) {
       this(usuario.getNome(), usuario.getEmail(), usuario.getCelular(), usuario.getDataNascimento(), usuario.getDiaDeCadastro());
   }
}