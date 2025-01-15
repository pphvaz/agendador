package ai.agendi.agendador.domain.usuario.usecase;

import org.hibernate.validator.constraints.br.CPF;

// Helper class for validation
public class CpfValidation {
    @CPF
    private String cpf;
}
