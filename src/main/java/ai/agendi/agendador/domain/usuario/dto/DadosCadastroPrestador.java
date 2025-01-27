package ai.agendi.agendador.domain.usuario.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosCadastroPrestador {

        Long estabelecimentoId;

        @NotEmpty(message = "E-mail não pode ser nulo.")
        String email;

        @NotEmpty
        @Size(min = 8, message = "Senha deve ter no mínimo de 8 caracteres.")
        String senha;

        @NotEmpty
        @Pattern(regexp = "^\\d{10,11}$", message = "O celular deve conter entre 10 e 11 dígitos.")
        String celular;

        @NotEmpty
        String nome;

        @NotNull
        LocalDate dataNascimento;

        @NotNull
        Integer anosDeExperiencia;

        @NotNull
        Boolean isManager;

        @NotNull
        Boolean isOwner;

        @NotNull
        Boolean atendeDomicilio;

        DadosCadastroEstabelecimento dadosEstabelecimento;

        public DadosCadastroPrestador(Long id, String mail, String senha, String nome, String dataDeNascimento, int anosDeExperiencia, boolean isManager, boolean isOwner, boolean atendeDomicilio, DadosCadastroEstabelecimento estabelecimento) {
                this.estabelecimentoId = id;
                this.email = mail;
                this.senha = senha;
                this.nome = nome;
                this.dataNascimento = LocalDate.parse(dataDeNascimento);
                this.anosDeExperiencia = anosDeExperiencia;
                this.isManager = isManager;
                this.isOwner = isOwner;
                this.atendeDomicilio = atendeDomicilio;
                this.dadosEstabelecimento = estabelecimento;
        }
}