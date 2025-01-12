package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.usuario.dto.DadosAtualizacaoCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import ai.agendi.agendador.domain.usuario.dto.DadosRespostaCliente;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import ai.agendi.agendador.domain.usuario.model.Usuario;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.domain.usuario.repository.UsuarioRepository;
import ai.agendi.agendador.infra.exceptions.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DadosRespostaCliente save(DadosCadastroCliente dados) {
        String senhaCriptografada = criptografarSenha(dados.senha());

        Usuario usuario = new Usuario(
                dados.email(),
                senhaCriptografada,
                dados.celular());

        Cliente cliente = new Cliente(
                dados.email(),
                senhaCriptografada,
                dados.celular(),
                dados.nome(),
                dados.cpf(),
                dados.dataNascimento()
        );
        try {
            clienteRepository.saveAndFlush(cliente);
            usuarioRepository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage(), e.getCause());
        }
        return new DadosRespostaCliente(cliente);
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    public DadosRespostaCliente buscarClientePorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findByIdUsuarioAndAtivoTrue(id);
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

    public DadosRespostaCliente buscarClientePorCpf(String cpf) {
        return new DadosRespostaCliente(clienteRepository.findByCpfAndAtivoTrue(cpf));
    }

    public DadosRespostaCliente buscarClientePorEmail(String email) {
        return new DadosRespostaCliente(clienteRepository.findByEmailAndAtivoTrue(email));
    }

    public DadosRespostaCliente buscarClientePorCelular(String celular) {
        return new DadosRespostaCliente(clienteRepository.findByCelularAndAtivoTrue(celular));
    }

    public void deleteById(Long id) {
        int updatedRows = clienteRepository.logicalDeletionByClienteId(id);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Cliente with ID " + id + " not found.");
        }
    }

    public DadosRespostaCliente update(DadosAtualizacaoCliente atualizados) {
        Cliente cliente = clienteRepository.findById(atualizados.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (isRecentChange(cliente.getDataAtualizacaoNascimento())) {
            throw new ValidacaoException("Não é permitido alterar a data de nascimento em um curto período de tempo.");
        }

        cliente.atualizarDadosCliente(atualizados);
        clienteRepository.saveAndFlush(cliente);
        return new DadosRespostaCliente(cliente);
    }

    private boolean isRecentChange(LocalDateTime lastUpdated) {
        if (lastUpdated == null) {
            return false;
        }
        return Duration.between(lastUpdated, LocalDateTime.now()).toDays() < 30;
    }
}