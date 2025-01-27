package ai.agendi.agendador.domain.usuario.dto;

import ai.agendi.agendador.domain.usuario.model.Prestador;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DadosCadastroEstabelecimento {
        @NotEmpty
        private String nome;
        private Prestador proprietario;
        private DadosCadastroEndereco endereco;
}
