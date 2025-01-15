package ai.agendi.agendador.domain.usuario.service;

import ai.agendi.agendador.domain.usuario.dto.DadosCadastroCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosCadastroUsuario;
import ai.agendi.agendador.domain.usuario.dto.DadosGeraisRespostaCliente;
import ai.agendi.agendador.domain.usuario.dto.DadosPessoaisRespostaCliente;
import ai.agendi.agendador.domain.usuario.enums.Perfil;
import ai.agendi.agendador.domain.usuario.model.Cliente;
import org.springframework.security.access.AccessDeniedException;
// import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.domain.usuario.repository.ClienteRepository;
import ai.agendi.agendador.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DadosPessoaisRespostaCliente register(DadosCadastroCliente dadosCliente) {
        String senhaCriptografada = passwordEncoder.encode(dadosCliente.senha());
        Set<Perfil> perfis = new HashSet<>();
        perfis.add(Perfil.ROLE_CLIENTE);

        DadosCadastroUsuario dadosUsuario = new DadosCadastroUsuario(
                dadosCliente.email(),
                senhaCriptografada,
                dadosCliente.celular(),
                dadosCliente.nome(),
                dadosCliente.dataNascimento(),
                perfis
        );

        Cliente cliente = new Cliente(dadosUsuario, dadosCliente);
        try {
            clienteRepository.saveAndFlush(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage(), e.getCause());
        }
        return new DadosPessoaisRespostaCliente(cliente);
    }

    public DadosGeraisRespostaCliente buscarClientePorId(Long id, Authentication authentication)  {
        Cliente authenticated = (Cliente) authentication.getPrincipal();
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        if (!cliente.getId().equals(authenticated.getId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar esses dados");
        }
        return new DadosGeraisRespostaCliente(cliente);
    }

    // Apenas role_admin pode buscar todos os clientes
    public Page<DadosGeraisRespostaCliente> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(DadosGeraisRespostaCliente::new);
    }

    public void deleteById(Long id, Authentication authentication) {
        Cliente authenticated = (Cliente) authentication.getPrincipal();
        Cliente cliente = clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        if (!cliente.getId().equals(authenticated.getId())) {
            throw new AccessDeniedException("Você não tem permissão para acessar esses dados");
        }
        int updatedRows = clienteRepository.logicalDeletionByClienteId(id);
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Cliente with ID " + id + " not found.");
        }
    }
}
/*
    public DadosRespostaCliente buscarClientePorCpf(String cpf) {
        return new DadosRespostaCliente(clienteRepository.findByCpfAndAtivoTrue(cpf));
    }

    public DadosRespostaCliente buscarClientePorEmail(String email) {
        return new DadosRespostaCliente(clienteRepository.findByEmailAndAtivoTrue(email));
    }

    public DadosRespostaCliente buscarClientePorCelular(String celular) {
        return new DadosRespostaCliente(clienteRepository.findByCelularAndAtivoTrue(celular));
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
*/