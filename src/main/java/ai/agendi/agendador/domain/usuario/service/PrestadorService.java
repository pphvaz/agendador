package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.usuario.dto.DadosGeraisRespostaCliente;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.domain.usuario.usecase.CpfValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PrestadorService {

    @Autowired
    private ClienteRepository clienteRepository;

    public DadosGeraisRespostaCliente searchClienteByCpfOrCelularOrEmail(String query, Authentication authentication) {
        Cliente authenticated = (Cliente) authentication.getPrincipal();
        Cliente cliente;
        if (isEmail(query)) {
            cliente = clienteRepository.findByEmailAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        } else if (isCpf(query)) {
            cliente = clienteRepository.findByCpfAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        } else {
            cliente = clienteRepository.findByCelularAndAtivoTrue(query)
                    .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        }
        return new DadosGeraisRespostaCliente(cliente);
    }

    private boolean isEmail(String query) {
        return query.contains("@");
    }

    private boolean isCpf(String query) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validateValue(CpfValidation.class, "cpf", query).isEmpty();
    }
}
