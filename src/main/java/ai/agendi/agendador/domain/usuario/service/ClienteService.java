package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaCliente;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public DadosRespostaCliente save(DadosCadastroCliente dados) {
        Cliente cliente = new Cliente(dados);
        try {
            clienteRepository.saveAndFlush(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Dados duplicados no banco de Dados" + e.getMessage());
        }
        return new DadosRespostaCliente(cliente);
    }

    public DadosRespostaCliente buscarClientePorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent() && cliente.get().getIdUsuario() != null) {
            return new DadosRespostaCliente(cliente.get());
        } else {
            throw new ValidacaoException("Cliente não encontrado para o id: " + id);
        }
    }

    // Apenas role_admin pode buscar todos os clientes
    public Page<DadosRespostaCliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(DadosRespostaCliente::new);
    }
}